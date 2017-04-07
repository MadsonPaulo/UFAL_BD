package br.ufal.aracomp.ufalbd.externo;

import br.ufal.aracomp.ufalbd.externo.exception.InstrucaoMalFormadaException;
import br.ufal.aracomp.ufalbd.externo.metamodel.TabelaConsulta;

/**
 * REPRESENTA A CAMADA EXTERNA DO SGBD - RESPONSABILIDADE DE INTERPRETAR INSTRÇÕES da "linguagem específica"
 * @author patrick
 *
 */
public interface IExterno {
	/**
	 * Interpreta a "instrucao" e executa a respectiva operacao DDL de IConceitual  
	 * @param instrucao
	 * @return true, se bem sucedido e false, caso contrário
	 * @throws InstrucaoMalFormadaException - lançada caso a instrucao esteja mal formada
	 */
	public boolean executarDDL(String instrucao) throws InstrucaoMalFormadaException;
	
	/**
	 * Interpreta a "instrucao" e executa a respectiva operacao DML de IConceitual  
	 * @param instrucao
	 * @return o número de tuplas afetadas pela instrução 
	 * @throws InstrucaoMalFormadaException - lançada caso a instrucao esteja mal formada
	 */
	public int executarDMLInstrucao(String instrucao) throws InstrucaoMalFormadaException;
	
	/**
	 * Interpreta a "instrucao" e executa a respectiva operacao de conslta de IConceitual  
	 * @param instrucao
	 * @return Tabela com os dados resultantes da consulta - Tipo de dado TabelaConsulta
	 * @throws InstrucaoMalFormadaException - lançada caso a instrucao esteja mal formada
	 */
	public TabelaConsulta executarDMLConsulta(String instrucao) throws InstrucaoMalFormadaException;
}
