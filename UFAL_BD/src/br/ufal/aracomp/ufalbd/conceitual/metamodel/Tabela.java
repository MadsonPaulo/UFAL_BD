package br.ufal.aracomp.ufalbd.conceitual.metamodel;

import java.util.List;

public interface Tabela {
	
	public String getNome();
	public void definirColunas(List<Coluna> colunas);
	public List<Coluna> getColunas();
	
}
