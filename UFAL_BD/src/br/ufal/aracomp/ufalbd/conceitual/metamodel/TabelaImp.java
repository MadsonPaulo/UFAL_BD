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

	TabelaImp(String name, List<Coluna> rows) {
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

}
