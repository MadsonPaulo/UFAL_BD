package br.ufal.aracomp.ufalbd.conceitual.metamodel;

/**
 * Implementação de uma Coluna
 * 
 * @author Madson
 *
 */
public class ColunaImp implements Coluna {
	private String databaseName;
	private String tableName;
	private String columnName;
	private int columnType;

	public ColunaImp(String databaseName, String tableName, String columnName, int columnType) {
		super();
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.columnName = columnName;
		this.columnType = columnType;
	}

	@Override
	public String obterNomeBanco() {
		return databaseName;
	}

	@Override
	public String obterNomeTabela() {
		return tableName;
	}

	@Override
	public String obterNomeColuna() {
		return columnName;
	}

	@Override
	public int getTipoColuna() {
		return columnType;
	}

}
