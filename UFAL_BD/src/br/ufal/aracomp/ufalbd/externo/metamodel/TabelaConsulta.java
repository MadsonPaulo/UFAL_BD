package br.ufal.aracomp.ufalbd.externo.metamodel;

public interface TabelaConsulta {

	public String obterNomeColuna(int ordemColuna);
	public String obterDadoColuna(int ordemColuna, int ordemTupla);
	public String obterDadoColuna(String nomeColuna, int ordemTupla);
	public int obterNumeroTuplas();
	public int obterNumeroColulas();
}
