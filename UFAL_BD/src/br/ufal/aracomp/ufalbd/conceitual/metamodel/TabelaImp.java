package br.ufal.aracomp.ufalbd.conceitual.metamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação de uma Tabela
 * 
 * @author Madson
 *
 */
public class TabelaImp implements Tabela {
	private String tableName;
	ArrayList<Coluna> rows;

	public TabelaImp(String name, List<Coluna> rows) {
		this.tableName = name;
		this.rows = new ArrayList<>(rows);
	}

	@Override
	public String getNome() {
		return tableName;
	}

	@Override
	public void definirColunas(List<Coluna> colunas) {
		this.rows.clear();
		this.rows.addAll(colunas);
	}

	@Override
	public List<Coluna> getColunas() {
		return rows;
	}

	@Override
	public int obterIndiceColuna(String nomeColuna) {
		for (int i = 0; i < rows.size(); i++) {
			if (rows.get(i).obterNomeColuna().equals(nomeColuna)) {
				return i;
			}
		}
		return -1;
	}

}
