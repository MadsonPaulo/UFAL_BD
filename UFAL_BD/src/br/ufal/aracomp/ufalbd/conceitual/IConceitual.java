package br.ufal.aracomp.ufalbd.conceitual;

import java.util.List;

import br.ufal.aracomp.ufalbd.conceitual.exception.BancoInvalidoException;
import br.ufal.aracomp.ufalbd.conceitual.exception.ColunaInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.exception.CondicaoInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.exception.ErroInsercaoException;
import br.ufal.aracomp.ufalbd.conceitual.exception.TabelaInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Coluna;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Condicao;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Dado;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Tabela;
import br.ufal.aracomp.ufalbd.externo.metamodel.TabelaConsulta;

/**
 * REPRESENTA A CAMADA CONCEITUAL DO SGBD - RESPONSABILIDADE DE RELACIONAR AS TABELAS DO BANCO DE DADOS E PROCESSAR CONSULTAS"
 * @author patrick
 */
public interface IConceitual {
	//DDL
	
	/**
	 * Cria banco de dados nomeBanco
	 * RESPONSABILIDADE DO MODULO INTERNO. DEVE REPASSAR PARA ELE
	 * @param nomeBanco
	 * @return true, se bem sucedida e false caso contrário
	 * @throws BancoInvalidoException laçada se o banco já existir ou se o nome for inválido
	 */
	public boolean criarBanco(String nomeBanco) throws BancoInvalidoException;
	
	/**
	 * Apaga banco de dados nomeBanco
	 * RESPONSABILIDADE DO MODULO INTERNO. DEVE REPASSAR PARA ELE
	 * @param nomeBanco
	 * @return true, se bem sucedida e false caso contrário
	 * @throws BancoInvalidoException laçada se o banco não existir ou se o nome for inválido
	 */
	public boolean apagarBanco(String nomeBanco) throws BancoInvalidoException;
	
	/**
	 * Cria tabela no banco de dados nomeBanco
	 * RESPONSABILIDADE DO MODULO INTERNO. DEVE REPASSAR PARA ELE
	 * @param nomeBanco
	 * @param descricaoTabela
	 * @return true, se bem sucedida e false caso contrário
	 * @throws BancoInvalidoException laçada se o banco não existir
	 * @throws TabelaInvalidaException lançada se a tabela já existir no banco ou se for inválido
	 */
	public boolean criarTabela(String nomeBanco, Tabela descricaoTabela) throws BancoInvalidoException, TabelaInvalidaException;
	
	/**
	 * Apaga tabela nomeTabela do banco de dados nomeBanco
	 * RESPONSABILIDADE DO MODULO INTERNO. DEVE REPASSAR PARA ELE
	 * @param nomeBanco
	 * @param nomeTabela
	 * @return true, se bem sucedida e false caso contrário
	 * @throws BancoInvalidoException laçada se o banco não existir
	 * @throws TabelaInvalidaException lançada se a tabela não existir
	 */
	public boolean apagarTabela(String nomeBanco, String nomeTabela) throws BancoInvalidoException, TabelaInvalidaException;
	
	//DML
	/**
	 * Inserir tupla na tabela nomeTabela do babco de dados nomeBanco
	 * RESPONSABILIDADE DO MODULO INTERNO. DEVE REPASSAR PARA ELE
	 * @param nomeBanco
	 * @param nomeTabela
	 * @param dados
	 * @return true, se bem sucedida e false caso contrário
	 * @throws BancoInvalidoException laçada se o banco não existir
	 * @throws TabelaInvalidaException lançada se a tabela não existir
	 * @throws ErroInsercaoException erro decorrente de algum outro problema, como violacao de chave primaria ou impossibilidade de inserir
	 */
	public boolean inserirTupla(String nomeBanco, String nomeTabela, List<Dado> dados) throws BancoInvalidoException, TabelaInvalidaException, ErroInsercaoException;

	/**
	 * Excluir tupla na tabela nomeTabela do babco de dados nomeBanco
	 * A INTERPERTAÇÃO DA CONSULTA consulta É RESPONSABILIDADE DO MODULO CONCEITUAL. MAS A RESPONSABILIDADE DA EXCLUSÃO EM SI É DO MÓDULO INTERNO
	 * @param nomeBanco
	 * @param nomeTabela
	 * @param condicao
	 * @return true, se bem sucedida e false caso contrário
	 * @throws BancoInvalidoException laçada se o banco não existir
	 * @throws TabelaInvalidaException lançada se a tabela não existir
	 * @throws CondicaoInvalidaException lançada se a condição especificada estiver inválida para tabela nomeTabela
	 */
	public boolean excluirTupla(String nomeBanco, String nomeTabela, Condicao condicao) throws BancoInvalidoException, TabelaInvalidaException, CondicaoInvalidaException;

	/**
	 * Executa consulta no banco de dados
	 * RESPONSABILIDADE DO MODULO CONCEITUAL 
	 * @param nomeBanco
	 * @param nomesTabelas
	 * @param colunas
	 * @param condicao
	 * @return Tabela contendo resultado da consulta (esquema e dados) 
	 * @throws BancoInvalidoException laçada se o banco não existir
	 * @throws TabelaInvalidaException lançada se a tabela não existir
	 * @throws ColunaInvalidaException lançada se a coluna a ser projetada for inválida para as tabelas envolvidas na consulta (nomesTabelas)
	 * @throws CondicaoInvalidaException lançada se a condição especificada estiver inválida para as tabelas envolvidas na consulta (nomesTabelas)
	 */
	public TabelaConsulta consultar(String nomeBanco, List<String> nomesTabelas, List<Coluna> colunas, Condicao condicao) throws BancoInvalidoException, TabelaInvalidaException, ColunaInvalidaException, CondicaoInvalidaException;
}
