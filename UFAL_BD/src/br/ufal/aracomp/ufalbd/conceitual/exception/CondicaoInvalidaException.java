package br.ufal.aracomp.ufalbd.conceitual.exception;

public class CondicaoInvalidaException extends Exception {
	private static final long serialVersionUID = -3135646094598179100L;
	
	public CondicaoInvalidaException(String message) {
		super(message);
	}
	
	public CondicaoInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}
}
