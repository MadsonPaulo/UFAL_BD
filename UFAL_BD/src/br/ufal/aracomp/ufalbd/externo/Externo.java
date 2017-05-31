package br.ufal.aracomp.ufalbd.externo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import br.ufal.aracomp.ufalbd.conceitual.Conceitual;
import br.ufal.aracomp.ufalbd.conceitual.exception.BancoInvalidoException;
import br.ufal.aracomp.ufalbd.conceitual.exception.ColunaInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.exception.CondicaoInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.exception.ErroInsercaoException;
import br.ufal.aracomp.ufalbd.conceitual.exception.TabelaInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Coluna;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.ColunaImp;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Condicao;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.CondicaoComposta;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.CondicaoSimples;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Dado;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.DadoImp;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Tabela;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.TabelaImp;
import br.ufal.aracomp.ufalbd.externo.exception.InstrucaoMalFormadaException;
import br.ufal.aracomp.ufalbd.externo.metamodel.TabelaConsulta;
import br.ufal.aracomp.ufalbd.interno.Interno;
import br.ufal.aracomp.ufalbd.interno.exception.ErroAcessoDadosException;

public class Externo implements IExterno {

	private final Conceitual conceitual = new Conceitual();
	private final Interno interno = new Interno();
	private final String createDatabaseKeyword = "construir_bd";
	private final String createDatabasePattern = "'" + createDatabaseKeyword + " nomeBanco'.";
	private final String deleteDatabaseKeyword = "destruir_bd";
	private final String deleteDatabasePattern = "'" + deleteDatabaseKeyword + " nomeBanco'.";
	private final String createTableKeyword = "construir_tb";
	private final String createTablePattern = "'" + createTableKeyword
			+ " nomeBanco nomeTabela(nomeColuna1:tipoDado,nomeColuna2:tipoDado,...)'."
			+ "\nOs valores válidos para tipoDado são: 1(texto), 2(número), 3(data) e 4(bytes).";
	private final String deleteTableKeyword = "destruir_tb";
	private final String insertTupleKeyword = "adicionar";
	private final String insertTuplePattern = "'" + insertTupleKeyword + " nomeBanco nomeTabela(valor,valor,...)'";
	private final String removeTupleKeyword = "remover";
	private final String removeTuplePattern = "'" + removeTupleKeyword + " nomeBanco nomeTabela(nomeColuna:op:valor)'."
			+ "\nOs valores válidos para o operador(op) são: 1(=), 2(!=), 3(<), 4(<=), 5(>) e 6(>=)."
			+ "\nPara utilizar duas condições, use o padrão: '" + removeTupleKeyword
			+ " nomeBanco nomeTabela(nomeColuna:op:valor,lg,nomeColuna:op:valor)'."
			+ "\nOs valores válidos para o operador lógico(lg) são: 1(AND) e 2(OR).";;
	private final String selectKeyword = "buscar";
	private final String selectPattern = "Buscar tabela inteira: '" + selectKeyword + " nomeBanco nomeTabela()'."
			+ "\nBuscar colunas específicas: '" + selectKeyword + " nomeBanco nomeTabela(nomeColuna;nomeColuna;...)'."
			+ "\nBuscar com condição: '" + selectKeyword + " nomeBanco nomeTabela(nomeColuna:op:valor)'."
			+ "\nBuscar com duas condições: '" + selectKeyword
			+ " nomeBanco nomeTabela(nomeColuna:op:valor,lg,nomeColuna:op:valor)'."
			+ "\nOs valores válidos para o operador(op) são: 1(=), 2(!=), 3(<), 4(<=), 5(>) e 6(>=)."
			+ "\nOs valores válidos para o operador lógico(lg) são: 1(AND) e 2(OR).";
	private final String invalidDDLCommandErrorMessage = "Comando DDL inválido. Os comandos válidos são: '"
			+ createDatabaseKeyword + "', '" + deleteDatabaseKeyword + "', '" + createTableKeyword + "' e '"
			+ deleteTableKeyword + "'.";
	private final String invalidDMLCommandErrorMessage = "Comando DML inválido. Os comandos válidos são: '"
			+ insertTupleKeyword + "' e '" + removeTupleKeyword + "'.";
	private final String invalidDMLSelect = "Comando DML de busca inválido. Os comandos válidos são: '" + selectKeyword
			+ "'.";
	private final String invalidNumberOfArgumentsMessage = "A quantidade de argumentos inserida é inválida.";
	private final String blankSpacesTipMessage = "Lembre que não podem haver espaços entre os tipos de coluna ou nos nomes de coluna."
			+ "\nO padrão é: 'CONSTRUIR_TB nomeBanco nomeTabela(coluna1:tipoDado,coluna2:tipoDado,...)'";
	private final String invalidColumnDataMessage = "Alguma coluna possui dados inválidos, como por exemplo: '('.";
	private final String validColumnTypesMessage = "Os tipos válidos são: 1(Texto), 2(Número), 3(Data) e 4(Byte).";
	private final String insufficientColumnMessage = "Não foram informados dados suficientes para criar nenhuma columa.";
	private final String localizedInvalidColumnDataMessage = "A seguinte coluna possui dados inválidos: ";
	private final String sameColumnNameMessage = "Não podem haver colunas com o mesmo nome.";
	private final String invalidFileName = "O nome do banco de dados/da tabela não pode conter os seguintes caracteres: '\\', '/', ':', '*', '?', '\"', '<', '>', '|'.";

	@Override
	public boolean executarDDL(String instrucao) throws InstrucaoMalFormadaException {
		if (instrucao.equals(createDatabaseKeyword)) {
			System.out.println("Utilize o seguinte padrão:\n" + createDatabasePattern);
			return false;
		} else if (instrucao.equals(deleteDatabaseKeyword)) {
			System.out.println("Utilize o seguinte padrão:\n" + deleteDatabasePattern);
			return false;
		} else if (instrucao.equals(createTableKeyword)) {

			return false;
		} else if (instrucao.equals(deleteTableKeyword)) {

			return false;
		}

		String[] comandos = instrucao.split(" ");

		if (comandos.length <= 1) {
			System.err.println(invalidDDLCommandErrorMessage);
			return false;
		} else {
			// verifica se o nome de arquivo é válido
			if (isDatabaseNameValid(comandos[1]) == false) {
				System.err.println(invalidFileName);
				return false;
			}

			if (comandos[0].equalsIgnoreCase(createDatabaseKeyword)) {
				// criar banco
				try {
					return conceitual.criarBanco(comandos[1]);
				} catch (BancoInvalidoException e) {
					e.printStackTrace();
					return false;
				}
			} else if (comandos[0].equalsIgnoreCase(deleteDatabaseKeyword)) {
				// deletar banco
				try {
					return conceitual.apagarBanco(comandos[1]);
				} catch (BancoInvalidoException e) {
					e.printStackTrace();
					return false;
				}
			} else if (comandos[0].equalsIgnoreCase(createTableKeyword)) {
				// criar tabela
				if (comandos.length != 3) {
					System.err.println(invalidNumberOfArgumentsMessage + "\n" + blankSpacesTipMessage);
					return false;
				} else if (instrucao.contains("(") == false) {
					System.err.println("É preciso informar o nome e tipo de dado das colunas entre parênteses. "
							+ "Utilize o seguinte padrão:\n" + createTablePattern);
					return false;
				} else {
					// identifica o nome do banco
					String nomeBanco = comandos[1];
					comandos = comandos[2].split("\\(");
					// identifica o nome da tabela
					String nomeTabela = comandos[0];
					// verifica se o nome da tabela é válido
					if (isDatabaseNameValid(nomeTabela) == false) {
						System.err.println(invalidFileName);
						return false;
					}
					// remove ), caso existam
					comandos[1] = comandos[1].replaceAll("\\)", "");
					// cria o array de colunas
					ArrayList<Coluna> colunas = new ArrayList<Coluna>();

					comandos = comandos[1].split(",");
					for (String dado : comandos) {
						String[] dadosColuna = dado.split(":");
						if (dadosColuna.length == 2) {
							try {
								int tipoDado = Integer.valueOf(dadosColuna[1]);
								if (tipoDado > 0 && tipoDado < 5) {
									colunas.add(new ColunaImp(nomeBanco, nomeTabela, dadosColuna[0], tipoDado));
								} else {
									System.err.println(localizedInvalidColumnDataMessage + dadosColuna[0] + ". "
											+ validColumnTypesMessage);
									return false;
								}
							} catch (Exception e) {
								System.err.println(localizedInvalidColumnDataMessage + dadosColuna[0] + ". "
										+ validColumnTypesMessage);
								return false;
							}
						} else {
							System.err.println(invalidColumnDataMessage);
							return false;
						}
					}

					// se há pelo menos uma coluna
					if (colunas.size() > 0) {
						// checa por colunas com nome repetido
						ArrayList<String> columnNames = new ArrayList<String>();
						for (Coluna col : colunas) {
							columnNames.add(col.obterNomeColuna());
						}
						Set<String> hashSet = new HashSet<String>(columnNames);
						if (hashSet.size() < colunas.size()) {
							// há colunas com mesmo nome
							System.err.println(sameColumnNameMessage);
							return false;
						} else {
							Tabela tabela = new TabelaImp(nomeTabela, colunas);
							try {
								return conceitual.criarTabela(nomeBanco, tabela);
							} catch (BancoInvalidoException | TabelaInvalidaException e) {
								e.printStackTrace();
								return false;
							}
						}
					} else {
						// se não há nenhuma coluna
						System.err.println(insufficientColumnMessage);
						return false;
					}
				}
			} else if (comandos[0].equalsIgnoreCase(deleteTableKeyword)) {
				// deletar tabela
				if (comandos.length < 3) {
					System.err.println(invalidDDLCommandErrorMessage);
					return false;
				} else {
					try {
						// verifica se o nome do banco de dados e da tabela são
						// válidos
						if (isDatabaseNameValid(comandos[1]) == false || isDatabaseNameValid(comandos[2]) == false) {
							System.err.println(invalidFileName);
							return false;
						}
						return conceitual.apagarTabela(comandos[1], comandos[2]);
					} catch (BancoInvalidoException | TabelaInvalidaException e) {
						e.printStackTrace();
						return false;
					}
				}
			} else {
				System.err.println(invalidDDLCommandErrorMessage);
				return false;
			}
		}
	}

	@Override
	public int executarDMLInstrucao(String instrucao) throws InstrucaoMalFormadaException {
		if (instrucao.equalsIgnoreCase(insertTupleKeyword)) {
			System.out.println("Utilize o seguinte padrão:\n" + insertTuplePattern);
			return 0;
		} else if (instrucao.equalsIgnoreCase(removeTupleKeyword)) {
			System.out.println("Utilize o seguinte padrão:\n" + removeTuplePattern);
			return 0;
		}

		String[] comandos = instrucao.split(" ");

		if (comandos.length <= 2) {
			System.err.println(invalidDMLCommandErrorMessage);
			return 0;
		} else {
			if (comandos[0].equalsIgnoreCase(insertTupleKeyword)) {
				// inserir tupla
				// separa comandos e argumentos
				if (instrucao.contains("(")) {

					comandos = instrucao.split("\\(");
					// remove )
					comandos[1] = comandos[1].replaceAll("\\)", "");
					// obtém nome do banco e da tabela
					String nomeBanco = comandos[0].split(" ")[1];
					String nomeTabela = comandos[0].split(" ")[2];
					// verifica se o nome do banco e tabela são válidos
					if (isDatabaseNameValid(nomeBanco) == false || isDatabaseNameValid(nomeTabela) == false) {
						System.err.println(invalidFileName);
						return 0;
					}
					try {
						// obtém a tabela em questão
						Tabela tabela = interno.getTabela(nomeBanco, nomeTabela);
						// lista de colunas da tabela
						ArrayList<Coluna> colunas = new ArrayList<Coluna>(tabela.getColunas());
						// array que conterá os dados informados pelo usuário
						ArrayList<Dado> dados = new ArrayList<Dado>();

						// separa os argumentos
						String[] data = comandos[1].split(",");

						// checa se a quantidade de argumentos é igual à de
						// colunas
						if (data.length != colunas.size()) {
							System.err.println(
									"A quantidade de argumentos deve ser igual à quantidade de colunas da tabela.");
							return 0;
						} else {
							// adiciona os dados ao array
							for (int i = 0; i < colunas.size(); i++) {
								dados.add(new DadoImp(colunas.get(i), data[i]));
							}
						}

						try {
							// insere a tupla
							if (conceitual.inserirTupla(nomeBanco, nomeTabela, dados)) {
								return 1;
							} else {
								return 0;
							}
						} catch (ErroInsercaoException | BancoInvalidoException | TabelaInvalidaException e) {
							e.printStackTrace();
							return 0;
						}
					} catch (IOException e) {
						e.printStackTrace();
						return 0;
					}
				} else {
					System.err.println("É preciso informar os argumentos entre parênteses. Utilize o seguinte padrão: "
							+ insertTuplePattern);
					return 0;
				}
			} else if (comandos[0].equalsIgnoreCase(removeTupleKeyword)) {
				// remover tupla
				// separa comandos e argumentos
				if (instrucao.contains("(")) {
					comandos = instrucao.split("\\(");
					// remove ) e ;
					comandos[1] = comandos[1].replaceAll("\\)", "");
					comandos[1] = comandos[1].replaceAll("\\;", "");
					// obtém nome do banco e da tabela
					String nomeBanco = comandos[0].split(" ")[1];
					String nomeTabela = comandos[0].split(" ")[2];
					// verifica se o nome do banco e tabela são válidos
					if (isDatabaseNameValid(nomeBanco) == false || isDatabaseNameValid(nomeTabela) == false) {
						System.err.println(invalidFileName);
						return 0;
					}
					try {
						// obtém a tabela em questão
						Tabela tabela = interno.getTabela(nomeBanco, nomeTabela);
						// lista de colunas da tabela
						ArrayList<Coluna> colunas = new ArrayList<Coluna>(tabela.getColunas());
						// nome das colunas
						ArrayList<String> columnNames = new ArrayList<String>();
						for (Coluna col : colunas) {
							columnNames.add(col.obterNomeColuna());
						}

						// possível condição composta
						if (comandos[1].contains(",")) {
							comandos = comandos[1].split(",");
							// quantidade de vírgulas inválida
							if (comandos.length != 3) {
								System.err.println("As condições estão fora do padrão.");
								return 0;
							} else {// quantidade válida de vírgulas
								try {
									String[] cond1 = comandos[0].split(":");
									int lg = Integer.valueOf(comandos[1]);
									String[] cond2 = comandos[2].split(":");

									int op1 = Integer.valueOf(cond1[1]);
									int op2 = Integer.valueOf(cond2[1]);
									// realiza algumas validações
									if (cond1.length != 3 || cond2.length != 3 || op1 < 1 || op1 > 6 || op2 < 1
											|| op2 > 6 || lg < 1 || lg > 2) {
										System.err.println("As condições e/ou operadores estão fora do padrão.");
										return 0;
									}

									// as colunas informadas existem
									if (columnNames.contains(cond1[0]) == false) {
										System.err.println("A coluna informada na primeira condição não existe.");
										return 0;
									} else if (columnNames.contains(cond2[0]) == false) {
										System.err.println("A coluna informada na segunda condição não existe.");
										return 0;
									}

									// cria as condições
									Condicao condicao1 = new CondicaoSimples(colunas.get(columnNames.indexOf(cond1[0])),
											op1, cond1[2]);
									Condicao condicao2 = new CondicaoSimples(colunas.get(columnNames.indexOf(cond2[0])),
											op2, cond2[2]);
									Condicao condicaoComposta = new CondicaoComposta(condicao1, condicao2, lg);

									// número de tuplas antes de tentar excluir
									int numTuplasAntes = interno.obterTuplas(nomeBanco, nomeTabela).obterNumeroTuplas();
									// tenta executar o comando de exclusão
									if (conceitual.excluirTupla(nomeBanco, nomeTabela, condicaoComposta)) {
										int numTuplasDepois = interno.obterTuplas(nomeBanco, nomeTabela)
												.obterNumeroTuplas();
										return numTuplasAntes - numTuplasDepois;
									} else {
										return 0;
									}

								} catch (NumberFormatException e) {
									System.out.println("Algum operador possui valor inválido."
											+ "\nOs valores válidos para o operador(op) são: 1(=), 2(!=), 3(<), 4(<=), 5(>) e 6(>=)."
											+ "\nOs valores válidos para o operador lógico(lg) são: 1(AND) e 2(OR).");
									return 0;
								} catch (CondicaoInvalidaException | BancoInvalidoException
										| TabelaInvalidaException f) {
									f.printStackTrace();
									return 0;
								} catch (ErroAcessoDadosException e) {
									e.printStackTrace();
									return 0;
								}
							}
						} else {// condição simples
							try {
								String[] cond1 = comandos[0].split(":");
								int op1 = Integer.valueOf(cond1[1]);
								// realiza algumas validações
								if (cond1.length != 3 || op1 < 1 || op1 > 6) {
									System.err.println("A condição e/ou operador estão fora do padrão.");
									return 0;
								}

								// as colunas informadas existem
								if (columnNames.contains(cond1[0]) == false) {
									System.err.println("A coluna informada não existe.");
									return 0;
								}

								// cria as condições
								Condicao condicao1 = new CondicaoSimples(colunas.get(columnNames.indexOf(cond1[0])),
										op1, cond1[2]);
								int numTuplasAntes = interno.obterTuplas(nomeBanco, nomeTabela).obterNumeroTuplas();
								if (conceitual.excluirTupla(nomeBanco, nomeTabela, condicao1)) {
									int numTuplasDepois = interno.obterTuplas(nomeBanco, nomeTabela)
											.obterNumeroTuplas();
									return numTuplasAntes - numTuplasDepois;
								} else {
									return 0;
								}

							} catch (NumberFormatException e) {
								System.err.println("Algum operador possui valor inválido."
										+ "\nOs valores válidos para o operador(op) são: 1(=), 2(!=), 3(<), 4(<=), 5(>) e 6(>=)."
										+ "\nOs valores válidos para o operador lógico(lg) são: 1(AND) e 2(OR).");
								return 0;
							} catch (CondicaoInvalidaException | BancoInvalidoException | TabelaInvalidaException f) {
								f.printStackTrace();
								return 0;
							} catch (ErroAcessoDadosException e) {
								e.printStackTrace();
								return 0;
							}
						}
					} catch (IOException e1) {
						e1.printStackTrace();
						return 0;
					}

				} else {
					System.err.println("É preciso informar os argumentos entre parênteses. Utilize o seguinte padrão: "
							+ removeTuplePattern);
					return 0;
				}
			} else {
				System.err.println(invalidDMLCommandErrorMessage);
				return 0;
			}
		}
	}

	@Override
	public TabelaConsulta executarDMLConsulta(String instrucao) throws InstrucaoMalFormadaException {
		String[] comandos = instrucao.split(" ");

		if (instrucao.equalsIgnoreCase(selectKeyword)) {
			System.out.println("Utilize o seguinte padrão:\n" + selectPattern);
			return null;
		}

		// comando correto
		if (comandos[0].equalsIgnoreCase(selectKeyword)) {
			// separa comandos e argumentos
			if (instrucao.contains("(")) {

				// remove ) e quebra no primeiro (
				comandos = instrucao.replaceAll("\\)", "").split("\\(");
				// checa se o nome do banco e da tabela foram inseridos
				if (comandos[0].split(" ").length != 3) {
					System.err.println("Comando inválido. Utilize o padrão:\n" + selectPattern);
					return null;
				}

				// obtém nome do banco e da tabela
				String nomeBanco = comandos[0].split(" ")[1];
				String nomeTabela = comandos[0].split(" ")[2];
				// verifica se o nome do banco e tabela são válidos
				if (isDatabaseNameValid(nomeBanco) == false || isDatabaseNameValid(nomeTabela) == false) {
					System.err.println(invalidFileName);
					return null;
				}
				try {
					// obtém a tabela em questão
					Tabela tabela = interno.getTabela(nomeBanco, nomeTabela);
					// lista de colunas da tabela
					ArrayList<Coluna> colunas = new ArrayList<Coluna>(tabela.getColunas());
					// nome das colunas
					ArrayList<String> columnNames = new ArrayList<String>();
					for (Coluna col : colunas) {
						columnNames.add(col.obterNomeColuna());
					}

					if (comandos.length == 1) {// buscar tabela completa
						try {
							return interno.obterTuplas(nomeBanco, nomeTabela);
						} catch (BancoInvalidoException | TabelaInvalidaException | ErroAcessoDadosException e) {
							e.printStackTrace();
							return null;
						}
					} else if (comandos[1].contains(";")) {// colunas
															// específicas
						// separa os nomes de coluna
						comandos = comandos[1].split(";");
						// checa se as colunas existem
						for (String colName : comandos) {
							if (columnNames.contains(colName) == false) {
								System.err.println("A coluna '" + colName + "' não existe.");
								return null;
							}
						}
						// ArrayList com as colunas específicas
						ArrayList<Coluna> colunasEspecificas = new ArrayList<Coluna>();
						// preenche o array
						for (String colName : comandos) {
							int index = columnNames.indexOf(colName);
							colunasEspecificas.add(colunas.get(index));
						}
						// chama o conceitual
						try {
							return conceitual.consultar(nomeBanco, nomeTabela, colunasEspecificas);
						} catch (BancoInvalidoException | TabelaInvalidaException | ColunaInvalidaException
								| CondicaoInvalidaException e) {
							e.printStackTrace();
							return null;
						}
					} else if (comandos[1].contains(",")) {// condição composta
						comandos = comandos[1].split(",");
						// quantidade de vírgulas inválida
						if (comandos.length != 3) {
							System.err.println("As condições estão fora do padrão.");
							return null;
						} else {// quantidade válida de vírgulas
							try {
								String[] cond1 = comandos[0].split(":");
								int lg = Integer.valueOf(comandos[1]);
								String[] cond2 = comandos[2].split(":");

								int op1 = Integer.valueOf(cond1[1]);
								int op2 = Integer.valueOf(cond2[1]);
								// realiza algumas validações
								if (cond1.length != 3 || cond2.length != 3 || op1 < 1 || op1 > 6 || op2 < 1 || op2 > 6
										|| lg < 1 || lg > 2) {
									System.err.println("As condições e/ou operadores estão fora do padrão.");
									return null;
								}

								// as colunas informadas existem
								if (columnNames.contains(cond1[0]) == false) {
									System.err.println("A coluna informada na primeira condição não existe.");
									return null;
								} else if (columnNames.contains(cond2[0]) == false) {
									System.err.println("A coluna informada na segunda condição não existe.");
									return null;
								}

								// cria as condições
								Condicao condicao1 = new CondicaoSimples(colunas.get(columnNames.indexOf(cond1[0])),
										op1, cond1[2]);
								Condicao condicao2 = new CondicaoSimples(colunas.get(columnNames.indexOf(cond2[0])),
										op2, cond2[2]);
								Condicao condicaoComposta = new CondicaoComposta(condicao1, condicao2, lg);
								// arraylist com o nome da tabela
								ArrayList<String> nomesTabelas = new ArrayList<String>();
								nomesTabelas.add(nomeTabela);

								// invoca o conceitual
								try {
									return conceitual.consultar(nomeBanco, nomesTabelas, colunas, condicaoComposta);
								} catch (BancoInvalidoException | TabelaInvalidaException | ColunaInvalidaException
										| CondicaoInvalidaException e) {
									e.printStackTrace();
									return null;
								}

							} catch (NumberFormatException e) {
								System.err.println("Algum operador possui valor inválido."
										+ "\nOs valores válidos para o operador(op) são: 1(=), 2(!=), 3(<), 4(<=), 5(>) e 6(>=)."
										+ "\nOs valores válidos para o operador lógico(lg) são: 1(AND) e 2(OR).");
								return null;
							}
						}
					} else {// condição simples
						if (comandos[1].contains(":")) {
							comandos = comandos[1].split(":");
							// checa a quantidade de :
							if (comandos.length != 3) {
								System.err.println("Busca inválida. Utilize o seguinte padrão:\n" + selectPattern);
								return null;
							}
							// checa a coluna
							if (columnNames.contains(comandos[0]) == false) {
								System.err.println("A coluna '" + comandos[0] + "' não existe.");
								return null;
							}
							try {
								// checa o operador
								int op = Integer.valueOf(comandos[1]);
								if (op < 1 || op > 6) {
									System.err.println("Operador inválido."
											+ "\nOs valores válidos para o operador(op) são: 1(=), 2(!=), 3(<), 4(<=), 5(>) e 6(>=).");
									return null;
								}
								Coluna coluna = colunas.get(columnNames.indexOf(comandos[0]));
								Condicao condicao = new CondicaoSimples(coluna, op, comandos[2]);
								// arraylist com o nome da tabela
								ArrayList<String> nomesTabelas = new ArrayList<String>();
								nomesTabelas.add(nomeTabela);
								// invoca o conceitual
								try {
									return conceitual.consultar(nomeBanco, nomesTabelas, colunas, condicao);
								} catch (BancoInvalidoException | TabelaInvalidaException | ColunaInvalidaException
										| CondicaoInvalidaException e) {
									e.printStackTrace();
									return null;
								}
							} catch (NumberFormatException e) {
								System.err.println("Operador inválido."
										+ "\nOs valores válidos para o operador(op) são: 1(=), 2(!=), 3(<), 4(<=), 5(>) e 6(>=).");
								return null;
							}
						} else {
							System.err.println("Busca inválida. Utilize o seguinte padrão:\n" + selectPattern);
							return null;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			} else {
				System.err.println("É preciso informar os argumentos entre parênteses. Utilize o seguinte padrão:\n"
						+ selectPattern);
				return null;
			}
		} else {// comando inválido
			System.err.println(invalidDMLSelect);
			return null;
		}
	}

	private boolean isDatabaseNameValid(String databaseName) {
		if (databaseName.contains("\"") || databaseName.contains("\\") || databaseName.contains("/")
				|| databaseName.contains(":") || databaseName.contains("*") || databaseName.contains("?")
				|| databaseName.contains("<") || databaseName.contains(">") || databaseName.contains("|")
				|| databaseName.trim().length() < 1) {
			return false;
		}
		return true;
	}
}