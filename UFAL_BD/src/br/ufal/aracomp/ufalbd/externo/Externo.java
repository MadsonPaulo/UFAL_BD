package br.ufal.aracomp.ufalbd.externo;

import java.util.List;

import br.ufal.aracomp.ufalbd.conceitual.metamodel.Coluna;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Condicao;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Dado;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Tabela;
import br.ufal.aracomp.ufalbd.externo.exception.InstrucaoMalFormadaException;
import br.ufal.aracomp.ufalbd.externo.metamodel.TabelaConsulta;

public class Externo implements IExterno{

	@Override
	public boolean executarDDL(String instrucao) throws InstrucaoMalFormadaException {
		String caixaBaixa = instrucao.toLowerCase();
		String[] comandos = caixaBaixa.split(" ");
		int numComandos = comandos.length;
		if (numComandos == 2) {
			if (comandos[0] == "criar_bd") {
				Conceitual.criarBanco(comandos[1]);
			} else if (comandos[0] == "excluir_bd"){
				Conceitual.apagarBanco(comandos[1]);
			}
		} else if (numComandos == 3) {
			if (comandos[0] == "criar_tb") {
				Conceitual.criarTabela(comandos[1], Tabela null); // Falta implementar a classe Tabela
			} else if (comandos[0] == "excluir_tb") {
				Conceitual.apagarTabela(comandos[1], comandos[2]);
			}
		}
		return false;
	}

	@Override
	public int executarDMLInstrucao(String instrucao) throws InstrucaoMalFormadaException {
		String caixaBaixa = instrucao.toLowerCase();
		String[] comandos = caixaBaixa.split(" ");
		int numComandos = comandos.length;
		if (numComandos == 4) {
			if (comandos[0] == "inserir") {
				Conceitual.inserirTupla(comandos[1], comandos[2], List<Dado> null); // Tratando
			} else if (comandos[0] == "excluir") {
				Conceitual.excluirTupla(comandos[1], comandos[2, Condicao null]); // Tratando
			}
		}
		return 0;
	}

	@Override
	public TabelaConsulta executarDMLConsulta(String instrucao) throws InstrucaoMalFormadaException {
		String caixaBaixa = instrucao.toLowerCase();
		String[] comandos = caixaBaixa.split(" ");
		int numComandos = comandos.length;
		if (numComandos == 5) {
			Conceitual.consultar(comandos[1], List<String> null, List<Coluna> null, Condicao null); // Tratando
		}
		return null;
	}
}