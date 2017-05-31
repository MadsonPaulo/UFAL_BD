package br.ufal.aracomp.ufalbd.conceitual;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufal.aracomp.ufalbd.conceitual.exception.BancoInvalidoException;
import br.ufal.aracomp.ufalbd.conceitual.exception.ColunaInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.exception.CondicaoInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.exception.ErroInsercaoException;
import br.ufal.aracomp.ufalbd.conceitual.exception.TabelaInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Coluna;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Condicao;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.CondicaoComposta;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.CondicaoSimples;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Dado;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Tabela;
import br.ufal.aracomp.ufalbd.externo.metamodel.TabelaConsulta;
import br.ufal.aracomp.ufalbd.externo.metamodel.TabelaConsultaImp;
import br.ufal.aracomp.ufalbd.interno.Interno;
import br.ufal.aracomp.ufalbd.interno.exception.ErroAcessoDadosException;

public class Conceitual implements IConceitual {
	Interno interno = new Interno();

	@Override
	public boolean criarBanco(String nomeBanco) throws BancoInvalidoException {
		try {
			return interno.criarBanco(nomeBanco);
		} catch (ErroAcessoDadosException | BancoInvalidoException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean apagarBanco(String nomeBanco) throws BancoInvalidoException {
		try {
			return interno.apagarBanco(nomeBanco);
		} catch (ErroAcessoDadosException | BancoInvalidoException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean criarTabela(String nomeBanco, Tabela descricaoTabela)
			throws BancoInvalidoException, TabelaInvalidaException {
		try {
			return interno.criarTabela(nomeBanco, descricaoTabela);
		} catch (ErroAcessoDadosException | BancoInvalidoException | TabelaInvalidaException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean apagarTabela(String nomeBanco, String nomeTabela)
			throws BancoInvalidoException, TabelaInvalidaException {
		try {
			return interno.apagarTabela(nomeBanco, nomeTabela);
		} catch (ErroAcessoDadosException | BancoInvalidoException | TabelaInvalidaException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean inserirTupla(String nomeBanco, String nomeTabela, List<Dado> dados)
			throws BancoInvalidoException, TabelaInvalidaException, ErroInsercaoException {
		try {
			return interno.inserirTupla(nomeBanco, nomeTabela, dados);
		} catch (ErroAcessoDadosException | BancoInvalidoException | TabelaInvalidaException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean excluirTupla(String nomeBanco, String nomeTabela, Condicao condicao)
			throws BancoInvalidoException, TabelaInvalidaException, CondicaoInvalidaException {
		try {
			// se alguma tupla foi excluida
			boolean houveramTuplasExcluidas = false;
			// obt�m a tabela
			TabelaConsultaImp tabela = (TabelaConsultaImp) interno.obterTuplas(nomeBanco, nomeTabela);
			// todos os dados da tabela
			ArrayList<ArrayList<Dado>> dadosTabela = tabela.getTuplas();

			if (condicao instanceof CondicaoComposta) {// condi��o composta
				CondicaoComposta condicaoComposta = (CondicaoComposta) condicao;
				CondicaoSimples cond1 = (CondicaoSimples) condicaoComposta.getCondicao1();
				CondicaoSimples cond2 = (CondicaoSimples) condicaoComposta.getCondicao2();

				// checa se o argumento da primeira condi��o � v�lido
				if (cond1.getColuna().getTipoColuna() == Coluna.NUMERO) {
					Double.valueOf(cond1.getValorColuna().toString());
				} else if (cond1.getColuna().getTipoColuna() == Coluna.DATA_HORA) {
					DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
					format.parse(cond1.getValorColuna().toString());
				}
				// checa se o argumento da segunda condi��o � v�lido
				if (cond2.getColuna().getTipoColuna() == Coluna.NUMERO) {
					Double.valueOf(cond2.getValorColuna().toString());
				} else if (cond2.getColuna().getTipoColuna() == Coluna.DATA_HORA) {
					DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
					format.parse(cond2.getValorColuna().toString());
				}
				// identifica o �ndice da coluna das duas condi��es
				int index1 = tabela.obterIndiceColuna(cond1.getColuna().obterNomeColuna());
				int index2 = tabela.obterIndiceColuna(cond2.getColuna().obterNomeColuna());
				// operador l�gico da condi��o composta
				int operadorLogico = condicaoComposta.getOperadorLogico();

				// apaga as tuplas que satisfazem a condi��o composta
				for (int i = dadosTabela.size() - 1; i >= 0; i--) {
					// a linha 0 do arquivo cont�m o cabe�allho da tabela.
					// dadosTabela n�o possui esse cabe�alho, por isso tamanho -
					// 1 precisa ser compensado com +1
					if (operadorLogico == CondicaoComposta.AND) {
						if ((satisfazCondicao(i, index1, cond1, dadosTabela)
								&& satisfazCondicao(i, index2, cond2, dadosTabela))) {
							interno.excluirTupla(nomeBanco, nomeTabela, i + 1);
							houveramTuplasExcluidas = true;
						}
					} else if (operadorLogico == CondicaoComposta.OR) {
						if ((satisfazCondicao(i, index1, cond1, dadosTabela)
								|| satisfazCondicao(i, index2, cond2, dadosTabela))) {
							interno.excluirTupla(nomeBanco, nomeTabela, i + 1);
							houveramTuplasExcluidas = true;
						}
					}
				} // fim do for
			} else {// condi��o simples
				CondicaoSimples condicaoSimples = (CondicaoSimples) condicao;

				// checa se o argumento da condi��o � v�lido
				if (condicaoSimples.getColuna().getTipoColuna() == Coluna.NUMERO) {
					Double.valueOf(condicaoSimples.getValorColuna().toString());
				} else if (condicaoSimples.getColuna().getTipoColuna() == Coluna.DATA_HORA) {
					DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
					format.parse(condicaoSimples.getValorColuna().toString());
				}
				// identifica o �ndice da coluna da condi��o
				int index = tabela.obterIndiceColuna(condicaoSimples.getColuna().obterNomeColuna());

				// apaga as tuplas que satisfazem a condi��o
				for (int i = dadosTabela.size() - 1; i >= 0; i--) {

					if (satisfazCondicao(i, index, condicaoSimples, dadosTabela)) {
						// a linha 0 do arquivo cont�m o cabe�allho da tabela.
						// dadosTabela n�o possui esse cabe�alho, por isso
						// tamanho -
						// 1 precisa ser compensado com +1
						interno.excluirTupla(nomeBanco, nomeTabela, i + 1);
						houveramTuplasExcluidas = true;
					}

				} // fim do for

			} // fim condi��o simples

			return houveramTuplasExcluidas;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public TabelaConsulta consultar(String nomeBanco, List<String> nomesTabelas, List<Coluna> colunas,
			Condicao condicao)
			throws BancoInvalidoException, TabelaInvalidaException, ColunaInvalidaException, CondicaoInvalidaException {
		try {
			// obt�m a tabela
			TabelaConsultaImp tabela = (TabelaConsultaImp) interno.obterTuplas(nomeBanco, nomesTabelas.get(0));
			// todos os dados da tabela
			ArrayList<ArrayList<Dado>> dadosTabela = tabela.getTuplas();

			if (condicao instanceof CondicaoComposta) {// condi��o composta
				CondicaoComposta condicaoComposta = (CondicaoComposta) condicao;
				CondicaoSimples cond1 = (CondicaoSimples) condicaoComposta.getCondicao1();
				CondicaoSimples cond2 = (CondicaoSimples) condicaoComposta.getCondicao2();

				// checa se o argumento da primeira condi��o � v�lido
				if (cond1.getColuna().getTipoColuna() == Coluna.NUMERO) {
					Double.valueOf(cond1.getValorColuna().toString());
				} else if (cond1.getColuna().getTipoColuna() == Coluna.DATA_HORA) {
					DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
					format.parse(cond1.getValorColuna().toString());
				}
				// checa se o argumento da segunda condi��o � v�lido
				if (cond2.getColuna().getTipoColuna() == Coluna.NUMERO) {
					Double.valueOf(cond2.getValorColuna().toString());
				} else if (cond2.getColuna().getTipoColuna() == Coluna.DATA_HORA) {
					DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
					format.parse(cond2.getValorColuna().toString());
				}
				// identifica o �ndice da coluna das duas condi��es
				int index1 = tabela.obterIndiceColuna(cond1.getColuna().obterNomeColuna());
				int index2 = tabela.obterIndiceColuna(cond2.getColuna().obterNomeColuna());
				// operador l�gico da condi��o composta
				int operadorLogico = condicaoComposta.getOperadorLogico();

				// remove as tuplas que n�o satisfazem a condi��o
				for (int i = dadosTabela.size() - 1; i >= 0; i--) {

					if (operadorLogico == CondicaoComposta.AND) {
						if ((satisfazCondicao(i, index1, cond1, dadosTabela)
								&& satisfazCondicao(i, index2, cond2, dadosTabela)) == false) {
							dadosTabela.remove(i);
						}
					} else if (operadorLogico == CondicaoComposta.OR) {
						if ((satisfazCondicao(i, index1, cond1, dadosTabela)
								|| satisfazCondicao(i, index2, cond2, dadosTabela)) == false) {
							dadosTabela.remove(i);
						}
					}

				}

				// agora dadosTabela cont�m apenas as tuplas que satisfazem a
				// condi��o composta
				return new TabelaConsultaImp(dadosTabela);

			} else {// condi��o simples
				CondicaoSimples condicaoSimples = (CondicaoSimples) condicao;

				// checa se o argumento da condi��o � v�lido
				if (condicaoSimples.getColuna().getTipoColuna() == Coluna.NUMERO) {
					Double.valueOf(condicaoSimples.getValorColuna().toString());
				} else if (condicaoSimples.getColuna().getTipoColuna() == Coluna.DATA_HORA) {
					DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
					format.parse(condicaoSimples.getValorColuna().toString());
				}
				// identifica o �ndice da coluna da condi��o
				int index = tabela.obterIndiceColuna(condicaoSimples.getColuna().obterNomeColuna());

				// remove as tuplas que n�o satisfazem a condi��o
				for (int i = dadosTabela.size() - 1; i >= 0; i--) {

					if (satisfazCondicao(i, index, condicaoSimples, dadosTabela) == false) {
						dadosTabela.remove(i);
					}

				}

				// agora dadosTabela cont�m apenas as tuplas que satisfazem a
				// condi��o
				return new TabelaConsultaImp(dadosTabela);
			} // fim condi��o simples

		} catch (ErroAcessoDadosException e) {
			throw new TabelaInvalidaException("O banco de dados e/ou tabela informado n�o existe.");
		} catch (ParseException e) {
			throw new CondicaoInvalidaException("A condi��o � inv�lida.");
		}
	}

	public TabelaConsulta consultar(String nomeBanco, String nomeTabela, List<Coluna> colunas)
			throws BancoInvalidoException, TabelaInvalidaException, ColunaInvalidaException, CondicaoInvalidaException {
		try {
			TabelaConsultaImp tabela = (TabelaConsultaImp) interno.obterTuplas(nomeBanco, nomeTabela);
			// todos os dados da tabela
			ArrayList<ArrayList<Dado>> dadosTabela = tabela.getTuplas();

			// identifica o indice das colunas desejadas
			ArrayList<Integer> indicesColunas = new ArrayList<Integer>();
			for (Coluna col : colunas) {
				for (int i = 0; i < tabela.obterNumeroColulas(); i++) {
					if (dadosTabela.get(0).get(i).obterColuna().obterNomeColuna().equals(col.obterNomeColuna())) {
						indicesColunas.add(i);
					}
				}
			}

			// remove as colunas que n�o forem as desejadas
			for (ArrayList<Dado> dado : dadosTabela) {
				for (int i = (dado.size() - 1); i >= 0; i--) {
					if (indicesColunas.contains(i) == false) {
						dado.remove(i);
					}
				}
			}

			// agora, 'dadosTabela' possui apenas as colunas desejadas
			return new TabelaConsultaImp(dadosTabela);
		} catch (ErroAcessoDadosException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Author: Madson
	 * 
	 * Verifica se uma determinada tupla satisfaz uma condi��o simples
	 * 
	 * @param indiceTupla
	 *            �ndice da tupla a ser analisada
	 * @param indiceColunaCondicao
	 *            �ndice da coluna referente � condi��o
	 * @param condicaoSimples
	 *            condi��o simples
	 * @param dadosTabela
	 *            todos os dados da tabela em quest�o
	 * @return true caso a condi��o seja satisfeita, false caso contr�rio
	 * @throws ParseException
	 */
	private boolean satisfazCondicao(int indiceTupla, int indiceColunaCondicao, CondicaoSimples condicaoSimples,
			ArrayList<ArrayList<Dado>> dadosTabela) throws ParseException {
		int tipoColuna = condicaoSimples.getColuna().getTipoColuna();
		int operador = condicaoSimples.getOperador();

		if (tipoColuna == Coluna.NUMERO) {
			double valorTupla = Double
					.valueOf(dadosTabela.get(indiceTupla).get(indiceColunaCondicao).obterValorDado().toString());
			double valorCond = Double.valueOf(condicaoSimples.getValorColuna().toString());

			if (operador == 1) {// =
				if ((valorTupla == valorCond) == false) {
					return false;
				}
			} else if (operador == 2) {// !=
				if ((valorTupla != valorCond) == false) {
					return false;
				}
			} else if (operador == 3) {// <
				if ((valorTupla < valorCond) == false) {
					return false;
				}
			} else if (operador == 4) {// <=
				if ((valorTupla <= valorCond) == false) {
					return false;
				}
			} else if (operador == 5) {// >
				if ((valorTupla > valorCond) == false) {
					return false;
				}
			} else if (operador == 6) {// >=
				if ((valorTupla >= valorCond) == false) {
					return false;
				}
			}
		} else if (tipoColuna == Coluna.DATA_HORA) {
			DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

			long valorTupla = ((Date) dadosTabela.get(indiceTupla).get(indiceColunaCondicao).obterValorDado())
					.getTime();
			long valorCond = format.parse(condicaoSimples.getValorColuna().toString()).getTime();

			// remove os valores que n�o suprem a condi��o
			if (operador == 1) {// =
				if ((valorTupla == valorCond) == false) {
					return false;
				}
			} else if (operador == 2) {// !=
				if ((valorTupla != valorCond) == false) {
					return false;
				}
			} else if (operador == 3) {// <
				if ((valorTupla < valorCond) == false) {
					return false;
				}
			} else if (operador == 4) {// <=
				if ((valorTupla <= valorCond) == false) {
					return false;
				}
			} else if (operador == 5) {// >
				if ((valorTupla > valorCond) == false) {
					return false;
				}
			} else if (operador == 6) {// >=
				if ((valorTupla >= valorCond) == false) {
					return false;
				}
			}
		} else {// TEXTO OU BYTES
			String valorTuplaString = dadosTabela.get(indiceTupla).get(indiceColunaCondicao).obterValorDado()
					.toString();
			String valorCondString = condicaoSimples.getValorColuna().toString();
			int valorTupla = valorTuplaString.length();
			int valorCond = valorCondString.length();

			if (operador == 1) {// =
				if ((valorTuplaString.equals(valorCondString)) == false) {
					return false;
				}
			} else if (operador == 2) {// !=
				if ((valorTuplaString.equals(valorCondString)) == true) {
					return false;
				}
			} else if (operador == 3) {// <
				if ((valorTupla < valorCond) == false) {
					return false;
				}
			} else if (operador == 4) {// <=
				if ((valorTupla <= valorCond) == false) {
					return false;
				}
			} else if (operador == 5) {// >
				if ((valorTupla > valorCond) == false) {
					return false;
				}
			} else if (operador == 6) {// >=
				if ((valorTupla >= valorCond) == false) {
					return false;
				}
			}
		}
		return true;
	}
}
