package br.ufal.aracomp.ufalbd.externo.metamodel;

import java.util.ArrayList;

import br.ufal.aracomp.ufalbd.conceitual.metamodel.Coluna;

public class TabelaConsultaImp implements TabelaConsulta {
	private ArrayList<Coluna> columns;

	@Override
	public String obterNomeColuna(int ordemColuna) {
		return columns.get(ordemColuna).obterNomeColuna();
	}

	@Override
	public String obterDadoColuna(int ordemColuna, int ordemTupla) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String obterDadoColuna(String nomeColuna, int ordemTupla) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int obterNumeroTuplas() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int obterNumeroColulas() {
		return columns.size();
	}

}
