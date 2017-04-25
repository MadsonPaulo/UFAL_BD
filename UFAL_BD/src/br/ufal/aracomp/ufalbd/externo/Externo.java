package br.ufal.aracomp.ufalbd.externo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import br.ufal.aracomp.ufalbd.conceitual.Conceitual;
import br.ufal.aracomp.ufalbd.conceitual.exception.BancoInvalidoException;
import br.ufal.aracomp.ufalbd.conceitual.exception.CondicaoInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.exception.ErroInsercaoException;
import br.ufal.aracomp.ufalbd.conceitual.exception.TabelaInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Coluna;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.ColunaImp;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.CondicaoSimples;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Dado;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.DadoImp;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.TabelaImp;
import br.ufal.aracomp.ufalbd.externo.exception.InstrucaoMalFormadaException;
import br.ufal.aracomp.ufalbd.externo.metamodel.TabelaConsulta;

public class Externo implements IExterno{

	Conceitual c = new Conceitual();
	
	public String[] separaComandos(String instrucao){
		String caixaBaixa = instrucao.toLowerCase();
		String[] comandos = caixaBaixa.split(" ");
		return comandos;
	}
	public void procExcluirTupla(String arg1, String arg2, CondicaoSimples arg3) {
		try {
			c.excluirTupla(arg1, arg2, arg3);
		} catch (BancoInvalidoException e) {
			e.printStackTrace();
		} catch (TabelaInvalidaException e) {
			e.printStackTrace();
		} catch (CondicaoInvalidaException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean executarDDL(String instrucao) throws InstrucaoMalFormadaException {
		String[] comandos = separaComandos(instrucao);
		if (comandos.length == 2) {
			switch(comandos[0]){
			case "construir_bd":
				try {
					c.criarBanco(comandos[1]);
				} catch (BancoInvalidoException e) {
					e.printStackTrace();}
			case "destruir_bd":
				try {
					c.apagarBanco(comandos[1]);
				} catch (BancoInvalidoException e) {
					e.printStackTrace();}
			}
			
		} else if (comandos.length == 3) {
			switch(comandos[0]){
			case "construir_tb":
				String nomeTabela = comandos[2].split(Pattern.quote("("))[0];
				String descricao = comandos[2].split(Pattern.quote("("))[1].split(Pattern.quote(")"))[0];
				String[] colunas = descricao.split(",");
				
				List<Coluna> colunaList = new ArrayList<>();
				for (int i = 0; i < colunas.length; i++) {
					if (colunas[i].split(":")[1] == "texto") {
						colunaList.add(new ColunaImp(comandos[1], nomeTabela, colunas[i].split(":")[0], 1));
					} else if (colunas[i].split(":")[1] == "numero") {
						colunaList.add(new ColunaImp(comandos[1], nomeTabela, colunas[i].split(":")[0], 2));
					} else if (colunas[i].split(":")[1] == "data_hora") {
						colunaList.add(new ColunaImp(comandos[1], nomeTabela, colunas[i].split(":")[0], 3));
					} else if (colunas[i].split(":")[1] == "bytes") {
						colunaList.add(new ColunaImp(comandos[1], nomeTabela, colunas[i].split(":")[0], 4));
					}
				}
				TabelaImp tabela = new TabelaImp(nomeTabela, colunaList);
				try {
					c.criarTabela(comandos[1], tabela);
				} catch (BancoInvalidoException e) {
					e.printStackTrace();
				} catch (TabelaInvalidaException e) {
					e.printStackTrace();
				}
			
			case "destruir_tb":
				try {
					c.apagarTabela(comandos[1], comandos[2]);
				} catch (BancoInvalidoException e) {
					e.printStackTrace();
				} catch (TabelaInvalidaException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public int executarDMLInstrucao(String instrucao) throws InstrucaoMalFormadaException {
		String[] comandos = separaComandos(instrucao);
		if (comandos.length == 3) {
			
			String nomeTabela = comandos[2].split(Pattern.quote("("))[0];
			String descricao = comandos[2].split(Pattern.quote("("))[1].split(Pattern.quote(")"))[0];
			String[] colunas = descricao.split(",");
			
			List<Dado> dadoList = new ArrayList<>();
			for (int i = 0; i < colunas.length; i++) {
				if (colunas[i].split(":")[1] == "texto") {
					Coluna col = new ColunaImp(comandos[1], nomeTabela, colunas[i].split(":")[0], 1);
					Dado e = new DadoImp(col, colunas[i].split(":")[2]);
					dadoList.add(e);
				} else if (colunas[i].split(":")[1] == "numero"){
					Coluna col = new ColunaImp(comandos[1], nomeTabela, colunas[i].split(":")[0], 2);
					Dado e = new DadoImp(col, colunas[i].split(":")[2]);
					dadoList.add(e);
				} else if (colunas[i].split(":")[1] == "data_hora"){
					Coluna col = new ColunaImp(comandos[1], nomeTabela, colunas[i].split(":")[0], 3);
					Dado e = new DadoImp(col, colunas[i].split(":")[2]);
					dadoList.add(e);
				} else if (colunas[i].split(":")[1] == "bytes"){
					Coluna col = new ColunaImp(comandos[1], nomeTabela, colunas[i].split(":")[0], 4);
					Dado e = new DadoImp(col, colunas[i].split(":")[2]);
					dadoList.add(e);
				}
			}
			
			switch(comandos[0]) {
			case "adicionar":
				try {
					c.inserirTupla(comandos[1], nomeTabela, dadoList);
				} catch (BancoInvalidoException e) {
					e.printStackTrace();
				} catch (TabelaInvalidaException e) {
					e.printStackTrace();
				} catch (ErroInsercaoException e) {
					e.printStackTrace();
				}
			case "remover":
				if (colunas[0].split(":")[1] == "texto") {
					Coluna col = new ColunaImp(comandos[1], nomeTabela, colunas[0].split(":")[0], 1);
					CondicaoSimples cond = new CondicaoSimples(col, 1, colunas[0].split(":")[2]);
					procExcluirTupla(comandos[1], nomeTabela, cond);

				} else if (colunas[0].split(":")[1] == "numero") {
					Coluna col = new ColunaImp(comandos[1], nomeTabela, colunas[0].split(":")[0], 2);
					CondicaoSimples cond = new CondicaoSimples(col, 1, colunas[0].split(":")[2]);
					procExcluirTupla(comandos[1], nomeTabela, cond);

				} else if (colunas[0].split(":")[1] == "data_hora") {
					Coluna col = new ColunaImp(comandos[1], nomeTabela, colunas[0].split(":")[0], 3);
					CondicaoSimples cond = new CondicaoSimples(col, 1, colunas[0].split(":")[2]);
					procExcluirTupla(comandos[1], nomeTabela, cond);

				} else if (colunas[0].split(":")[1] == "bytes") {
					Coluna col = new ColunaImp(comandos[1], nomeTabela, colunas[0].split(":")[0], 4);
					CondicaoSimples cond = new CondicaoSimples(col, 1, colunas[0].split(":")[2]);
					procExcluirTupla(comandos[1], nomeTabela, cond);
				}}
		}
		return 0;
	}

	@Override
	public TabelaConsulta executarDMLConsulta(String instrucao) throws InstrucaoMalFormadaException {
		String[] comandos = separaComandos(instrucao);
		if (comandos.length == 5) {
			
		}
		return null;
	}
}