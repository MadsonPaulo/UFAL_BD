package br.ufal.aracomp.ufalbd.externo.metamodel;

import java.util.ArrayList;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Dado;

/**
 * Tabela de Consulta
 * 
 * @author Madson
 *
 */
public class TabelaConsultaImp implements TabelaConsulta {
	private ArrayList<ArrayList<Dado>> arrayTuplas;

	public TabelaConsultaImp(ArrayList<ArrayList<Dado>> arrayTuplas) {
		this.arrayTuplas = new ArrayList<ArrayList<Dado>>();
		this.arrayTuplas = arrayTuplas;
	}

	@Override
	public String obterNomeColuna(int ordemColuna) {
		return arrayTuplas.get(0).get(ordemColuna).obterColuna().obterNomeColuna();
	}

	@Override
	public String obterDadoColuna(int ordemColuna, int ordemTupla) {
		return arrayTuplas.get(ordemTupla).get(ordemColuna).obterValorDado().toString();
	}

	@Override
	public String obterDadoColuna(String nomeColuna, int ordemTupla) {
		for (int i = 0; i < arrayTuplas.get(ordemTupla).size(); i++) {
			if (arrayTuplas.get(ordemTupla).get(i).obterColuna().obterNomeColuna().equals(nomeColuna)) {
				return arrayTuplas.get(ordemTupla).get(i).obterValorDado().toString();
			}
		}
		return null;
	}

	@Override
	public int obterNumeroTuplas() {
		return arrayTuplas.size();
	}

	@Override
	public int obterNumeroColulas() {
		return arrayTuplas.get(0).size();
	}

	public ArrayList<ArrayList<Dado>> getTuplas() {
		return arrayTuplas;
	}

	@Override
	public int obterIndiceColuna(String nomeColuna) {
		for (int i = 0; i < arrayTuplas.get(0).size(); i++) {
			if (arrayTuplas.get(0).get(i).obterColuna().obterNomeColuna().equals(nomeColuna)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void imprimirTuplas() {
		String sepatator = ", ";
		for (ArrayList<Dado> tupla : arrayTuplas) {
			String data = "";
			for (int i = 0; i < tupla.size(); i++) {
				data += tupla.get(i).obterValorDado().toString();
				if (i < tupla.size() - 1) {
					data += sepatator;
				}
			}
			System.out.println(data);
		}
	}

	@Override
	public void imprimirTuplascomNomeColuna() {
		String sepatator = ", ";
		String colNameSeparator = "= ";
		for (ArrayList<Dado> tupla : arrayTuplas) {
			String data = "";
			for (int i = 0; i < tupla.size(); i++) {
				data += tupla.get(i).obterColuna().obterNomeColuna() + colNameSeparator
						+ tupla.get(i).obterValorDado().toString();
				if (i < tupla.size() - 1) {
					data += sepatator;
				}
			}
			System.out.println(data);
		}
	}

}
