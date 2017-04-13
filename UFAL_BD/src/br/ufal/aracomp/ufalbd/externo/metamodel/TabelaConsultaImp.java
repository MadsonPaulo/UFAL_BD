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

}
