package br.ufal.aracomp.ufalbd.conceitual.exception;

public class ErroInsercaoException extends Exception {
	private static final long serialVersionUID = -2810474772677545595L;
	
	public ErroInsercaoException(String message) {
		super(message);
	}
	
	public ErroInsercaoException(String message, Throwable cause) {
		super(message, cause);
	}
}
