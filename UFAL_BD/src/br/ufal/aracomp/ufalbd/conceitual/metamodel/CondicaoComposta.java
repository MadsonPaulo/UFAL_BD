package br.ufal.aracomp.ufalbd.conceitual.metamodel;

/**
 * FORA DO ESCOPO - EXTRA
 * @author patrick
 */
public class CondicaoComposta extends Condicao{
	public static int AND=1;
	public static int OR=2;
	
	private Condicao condicao1;
	private Condicao condicao2;
	private int operadorLogico;
	
	public CondicaoComposta(Condicao condicao1, Condicao condicao2, int operadorLogico) {
		this.condicao1 = condicao1;
		this.condicao2 = condicao2;
		this.operadorLogico = operadorLogico;
	}
	
	public int getOperadorLogico() {
		return operadorLogico;
	}
	
	public Condicao getCondicao1() {
		return condicao1;
	}
	
	public Condicao getCondicao2() {
		return condicao2;
	}
}
