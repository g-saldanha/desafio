import dao.AluguelDAO;
import dao.ClienteDAO;
import dao.FilmeDAO;
import dao.jdbc.AluguelDAOImpl;
import dao.jdbc.ClienteDAOImpl;
import dao.jdbc.FilmeDAOImpl;
import entidades.Aluguel;
import entidades.Cliente;
import entidades.Filme;
import recursos.Constantes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.logging.Level.*;
import static recursos.ConfigConstantes.*;
import static recursos.Constantes.EXCECAO;
import static recursos.Constantes.LISTA_DE;


public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());
    private static ClienteDAO clienteDAO = new ClienteDAOImpl();
    private static FilmeDAO filmeDAO = new FilmeDAOImpl();
    private static AluguelDAO aluguelDao = new AluguelDAOImpl();
    private static final String MSG_LOG = "%s foi %s base de dados \n";
    private static final String ADD = "adicionado a";
    private static final String UPD = "editado na";
    private static final String DEL = "removido da";
    private static final String DEL_ERRO = "Nao foi possivel deletar!";

    public static void main(String[] args) {
        Connection conn = null;
        try {
            Class.forName(DB_TYPE);
            conn = DriverManager.getConnection(URL_DB, USER_DB, SENHA_DB);
            conn.setAutoCommit(false);

            //Demonstrar o funcionamento aqui
            crudCliente(conn);
            crudFilme(conn);
            crudAluguel(conn);

        } catch (Exception e) {
            logger.log(SEVERE, EXCECAO, e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.log(SEVERE, EXCECAO, e);
            }
        }
        logger.log(INFO, Constantes.FIM_TESTE);
    }

    private static void crudCliente(Connection conn) throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNome("Gabriel Saldanha");
        createCliente(conn, cliente);

        readCliente(conn);

        cliente.setNome("Gabriel Saldanha da Silva Athayde");
        updateCliente(conn, cliente);

        deleteCliente(conn, cliente);
    }

    private static void createCliente(Connection conn, Cliente cliente) throws Exception {
        clienteDAO.insert(conn, cliente);
        Cliente clienteBD = clienteDAO.find(conn, cliente.getIdCliente());
        logger.log(INFO, String.format(MSG_LOG, clienteBD.toString(), ADD));
    }

    private static void readCliente(Connection conn) throws Exception {
        Collection<Cliente> colecaoDeClientes = clienteDAO.list(conn);
        List<String> listaDeClientes = colecaoDeClientes.stream().map(
                cliente -> cliente.toString() + "\n"
        ).collect(Collectors.toList());
        String msg = String.format(LISTA_DE, "Clientes \n") + listaDeClientes + "\n";
        logger.log(INFO, msg);
    }

    private static void updateCliente(Connection conn, Cliente cliente) throws Exception {
        clienteDAO.edit(conn, cliente);
        Cliente clienteBD = clienteDAO.find(conn, cliente.getIdCliente());
        logger.log(INFO, String.format(MSG_LOG, clienteBD.toString(), UPD));
    }

    private static void deleteCliente(Connection conn, Cliente cliente) throws Exception {
        clienteDAO.delete(conn, cliente.getIdCliente());
        Cliente clienteBD = clienteDAO.find(conn, cliente.getIdCliente());
        if (clienteBD == null) {
            logger.log(WARNING, String.format(MSG_LOG, cliente.toString(), DEL));
        } else {
            logger.log(WARNING, DEL_ERRO);
        }
    }

    private static void crudFilme(Connection conn) throws Exception {
        Filme filme = new Filme();
        filme.setNome("Vingadores - Ultimato");
        filme.setDescricao("Fantasia/Filme de ficção");
        filme.setDataLancamento(new Date(1554102000000L));
        createFilme(conn, filme);

        readFilme(conn);

        filme.setDescricao("Avengers - Ultimato");
        filme.setDataLancamento(new Date(1556175600000L));
        updateFilme(conn, filme);

        deleteFilme(conn, filme);
    }

    private static void createFilme(Connection conn, Filme filme) throws Exception {
        filmeDAO.insert(conn, filme);
        Filme filmeDB = filmeDAO.find(conn, filme.getIdFilme());
        logger.log(INFO, String.format(MSG_LOG, filmeDB.toString(), ADD));
    }

    private static void readFilme(Connection conn) throws Exception {
        Collection<Filme> colecaoDeFilmes = filmeDAO.list(conn);
        List<String> listaDeFilmes = colecaoDeFilmes.stream().map(
                filme -> filme.toString() + "\n"
        ).collect(Collectors.toList());
        String msg = String.format(LISTA_DE, "Filmes \n") + listaDeFilmes + "\n";
        logger.log(INFO, msg);
    }

    private static void updateFilme(Connection conn, Filme filme) throws Exception {
        Filme filmeAntes = filmeDAO.find(conn, filme.getIdFilme());
        filmeDAO.edit(conn, filme);
        Filme filmeDepois = filmeDAO.find(conn, filme.getIdFilme());
        logger.log(INFO, String.format(MSG_LOG, filmeAntes + "->" + filmeDepois, UPD));
    }

    private static void deleteFilme(Connection conn, Filme filme) throws Exception {
        filmeDAO.delete(conn, filme.getIdFilme());
        Filme filmeDB = filmeDAO.find(conn, filme.getIdFilme());
        if (filmeDB == null) {
            logger.log(WARNING, String.format(MSG_LOG, filme.toString(), DEL));
        } else {
            logger.log(WARNING, DEL_ERRO);
        }
    }

    private static void crudAluguel(Connection conn) throws Exception {
        Aluguel aluguel = new Aluguel();
        aluguel.setDataAluguel(new Date());
        aluguel.setValor(15.5f);
        aluguel.setCliente(clienteDAO.find(conn, 1));
        aluguel.setFilmes(new ArrayList<>());
        aluguel.getFilmes().add(filmeDAO.find(conn, 1));
        createAluguel(conn, aluguel);

        readAluguel(conn);

        aluguel.getFilmes().add(filmeDAO.find(conn, 2));
        aluguel.setCliente(clienteDAO.find(conn, 2));
        aluguel.setValor(14.5f);
        aluguel.setDataAluguel(new Date(1554102000000L));
        updateAluguel(conn, aluguel);

        deleteAluguel(conn, aluguel);
    }

    private static void createAluguel(Connection conn, Aluguel aluguel) throws Exception {
        aluguelDao.insert(conn, aluguel);
        Aluguel aluguelBD = aluguelDao.find(conn, aluguel.getIdAluguel());
        logger.log(INFO, String.format(MSG_LOG, aluguelBD.toString(), ADD));
    }

    private static void readAluguel(Connection conn) throws Exception {
        Collection<Aluguel> colecaoDeAlugueis = aluguelDao.list(conn);
        List<String> listaDeAlugueis = colecaoDeAlugueis.stream().map(
                aluguel -> aluguel.toString() + "\n"
        ).collect(Collectors.toList());
        String msg = String.format(LISTA_DE, "Alugueis \n") + listaDeAlugueis + "\n";
        logger.log(INFO, msg);
    }

    private static void updateAluguel(Connection conn, Aluguel aluguel) throws Exception {
        Aluguel aluguelAntes = aluguelDao.find(conn, aluguel.getIdAluguel());
        aluguelDao.edit(conn, aluguel);
        Aluguel aluguelDepois = aluguelDao.find(conn, aluguel.getIdAluguel());
        logger.log(INFO, String.format(MSG_LOG, aluguelAntes.toString() + "->\n->" + aluguelDepois.toString(), UPD));

    }

    private static void deleteAluguel(Connection conn, Aluguel aluguel) throws Exception {
        aluguelDao.delete(conn, aluguel);
        Aluguel alugueBD = aluguelDao.find(conn, aluguel.getIdAluguel());
        if (alugueBD == null) {
            logger.log(WARNING, String.format(MSG_LOG, aluguel.toString(), DEL));
        } else {
            logger.log(WARNING, DEL_ERRO);
        }
    }
}