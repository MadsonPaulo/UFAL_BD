package br.ufal.aracomp.ufalbd.interno;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import br.ufal.aracomp.ufalbd.conceitual.exception.BancoInvalidoException;
import br.ufal.aracomp.ufalbd.conceitual.exception.TabelaInvalidaException;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Dado;
import br.ufal.aracomp.ufalbd.conceitual.metamodel.Tabela;
import br.ufal.aracomp.ufalbd.externo.metamodel.TabelaConsulta;
import br.ufal.aracomp.ufalbd.interno.exception.ErroAcessoDadosException;

/**
 * Módulo interno
 * 
 * @author Madson
 *
 */
public class Interno implements IInterno {
	private final String path = System.getProperty("user.home") + File.separator + "UFAL_BD" + File.separator;
	private final String separator = "@sep@";

	// TODO Entender quando lançar exeções

	@Override
	public boolean criarBanco(String nomeBanco) throws BancoInvalidoException, ErroAcessoDadosException {
		File databaseDirectory = new File(path + nomeBanco);

		if (databaseDirectory.exists()) {// banco de dados existe
			System.out.println(nomeBanco + " já existe. Sua localização é: " + databaseDirectory);
			return false;
		} else if (databaseDirectory.mkdirs()) {// banco de dados é criado
			System.out.println(nomeBanco + " foi criado. Sua localização é: " + databaseDirectory);
			return true;
		} else {// qualquer outro caso
			System.out.println(nomeBanco + " não foi criado.");
			return false;
		}
	}

	@Override
	public boolean apagarBanco(String nomeBanco) throws BancoInvalidoException, ErroAcessoDadosException {
		File databaseDirectory = new File(path + nomeBanco);

		if (databaseDirectory.exists()) {// banco de dados existe
			// lista com todos os arquivos (tabelas) dentro da pasta (banco)
			String[] entries = databaseDirectory.list();
			// apaga todos os arquivos dentro do banco
			for (String s : entries) {
				File currentFile = new File(databaseDirectory.getPath(), s);
				currentFile.delete();
			}
			// apaga o banco
			databaseDirectory.delete();
			System.out.println(nomeBanco + " excluído com sucesso de " + path);
			return true;
		} else {// banco de dados não existe
			System.out.println(nomeBanco + " não exite em " + path);
			return false;
		}
	}

	@Override
	public boolean criarTabela(String nomeBanco, Tabela descricaoTabela)
			throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException {
		try {
			File databaseDirectory = new File(path + nomeBanco);
			if (databaseDirectory.exists()) {// banco de dados existe
				File tableDirectory = new File(path + nomeBanco + File.separator + descricaoTabela.getNome());

				if (tableDirectory.exists()) {// tabela existe
					System.out.println("A tabela " + descricaoTabela.getNome() + " já existe no banco de dados "
							+ nomeBanco + ".");
					return false;
				} else {// tabela não existe
					// cria a tabela
					FileWriter fW = new FileWriter(tableDirectory, true);
					fW.close();
					System.out.println("A tabela " + descricaoTabela.getNome()
							+ " foi criada com sucesso no banco de dados " + nomeBanco + ".");
					return false;
				}
			} else {// banco de dados não existe
				System.out.println(nomeBanco + " não existe em " + path);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean apagarTabela(String nomeBanco, String nomeTabela)
			throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException {
		File databaseDirectory = new File(path + nomeBanco);
		if (databaseDirectory.exists()) {// banco de dados existe
			File tableDirectory = new File(path + nomeBanco + File.separator + nomeTabela);

			if (tableDirectory.exists()) {// tabela existe
				tableDirectory.delete();
				System.out.println(
						"A tabela " + nomeTabela + " foi excluída com sucesso do banco de dados " + nomeBanco + ".");
				return true;
			} else {// tabela não existe
				System.out.println("A tabela " + nomeTabela + " não existe no banco de dados " + nomeBanco + ".");
				return false;
			}
		} else {// banco de dados não existe
			System.out.println(nomeBanco + " não existe em " + path);
			return false;
		}
	}

	@Override
	public boolean inserirTupla(String nomeBanco, String nomeTabela, List<Dado> dados)
			throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException {
		File databaseDirectory = new File(path + nomeBanco);
		if (databaseDirectory.exists()) {// se o banco de dados existe
			File tableDirectory = new File(path + nomeBanco + File.separator + nomeTabela);

			if (tableDirectory.exists()) {// tabela existe
				try {
					String data = "";
					FileWriter fW = new FileWriter(tableDirectory, true);
					PrintWriter printWriter = new PrintWriter(fW);

					// transforma os dados numa única string
					for (int i = 0; i < dados.size(); i++) {
						if (i < dados.size() - 1) {
							data += dados.get(i).obterValorDado().toString() + separator;
						} else {
							data += dados.get(i).obterValorDado().toString() + "\n";
						}
					}
					// grava a tupla na tabela
					printWriter.printf(data);
					fW.close();
					System.out.println("Tupla inserida com sucesso.");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			} else {// tabela não existe
				System.out.println("A tabela " + nomeTabela + " não existe no banco de dados " + nomeBanco + ".");
				return false;
			}
		} else {// se o banco de dados não existe
			System.out.println(nomeBanco + " não existe em " + path);
			return false;
		}
	}

	@Override
	public boolean excluirTupla(String nomeBanco, String nomeTabela, int ordem)
			throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException {
		File databaseDirectory = new File(path + nomeBanco);
		if (databaseDirectory.exists()) {// se o banco de dados existe
			File tableDirectory = new File(path + nomeBanco + File.separator + nomeTabela);

			if (tableDirectory.exists()) {// tabela existe
				try {
					Path local = tableDirectory.toPath();
					ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(local);

					lines.remove(ordem);
					Files.write(local, lines);
					System.out.println("Tupla removida com sucesso.");
					return true;
				} catch (IndexOutOfBoundsException i) {
					System.out.println("Não existe tupla na linha " + ordem + ".");
					return false;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			} else {// tabela não existe
				System.out.println("A tabela " + nomeTabela + " não existe no banco de dados " + nomeBanco + ".");
				return false;
			}
		} else {// se o banco de dados não existe
			System.out.println(nomeBanco + " não existe em " + path);
			return false;
		}
	}

	//TODO
	@Override
	public TabelaConsulta obterTuplas(String nomeBanco, String nomeTabela)
			throws BancoInvalidoException, TabelaInvalidaException, ErroAcessoDadosException {
		File databaseDirectory = new File(path + nomeBanco);
		if (databaseDirectory.exists()) {// se o banco de dados existe
			File tableDirectory = new File(path + nomeBanco + File.separator + nomeTabela);

			if (tableDirectory.exists()) {// tabela existe

			} else {// tabela não existe
				System.out.println("A tabela " + nomeTabela + " não existe no banco de dados " + nomeBanco + ".");
				return null;
			}
		} else {// se o banco de dados não existe
			System.out.println(nomeBanco + " não existe em " + path);

		}
		return null;
	}

}
