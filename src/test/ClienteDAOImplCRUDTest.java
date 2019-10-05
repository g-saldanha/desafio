package test;

import dao.ClienteDAO;
import dao.jdbc.ClienteDAOImpl;
import entidades.Cliente;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

import static org.junit.Assert.*;

public class ClienteDAOImplCRUDTest {
    private Connection conn;
    private ClienteDAO dao;
    private Cliente cliente;

    @Before
    public void setUp() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "gbl");
        this.conn.setAutoCommit(false);
        this.dao = new ClienteDAOImpl();
        this.cliente = new Cliente();
        this.cliente.setNome("Gabriel Saldanha");
    }

    @After
    public void tearDown() throws SQLException {
        this.conn.rollback();
    }

    @Test
    public void CRUDTest() throws Exception {
        this.dao.insert(this.conn, this.cliente);
        Cliente cliente = this.dao.find(this.conn, this.cliente.getIdCliente());
        assertEquals("Gabriel Saldanha", cliente.getNome());

        Collection<Cliente> list = this.dao.list(this.conn);
        assertNotNull(list);
        for (Cliente clienteDaLista :
                list) {
            System.out.println(cliente.toString());
        }

        String nome = "Gabriel Saldanha da Silva Athayde";

        this.cliente.setNome(nome);
        this.dao.edit(this.conn, this.cliente);

        cliente = this.dao.find(this.conn, this.cliente.getIdCliente());
        assertEquals(nome, cliente.getNome());

        this.dao.delete(this.conn, this.cliente.getIdCliente());
        cliente = this.dao.find(this.conn, this.cliente.getIdCliente());
        assertNull(cliente);
    }
}