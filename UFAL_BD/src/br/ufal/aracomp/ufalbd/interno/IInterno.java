package br.ufal.aracomp.ufalbd.interno;

import java.util.List;

import br.ufal.aracomp.ufalbd.conceitual.exception.BancoInvalidoException;
import br.ufal.aracomp.ufalbd.conceitual.exception.TabelaInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Dado;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Tabela;
import br.ufal.aracomp.ufalbd.externo.metamodel.TabelaConsulta;
import br.ufal.aracomp.ufalbd.interno.exception.ErroAcessoDadosException;

/**
 * REPRESENTA A CAMADA INTERNA DO SGBD - RESPONSABILIDADE DE ARMAZENAR E RECUPERAR OS DADOS NO DISCO
 * @author patrick
 */
public interface IInterno {
	//DDL
	/**
	 * Criar banco de dados nomeBanco
	 * @param nomeBanco
	 * @return true, se bem sucedida e false, caso contrario
	 * @throws BancoInvalidoException lançada se o banco já existir ou for inválido
	 * @throws ErroAcessoDadosException lançada se não for possível atualizar os dados
	 */
	public boolean criarBanco(String nomeBanco) throws BancoInvalidoException, ErroAcessoDadosException;
	
	/**
	 * Excuir banco de dados nomeBanco
	 * @param nomeBanco
	 * @return true, se bem sucedida e false, caso contrario
	 * @throws BancoInvalidoException lançada se o banco não existir ou for inválido
	 * @throws ErroAcessoDadosException lançada se não for possível atualizar os dados
	 */
	public boolean apagarBanco(String nomeBanco) throws BancoInvalidoException, ErroAcessoDadosException;
	
	/**
	 * Criar tabela descricaoTabela no banco de dados nomeBanco
	 * @param nomeBanco
	 * @param descricaoTabela
	 * @return true, se bem sucedida e false, caso contrario
	 * @throws BancoInvalidoException lançada se o banco não existir
	 * @throws TabelaInvalidaException lançada se o banco já existir ou for inválida
	 * @throws ErroAcessoDadosException lançada se não for possível atualizar os dados
	 */
	public boolean criarTabela(String nomeBanco, Tabela descricaoTabela) throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException;
	
	/**
	 * Excluir tabela descricaoTabela no banco de dados nomeBanco
	 * @param nomeBanco
	 * @param descricaoTabela
	 * @return true, se bem sucedida e false, caso contrario
	 * @throws BancoInvalidoException lançada se o banco não existir
	 * @throws TabelaInvalidaException lançada se o banco não existir ou for inválida
	 * @throws ErroAcessoDadosException lançada se não for possível atualizar os dados
	 */
	public boolean apagarTabela(String nomeBanco, String nomeTabela) throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException;
	
	
	//DML
	/**
	 * Inserir tupla com dados dados na tabela nomeTabela no banco de dados nomeBanco
	 * @param nomeBanco
	 * @param nomeTabela
	 * @param dados
	 * @return true, se bem sucedida e false, caso contrario
	 * @throws BancoInvalidoException lançada se o banco não existir
	 * @throws TabelaInvalidaException lançada se o banco não existir ou for inválida
	 * @throws ErroAcessoDadosException lançada se não for possível atualizar os dados
	 */
	public boolean inserirTupla(String nomeBanco, String nomeTabela, List<Dado> dados) throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException;
	
	/**
	 * Excluir tupla de ordem ordem da tabela nomeTabela no banco de dados nomeBanco
	 * @param nomeBanco
	 * @param nomeTabela
	 * @param ordem
	 * @return true, se bem sucedida e false, caso contrario
	 * @throws BancoInvalidoException lançada se o banco não existir
	 * @throws TabelaInvalidaException lançada se o banco não existir ou for inválida
	 * @throws ErroAcessoDadosException lançada se não for possível atualizar os dados
	 */
	public boolean excluirTupla(String nomeBanco, String nomeTabela, int ordem) throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException;
	
	/**
	 * Consultar dados da tabela nomeTabela no banco de dados nomeBanco
	 * @param nomeBanco
	 * @param nomeTabela
	 * @return true, se bem sucedida e false, caso contrario
	 * @throws BancoInvalidoException lançada se o banco não existir
	 * @throws TabelaInvalidaException lançada se o banco não existir ou for inválida
	 * @throws ErroAcessoDadosException lançada se não for possível acessar os dados
	 */
	public TabelaConsulta obterTuplas(String nomeBanco, String nomeTabela) throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException;
}