package br.ufal.aracomp.ufalbd.externo.exception;

public class InstrucaoMalFormadaException extends Exception {
	private static final long serialVersionUID = 3114632635059800527L;

	
	public InstrucaoMalFormadaException(String message) {
		super(message);
	}
	
	public InstrucaoMalFormadaException(String message, Throwable cause) {
		super(message, cause);
	}
}
