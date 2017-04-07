package br.ufal.aracomp.ufalbd.conceitual.metamodel;

public class CondicaoSimples extends Condicao{
	
	protected Coluna coluna;
	protected int operadorComparacao;
	protected Object valorColuna;
	
	public CondicaoSimples(Coluna coluna, int operadorComparacao, Object valorColuna) {
		this.coluna = coluna;
		this.operadorComparacao = operadorComparacao;
		this.valorColuna = valorColuna;
	}
	
	

	public Coluna getColuna() {
		return coluna;
	}
	
	public int getOperador() {
		return operadorComparacao;
	}
	
	public Object getValorColuna() {
		return valorColuna;
	}
}
