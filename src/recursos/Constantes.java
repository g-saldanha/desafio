package recursos;

import javax.swing.*;

public class Constantes {
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";


    public static final String FIM_TESTE = ANSI_GREEN_BACKGROUND + ANSI_BLACK + "Fim do teste." + ANSI_RESET;
    public static final String LISTA_DE = "Lista de %s";


    public static final String BOAS_VINDAS = "Desafio Pari Passu";
    public static final String ALUGUEIS = "Aluguéis";
    public static final String FILMES = "Filmes";
    public static final String CLIENTES = "Clientes";
    public static final String TELA_CLIENTES = "Tela de Clientes";
    public static final String TELA_FILMES = "Tela de Filmes";
    public static final String TELA_ALUGUEIS = "Tela de Aluguéis";
    public static final JLabel ACOES = new JLabel("Acoes");
    public static final String ID = "ID";
    public static final String VOLTAR = "Voltar";
    public static final String EDITAR = "Editar";
    public static final String DELETAR = "Deletar";
    public static final String CADASTRAR_CLIENTE = "Cadastrar Cliente";
    public static final String CADASTRAR_FILME = "Cadastrar Filme";
    public static final String CADASTRAR_ALUGUEL = "Cadastrar Aluguel";
    public static final String NOME = "Nome";
    public static final String DESCRICAO = "Descricao";

    public static final String DATA = "Data";
    public static final String VALOR = "Valor";
    public static final String EXCECAO = "Foi causada uma exceção que travou a execucao da aplicacao";
    public static final String EM_BRANCO = "Campos em branco ou errados, nao foi possivel efetuar acao";
    public static final String ERRO = "Erro";
    public static final String NAO_EXISTE = "Dado nao existe na base de dados";
    public static final String LISTAGEM_DE_FILMES = "Listagem de Filmes";
    public static final String DIGITE_O_ID = "Digite o ID:";
    public static final String EX_DATA = ": ex(06/12/1987 = 06121987)";
    public static final String SELECIONE = "Selecione algum item para editar";
}
