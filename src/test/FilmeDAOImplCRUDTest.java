package test;

import dao.FilmeDAO;
import dao.jdbc.FilmeDAOImpl;
import entidades.Filme;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Collection;
import java.util.Date;

import static java.sql.DriverManager.getConnection;
import static org.junit.Assert.*;
import static recursos.ConfigConstantes.*;

public class FilmeDAOImplCRUDTest {
    private FilmeDAO dao = new FilmeDAOImpl();
    private Connection conn;
    private Filme filme;

    @Before
    public void setUp() throws Exception {
        this.conn = getConnection(URL_DB, USER_DB, SENHA_DB);
        this.conn.setAutoCommit(false);
        this.filme = new Filme(null, new Date(1262311200000L), "Inception", "Filme de ficção científica/Thriller");
    }

    @After
    public void tearDown() throws Exception {
        this.conn.rollback();
    }

    @Test
    public void CRUDTest() throws Exception {
        this.dao.insert(this.conn, this.filme);
        Filme filmeFind = this.dao.find(this.conn, this.filme.getIdFilme());
        assertEquals(this.filme.getDataLancamento(), filmeFind.getDataLancamento());
        assertEquals(this.filme.getDescricao(), filmeFind.getDescricao());
        assertEquals(this.filme.getNome(), filmeFind.getNome());


        Collection<Filme> list = this.dao.list(this.conn);
        assertNotNull(list);
        for (Filme filmeDaLista :
                list) {
            System.out.println(filmeDaLista.toString());
        }

        String nome = "A Origem";
        String descricao = "Thriller/Filme de ficção científica/";
        Date dataExata = new Date(1281063600000L);

        this.filme.setNome(nome);
        this.filme.setDescricao(descricao);
        this.filme.setDataLancamento(dataExata);

        this.dao.edit(this.conn, this.filme);

        filmeFind = this.dao.find(this.conn, this.filme.getIdFilme());
        assertEquals(nome, filmeFind.getNome());
        assertEquals(descricao, filmeFind.getDescricao());
        assertEquals(dataExata, filmeFind.getDataLancamento());

        this.dao.delete(this.conn, this.filme.getIdFilme());
        filmeFind = this.dao.find(this.conn, this.filme.getIdFilme());
        assertNull(filmeFind);
    }


}