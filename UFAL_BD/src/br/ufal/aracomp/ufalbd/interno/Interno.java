package br.ufal.aracomp.ufalbd.interno;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.ufal.aracomp.ufalbd.conceitual.exception.BancoInvalidoException;
import br.ufal.aracomp.ufalbd.conceitual.exception.TabelaInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Coluna;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.ColunaImp;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Dado;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.DadoImp;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Tabela;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.TabelaImp;
import br.ufal.aracomp.ufalbd.externo.metamodel.TabelaConsulta;
import br.ufal.aracomp.ufalbd.externo.metamodel.TabelaConsultaImp;
import br.ufal.aracomp.ufalbd.interno.exception.ErroAcessoDadosException;

/**
 * M�dulo interno
 * 
 * @author Madson
 *
 */
public class Interno implements IInterno {
	private final String path = System.getProperty("user.home") + File.separator + "UFAL_BD" + File.separator;
	public final String separator = "@sep@";

	@Override
	public boolean criarBanco(String nomeBanco) throws BancoInvalidoException, ErroAcessoDadosException {
		if (nomeBanco == null || nomeBanco.trim().isEmpty()) {// nome inv�lido
			throw new BancoInvalidoException("O nome deste banco de dados � inv�lido.");
		}
		File databaseDirectory = new File(path + nomeBanco);

		if (databaseDirectory.exists()) {// banco de dados existe
			throw new BancoInvalidoException(nomeBanco + " j� existe. Sua localiza��o �: " + databaseDirectory + ".");
		} else if (databaseDirectory.mkdirs()) {// banco de dados � criado
			System.out.println(nomeBanco + " foi criado com sucesso. Sua localiza��o �: " + databaseDirectory + ".");
			return true;
		} else {// qualquer outro caso
			throw new ErroAcessoDadosException("N�o foi poss�vel acessar o banco de dados.");
		}
	}

	@Override
	public boolean apagarBanco(String nomeBanco) throws BancoInvalidoException, ErroAcessoDadosException {
		if (nomeBanco == null || nomeBanco.trim().isEmpty()) {// nome inv�lido
			throw new BancoInvalidoException("O nome deste banco de dados � inv�lido.");
		}
		File databaseDirectory = new File(path + nomeBanco);

		if (databaseDirectory.exists()) {// banco de dados existe
			// lista com todos os arquivos (tabelas) dentro da pasta (banco)
			String[] entries = databaseDirectory.list();
			// apaga todos os arquivos dentro do banco
			for (String s : entries) {
				File currentFile = new File(databaseDirectory.getPath(), s);
				currentFile.delete();
			}
			// apaga o banco
			databaseDirectory.delete();
			System.out.println(nomeBanco + " exclu�do com sucesso de " + path + ".");
			return true;
		} else {// banco de dados n�o existe
			throw new BancoInvalidoException(nomeBanco + " n�o exite em " + path + ".");
		}
	}

	@Override
	public boolean criarTabela(String nomeBanco, Tabela descricaoTabela)
			throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException {
		try {
			if (nomeBanco == null || nomeBanco.trim().isEmpty()) {// nome
																	// inv�lido
				throw new BancoInvalidoException("O nome deste banco de dados � inv�lido.");
			}
			if (descricaoTabela == null || descricaoTabela.getNome().trim().isEmpty()) {// nome
																						// inv�lido
				throw new TabelaInvalidaException("O nome desta tabela � inv�lido.");
			}
			File databaseDirectory = new File(path + nomeBanco);
			if (databaseDirectory.exists()) {// banco de dados existe
				File tableDirectory = new File(path + nomeBanco + File.separator + descricaoTabela.getNome());

				if (tableDirectory.exists()) {// tabela existe
					throw new TabelaInvalidaException("A tabela " + descricaoTabela.getNome()
							+ " j� existe no banco de dados " + nomeBanco + ".");
				} else {// tabela n�o existe
					// cria a tabela
					FileWriter fW = new FileWriter(tableDirectory, true);
					PrintWriter printWriter = new PrintWriter(fW);
					String data = "";// nome e tipo das colunas
					// salva as informa��es das colunas na primeira linha da
					// tabela
					for (Coluna col : descricaoTabela.getColunas()) {
						data += col.obterNomeColuna() + separator + col.getTipoColuna() + separator;
					}
					printWriter.println(data);
					fW.close();
					System.out.println("A tabela " + descricaoTabela.getNome()
							+ " foi criada com sucesso no banco de dados " + nomeBanco + ".");
					return true;
				}
			} else {// banco de dados n�o existe
				throw new BancoInvalidoException(nomeBanco + " n�o existe em " + path + ".");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean apagarTabela(String nomeBanco, String nomeTabela)
			throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException {
		if (nomeBanco == null || nomeBanco.trim().isEmpty()) {// nome inv�lido
			throw new BancoInvalidoException("O nome deste banco de dados � inv�lido.");
		}
		if (nomeTabela == null || nomeTabela.trim().isEmpty()) {// nome inv�lido
			throw new TabelaInvalidaException("O nome desta tabela � inv�lido.");
		}
		File databaseDirectory = new File(path + nomeBanco);
		if (databaseDirectory.exists()) {// banco de dados existe
			File tableDirectory = new File(path + nomeBanco + File.separator + nomeTabela);

			if (tableDirectory.exists()) {// tabela existe
				tableDirectory.delete();
				System.out.println(
						"A tabela " + nomeTabela + " foi exclu�da com sucesso do banco de dados " + nomeBanco + ".");
				return true;
			} else {// tabela n�o existe
				throw new TabelaInvalidaException(
						"A tabela " + nomeTabela + " n�o existe no banco de dados " + nomeBanco + ".");
			}
		} else {// banco de dados n�o existe
			throw new BancoInvalidoException(nomeBanco + " n�o existe em " + path + ".");
		}
	}

	@Override
	public boolean inserirTupla(String nomeBanco, String nomeTabela, List<Dado> dados)
			throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException {
		if (nomeBanco == null || nomeBanco.trim().isEmpty()) {// nome inv�lido
			throw new BancoInvalidoException("O nome deste banco de dados � inv�lido.");
		}
		if (nomeTabela == null || nomeTabela.trim().isEmpty()) {// nome inv�lido
			throw new TabelaInvalidaException("O nome desta tabela � inv�lido.");
		}
		if (dados == null || dados.isEmpty()) {// lista inv�lida
			throw new ErroAcessoDadosException("N�o h� dados para serem inseridos.");
		}

		File databaseDirectory = new File(path + nomeBanco);
		if (databaseDirectory.exists()) {// se o banco de dados existe
			File tableDirectory = new File(path + nomeBanco + File.separator + nomeTabela);

			if (tableDirectory.exists()) {// tabela existe
				try {
					// recebe o formato atual da tabela
					Tabela table = getTabela(nomeBanco, nomeTabela);
					if (table.getColunas().size() != dados.size()) {
						throw new ErroAcessoDadosException(
								"N�o � poss�vel inserir " + dados.size() + " dados. A tabela " + table.getNome()
										+ " possui " + table.getColunas().size() + " colunas.");
					}
					for (int i = 0; i < dados.size(); i++) {
						if (dados.get(i).obterColuna().getTipoColuna() != table.getColunas().get(i).getTipoColuna()) {
							throw new ErroAcessoDadosException("O tipo de dado a ser inserido na coluna "
									+ table.getColunas().get(i).obterNomeColuna() + " � diferente do tipo esperado.");
						}
					}

					String data = "";
					FileWriter fW = new FileWriter(tableDirectory, true);
					PrintWriter printWriter = new PrintWriter(fW);

					// transforma os dados numa �nica string
					for (int i = 0; i < dados.size(); i++) {
						int tipo = dados.get(i).obterColuna().getTipoColuna();
						if (tipo == Coluna.DATA_HORA) {// transforma em
														// getTime() para salvar
							try {
								DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
								Date date = format.parse(dados.get(i).obterValorDado().toString());
								data += date.getTime();
							} catch (ParseException e) {
								printWriter.close();
								throw new ErroAcessoDadosException(
										"A data '" + dados.get(i).obterValorDado().toString() + "' � inv�lida.");
							}

						} else if (tipo == Coluna.NUMERO) {
							try {
								Double.valueOf(dados.get(i).obterValorDado().toString());
								data += dados.get(i).obterValorDado().toString();
							} catch (NumberFormatException e) {
								printWriter.close();
								throw new ErroAcessoDadosException(
										"O n�mero '" + dados.get(i).obterValorDado().toString() + "' � inv�lido.");
							}
						} else {
							data += dados.get(i).obterValorDado().toString();
						}

						if (i < dados.size() - 1) {// se n�o for o ultimo dado
							data += separator;
						} else {// se for o ultimo dado, quebra uma linha
							data += "\n";
						}
					}
					// grava a tupla na tabela
					printWriter.printf(data);
					fW.close();
					System.out.println("Tupla inserida com sucesso.");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			} else {// tabela n�o existe
				throw new TabelaInvalidaException(
						"A tabela " + nomeTabela + " n�o existe no banco de dados " + nomeBanco + ".");
			}
		} else {// se o banco de dados n�o existe
			throw new BancoInvalidoException(nomeBanco + " n�o existe em " + path + ".");
		}
	}

	@Override
	public boolean excluirTupla(String nomeBanco, String nomeTabela, int ordem)
			throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException {
		if (nomeBanco == null || nomeBanco.trim().isEmpty()) {// nome inv�lido
			throw new BancoInvalidoException("O nome deste banco de dados � inv�lido.");
		}
		if (nomeTabela == null || nomeTabela.trim().isEmpty()) {// nome inv�lido
			throw new TabelaInvalidaException("O nome desta tabela � inv�lido.");
		}
		if (ordem < 1) {// ordem inv�lida
			throw new ErroAcessoDadosException("N�o existe tupla na posi��o " + ordem + ".");
		}
		File databaseDirectory = new File(path + nomeBanco);
		if (databaseDirectory.exists()) {// se o banco de dados existe
			File tableDirectory = new File(path + nomeBanco + File.separator + nomeTabela);

			if (tableDirectory.exists()) {// tabela existe
				try {
					Path local = tableDirectory.toPath();
//					Charset charset = Charset.forName("UTF8");
					Charset charset = Charset.forName("Cp1252");
					ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(local, charset);

					lines.remove(ordem);
					Files.write(local, lines);
					System.out.println("Tupla removida com sucesso.");
					return true;
				} catch (IndexOutOfBoundsException i) {
					throw new ErroAcessoDadosException("N�o existe tupla na posi��o " + ordem + ".");
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			} else {// tabela n�o existe
				throw new TabelaInvalidaException(
						"A tabela " + nomeTabela + " n�o existe no banco de dados " + nomeBanco + ".");
			}
		} else {// se o banco de dados n�o existe
			throw new BancoInvalidoException(nomeBanco + " n�o existe em " + path + ".");
		}
	}

	@Override
	public TabelaConsulta obterTuplas(String nomeBanco, String nomeTabela)
			throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException {
		if (nomeBanco == null || nomeBanco.trim().isEmpty()) {// nome inv�lido
			throw new BancoInvalidoException("O nome deste banco de dados � inv�lido.");
		}
		if (nomeTabela == null || nomeTabela.trim().isEmpty()) {// nome inv�lido
			throw new TabelaInvalidaException("O nome desta tabela � inv�lido.");
		}
		File databaseDirectory = new File(path + nomeBanco);
		if (databaseDirectory.exists()) {// se o banco de dados existe
			File tableDirectory = new File(path + nomeBanco + File.separator + nomeTabela);

			if (tableDirectory.exists()) {// tabela existe
				try {
					// recebe o formato atual da tabela
					Tabela table = getTabela(nomeBanco, nomeTabela);
					// ArrayList para criar a tabelaConsultaImp
					ArrayList<ArrayList<Dado>> arrayTuplas = new ArrayList<>();
					// necess�rios para ler o arquivo
					FileInputStream fis = new FileInputStream(tableDirectory);
					BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
					// linha a ser lida
					String line = reader.readLine();
					line = reader.readLine();// pula o cabe�alho
					while (line != null) {
						ArrayList<Dado> dados = new ArrayList<>();
						String[] pedacos = line.split(separator);
						if (pedacos.length != table.getColunas().size()) {
							// fecha os leitores
							reader.close();
							fis.close();
							throw new ErroAcessoDadosException(
									"A tabela foi corrompida. H� tuplas que possuem um n�mero maior de dados do que a quantidade "
											+ "de colunas existentes na tabela.");
						}
						// adiciona os dados ao array da tupla
						for (int i = 0; i < pedacos.length; i++) {
							Dado novoDado = null;
							if (table.getColunas().get(i).getTipoColuna() == Coluna.TEXTO) {
								novoDado = new DadoImp(table.getColunas().get(i), pedacos[i]);
							} else if (table.getColunas().get(i).getTipoColuna() == Coluna.NUMERO) {
								novoDado = new DadoImp(table.getColunas().get(i), Integer.valueOf(pedacos[i]));
							} else if (table.getColunas().get(i).getTipoColuna() == Coluna.DATA_HORA) {
								novoDado = new DadoImp(table.getColunas().get(i), new Date(Long.valueOf(pedacos[i])));
							} else {
								novoDado = new DadoImp(table.getColunas().get(i), pedacos[i].getBytes());
							}
							dados.add(novoDado);
						}
						arrayTuplas.add(dados);
						// prepara a pr�xima linha
						line = reader.readLine();
					}
					// fecha os leitores
					reader.close();
					fis.close();

					return new TabelaConsultaImp(arrayTuplas);

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {// tabela n�o existe
				throw new TabelaInvalidaException(
						"A tabela " + nomeTabela + " n�o existe no banco de dados " + nomeBanco + ".");
			}
		} else {// se o banco de dados n�o existe
			throw new BancoInvalidoException(nomeBanco + " n�o existe em " + path + ".");
		}
		return null;
	}

	public Tabela getTabela(String nomeBanco, String nomeTabela) throws IOException {
		ArrayList<Coluna> colunas = new ArrayList<Coluna>();
		File tableDirectory = new File(path + nomeBanco + File.separator + nomeTabela);
		// necess�rios para ler o arquivo
		try {
			FileInputStream fis = new FileInputStream(tableDirectory);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			// quebra a linha 0 da tabela, a qual cont�m o cabe�alho
			String[] tableHead = reader.readLine().split(separator);
			for (int i = 0; i < tableHead.length; i = i + 2) {
				Coluna col = new ColunaImp(nomeBanco, nomeTabela, tableHead[i], Integer.valueOf(tableHead[i + 1]));
				colunas.add(col);
			}
			// fecha os leitores
			reader.close();
			fis.close();
			return new TabelaImp(nomeTabela, colunas);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("A tabela n�o existe.");
		}
	}

}
