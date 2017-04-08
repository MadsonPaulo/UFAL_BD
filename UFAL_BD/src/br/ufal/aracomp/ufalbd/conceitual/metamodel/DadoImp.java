package br.ufal.aracomp.ufalbd.conceitual.metamodel;

/**
 * Implementação de um Dado
 * 
 * @author Madson
 *
 */
public class DadoImp implements Dado {
	private Coluna column;
	private Object value;

	public DadoImp(Coluna column, Object value) {
		this.column = column;
		this.value = value;
	}

	@Override
	public Coluna obterColuna() {
		return column;
	}

	@Override
	public Object obterValorDado() {
		return value;
	}

}
