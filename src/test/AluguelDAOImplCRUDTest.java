package test;

import dao.AluguelDAO;
import dao.jdbc.AluguelDAOImpl;
import entidades.Aluguel;
import entidades.Cliente;
import entidades.Filme;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AluguelDAOImplCRUDTest {
    private AluguelDAO dao = new AluguelDAOImpl();
    private Connection conn;
    private Aluguel aluguel;
    private List<Filme> listaDeFilmes;
    private Cliente clienteAna;

    @Before
    public void setUp() throws Exception {
        this.listaDeFilmes = new ArrayList<>();
        this.listaDeFilmes.add(new Filme(1, new Date(), "Lagoa Azul", "Drama/Filme para adolescentes"));
        this.listaDeFilmes.add(new Filme(2, new Date(), "Eu Sou a Lenda", "Thriller/Drama"));
        this.listaDeFilmes.add(new Filme(3, new Date(), "Lagoa Azul", "Thriller/Drama"));
        this.clienteAna = new Cliente(1, "Ana");

        this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "gbl");
        this.conn.setAutoCommit(false);
        this.aluguel = new Aluguel(null, this.listaDeFilmes, this.clienteAna, new Date(), 20.99f);
    }

    @After
    public void tearDown() throws Exception {
        this.conn.rollback();
    }


    @Test
    public void CRUDTest() throws Exception {
        this.dao.insert(this.conn, this.aluguel);
        Aluguel aluguelFind = this.dao.find(this.conn, this.aluguel.getIdAluguel());
//        assertEquals(this.aluguel, aluguelFind);

        Collection<Aluguel> list = this.dao.list(this.conn);
        assertNotNull(list);
        System.out.println("1");
        for (Aluguel aluguelDaLista :
                list) {
            System.out.println(aluguelDaLista.toString());
        }

        this.aluguel.setCliente(new Cliente(4, "Felippe"));
        this.listaDeFilmes.remove(2);
        this.aluguel.setDataAluguel(new Date(1262311200000L));
        this.aluguel.setValor(15.99f);
        this.dao.edit(this.conn, this.aluguel);
        aluguelFind = this.dao.find(this.conn, this.aluguel.getIdAluguel());
//        assertEquals(this.aluguel.getCliente(), aluguelFind.getCliente());
//        assertEquals(this.aluguel.getDataAluguel(), aluguelFind.getDataAluguel());
//        assertEquals(this.aluguel.getValor(), aluguelFind.getValor());
//        assertEquals(this.aluguel.getFilmes(), aluguelFind.getFilmes());
        Collection<Aluguel> list2 = this.dao.list(this.conn);
        assertNotNull(list2);
        System.out.println("2");
        for (Aluguel aluguelDaLista :
                list2) {
            System.out.println(aluguelDaLista.toString());
        }


        this.dao.delete(this.conn, this.aluguel);
        aluguelFind = this.dao.find(this.conn, this.aluguel.getIdAluguel());
        assertNull(aluguelFind);
        Collection<Aluguel> list3 = this.dao.list(this.conn);
        assertNotNull(list3);
        System.out.println("3");
        for (Aluguel aluguelDaLista :
                list3) {
            System.out.println(aluguelDaLista.toString());
        }
    }
}