package br.ufal.aracomp.ufalbd.conceitual.exception;

public class ColunaInvalidaException extends Exception {
	private static final long serialVersionUID = -2634436716503773684L;
	
	public ColunaInvalidaException(String message) {
		super(message);
	}
	
	public ColunaInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}
}
