package br.ufal.aracomp.ufalbd.interno.exception;

public class ErroAcessoDadosException extends Exception {
	private static final long serialVersionUID = 1433452963569995561L;

	public ErroAcessoDadosException(String message) {
		super(message);
	}
	
	public ErroAcessoDadosException(String message, Throwable cause) {
		super(message, cause);
	}
}
