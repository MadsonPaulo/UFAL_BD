package br.ufal.aracomp.ufalbd.conceitual.exception;

public class BancoInvalidoException extends Exception {
	private static final long serialVersionUID = -3677703984597247175L;

	public BancoInvalidoException(String message) {
		super(message);
	}
	
	public BancoInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}
}
