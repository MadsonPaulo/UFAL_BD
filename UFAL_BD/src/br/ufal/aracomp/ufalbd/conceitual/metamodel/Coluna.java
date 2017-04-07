package br.ufal.aracomp.ufalbd.conceitual.metamodel;

public interface Coluna {
	public static int TEXTO = 1; // sugestao: texto simples
	public static int NUMERO = 2; // sugestao: numero simples
	public static int DATA_HORA = 3; // sugestao:armazenar o tempo em milisegundos
	public static int BYTES = 4; // sugestao: armazenar em um arquivo separado
	//... FIQUE A VONTADE PARA ADICIONAR NOVOS TIPOS
	
	
	
	public String obterNomeBanco();
	public String obterNomeTabela();
	public String obterNomeColuna();
	public int getTipoColuna();
}
