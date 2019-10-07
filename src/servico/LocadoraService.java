package servico;

import dao.AluguelDAO;
import dao.ClienteDAO;
import dao.FilmeDAO;
import dao.jdbc.AluguelDAOImpl;
import dao.jdbc.ClienteDAOImpl;
import dao.jdbc.FilmeDAOImpl;
import entidades.Aluguel;
import entidades.Cliente;
import entidades.Filme;

import javax.swing.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static recursos.Constantes.EXCECAO;

public class LocadoraService {
    private static Logger logger = Logger.getLogger(LocadoraService.class.getName());

    private static LocadoraService instancia = new LocadoraService();
    private AluguelDAO aluguelDao = new AluguelDAOImpl();
    private FilmeDAO filmeDAO = new FilmeDAOImpl();
    private ClienteDAO clienteDAO = new ClienteDAOImpl();
    private Connection conn;

    public void setConnection(Connection connection) {
        this.conn = connection;
    }

    public static LocadoraService getInstance() {
        return instancia;
    }

    public Collection<Aluguel> getAlugueis() throws Exception {
        return this.aluguelDao.list(this.conn);
    }

    public Collection<Filme> getFilmes() throws Exception {
        return this.filmeDAO.list(this.conn);
    }

    public Collection<Cliente> getClientes() throws Exception {
        return this.clienteDAO.list(this.conn);
    }


    public void createCliente(String nome) throws Exception {
        this.clienteDAO.insert(this.conn, new Cliente(null, nome));
    }

    public boolean hasCliente(int id) throws Exception {
        return this.clienteDAO.find(this.conn, id) != null;
    }

    public Cliente findCliente(int id) throws Exception {
        return this.clienteDAO.find(this.conn, id);
    }

    public void deleteCliente(Integer id) throws Exception {
        this.clienteDAO.delete(this.conn, id);
    }

    public void createFilme(String nome, String data, String descricao) throws Exception {
        this.filmeDAO.insert(this.conn, new Filme(null, this.convertToDateViaInstant(data), nome, descricao));
    }

    public Date convertToDateViaInstant(String data) {
        LocalDate date = LocalDate.of(Integer.parseInt(data.substring(4, 8)), Integer.parseInt(data.substring(2, 4)), Integer.parseInt(data.substring(0, 2)));
        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public boolean hasFilme(int idToInteger) throws Exception {
        return this.filmeDAO.find(this.conn, idToInteger) != null;
    }

    public Filme findFilme(int idToInteger) throws Exception {
        return this.filmeDAO.find(this.conn, idToInteger);
    }

    public void deleteFilme(int idToInteger) throws Exception {
        this.filmeDAO.delete(this.conn, idToInteger);
    }

    public Vector getOpcaoFilmes() {
        Vector opcoes = new Vector();
        Collection<Filme> filmes = Collections.emptyList();
        try {
            filmes = this.getFilmes();
        } catch (Exception e) {
            logger.log(SEVERE, EXCECAO, e);
        }
        filmes.forEach(filme -> opcoes.addElement(filme.getIdFilme() + " ->" + filme.getNome()));
        return opcoes;
    }

    public Vector getOpcaoClientes() {
        Vector opcoes = new Vector();
        Collection<Cliente> clientes = Collections.emptyList();
        try {
            clientes = this.getClientes();
        } catch (Exception e) {
            logger.log(SEVERE, EXCECAO, e);
        }
        clientes.forEach(cliente -> opcoes.addElement(cliente.getIdCliente() + " ->" + cliente.getNome()));
        return opcoes;
    }

    public void editCliente(Integer id, String nomeCliente) throws Exception {
        this.clienteDAO.edit(this.conn, new Cliente(id, nomeCliente));
    }

    public JTextField getTextData(Date dataLancamento) {
        return new JTextField(new SimpleDateFormat("ddMMyyyy").format(dataLancamento));
    }

    public void editFilme(Integer id, String nomeFilmeText, String dataText, String descricaoText) throws Exception {
        Date date = this.convertToDateViaInstant(dataText);
        this.filmeDAO.edit(this.conn, new Filme(id, date, nomeFilmeText, descricaoText));

    }

    public void createAluguel(String idNomeCliente, String dataText, String valorText, List<String> idsNomesfilmes) throws Exception {
        Cliente cliente = this.clienteDAO.find(this.conn, Integer.parseInt(idNomeCliente.substring(0, 2)));
        Date dataAluguel = this.convertToDateViaInstant(dataText);
        Float valor = Float.parseFloat(valorText);
        List<Filme> filmes = new ArrayList<>();
        for (String s : idsNomesfilmes) {
            filmes.add(this.filmeDAO.find(this.conn, Integer.parseInt(s.substring(0, 2).replace(" ", ""))));
        }
        this.aluguelDao.insert(this.conn, new Aluguel(null, filmes, cliente, dataAluguel, valor));
    }

    public void deleteAluguel(int idToInteger) throws Exception {
        Aluguel aluguel = this.findAluguel(idToInteger);
        this.aluguelDao.delete(this.conn, aluguel);
    }

    public Aluguel findAluguel(int idToInteger) throws Exception {
        return this.aluguelDao.find(this.conn, idToInteger);
    }

    public boolean hasAlugel(int idToInteger) throws Exception {
        return this.aluguelDao.find(this.conn, idToInteger) != null;
    }

    public void editAlugel(Integer id, String dataText, String valorText, String idNomeCliente, List<String> idsNomesfilmes) throws Exception {
        Cliente cliente = this.clienteDAO.find(this.conn, Integer.parseInt(idNomeCliente.substring(0, 2)));
        Date dataAluguel = this.convertToDateViaInstant(dataText);
        Float valor = Float.parseFloat(valorText);
        List<Filme> filmes = new ArrayList<>();
        for (String s : idsNomesfilmes) {
            filmes.add(this.filmeDAO.find(this.conn, Integer.parseInt(s.substring(0, 2).replace(" ", ""))));
        }
        this.aluguelDao.edit(this.conn, new Aluguel(id, filmes, cliente, dataAluguel, valor));
    }
}
