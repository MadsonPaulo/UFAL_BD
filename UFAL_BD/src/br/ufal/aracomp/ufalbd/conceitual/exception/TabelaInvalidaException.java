package br.ufal.aracomp.ufalbd.conceitual.exception;

public class TabelaInvalidaException extends Exception {
	private static final long serialVersionUID = -2568311193398765569L;

	public TabelaInvalidaException(String message) {
		super(message);
	}
	
	public TabelaInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}
}
