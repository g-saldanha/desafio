package dao.jdbc;

import dao.AluguelDAO;
import dao.ClienteDAO;
import entidades.Aluguel;
import entidades.Cliente;
import entidades.Filme;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AluguelDAOImpl implements AluguelDAO {
    private ClienteDAO clienteDAO = new ClienteDAOImpl();


    @Override
    public void insert(Connection conn, Aluguel aluguel) throws Exception {
        PreparedStatement myStmt = conn.prepareStatement("insert into en_aluguel (id_aluguel, id_cliente, data_aluguel, valor) values (?, ? ,? ,?)");

        Integer idAluguel = this.getNextId(conn);

        myStmt.setInt(1, idAluguel);
        myStmt.setInt(2, aluguel.getCliente().getIdCliente());
        myStmt.setDate(3, new Date(aluguel.getDataAluguel().getTime()));
        myStmt.setFloat(4, aluguel.getValor());

        myStmt.execute();
        conn.commit();

        aluguel.setIdAluguel(idAluguel);

        this.insertFilmes(conn, aluguel);
    }

    private void insertFilmes(Connection conn, Aluguel aluguel) throws SQLException {
        if (!aluguel.getFilmes().isEmpty()) {
            for (Filme filme :
                    aluguel.getFilmes()) {
                PreparedStatement myStmt = conn.prepareStatement("insert into re_aluguel_filme (id_filme, id_aluguel) values (?, ?)");

                myStmt.setInt(1, filme.getIdFilme());
                myStmt.setInt(2, aluguel.getIdAluguel());

                myStmt.execute();
                conn.commit();
            }
        }
    }

    @Override
    public Integer getNextId(Connection conn) throws Exception {
        PreparedStatement myStmt = conn.prepareStatement("select nextval('seq_en_aluguel')");
        ResultSet myRs = myStmt.executeQuery();
        myRs.next();
        return myRs.getInt(1);
    }

    @Override
    public void edit(Connection conn, Aluguel aluguel) throws Exception {
        PreparedStatement myStmt = conn.prepareStatement("update en_aluguel set id_cliente = (?), data_aluguel = (?), valor = (?) where id_aluguel = (?)");

        myStmt.setInt(4, aluguel.getIdAluguel());
        myStmt.setInt(1, aluguel.getCliente().getIdCliente());
        myStmt.setDate(2, new Date(aluguel.getDataAluguel().getTime()));
        myStmt.setFloat(3, aluguel.getValor());

        myStmt.execute();
        conn.commit();

        this.updateFilmes(conn, aluguel);
    }

    private void updateFilmes(Connection conn, Aluguel aluguel) throws SQLException {
        this.deleteFilmes(conn, aluguel.getIdAluguel());
        this.insertFilmes(conn, aluguel);
    }

    private void deleteFilmes(Connection conn, Integer idAluguel) throws SQLException {
        PreparedStatement myStmt = conn.prepareStatement("delete from re_aluguel_filme where id_aluguel = (?)");

        myStmt.setInt(1, idAluguel);

        myStmt.execute();
        conn.commit();
    }

    @Override
    public void delete(Connection conn, Aluguel aluguel) throws Exception {
        this.deleteRelationship(conn, aluguel);

        PreparedStatement myStmt = conn.prepareStatement("update en_aluguel set status = false where id_aluguel = (?)");

        myStmt.setInt(1, aluguel.getIdAluguel());

        myStmt.execute();
        conn.commit();

    }

    private void deleteRelationship(Connection conn, Aluguel aluguel) throws SQLException {
        PreparedStatement myStmt = conn.prepareStatement("delete from re_aluguel_filme where id_aluguel = (?)");

        myStmt.setInt(1, aluguel.getIdAluguel());

        myStmt.execute();
        conn.commit();
    }

    @Override
    public Aluguel find(Connection conn, Integer idAluguel) throws Exception {
        PreparedStatement myStmt = conn.prepareStatement("select * from en_aluguel where id_aluguel = (?) and status = true;");

        myStmt.setInt(1, idAluguel);

        ResultSet myRs = myStmt.executeQuery();

        if (!myRs.next()) {
            return null;
        }

        Integer idCliente = myRs.getInt("id_cliente");
        Date dataAluguelSql = myRs.getDate("data_aluguel");
        Float valor = myRs.getFloat("valor");

        Cliente cliente = this.clienteDAO.find(conn, idCliente);
        java.util.Date dataAluguelUtil = new java.util.Date(dataAluguelSql.getTime());
        List<Filme> listaDeFilmes = new ArrayList<>();
        this.getFilmes(conn, idAluguel, listaDeFilmes);

        return new Aluguel(idAluguel, listaDeFilmes, cliente, dataAluguelUtil, valor);

    }

    private void getFilmes(Connection conn, Integer idAluguel, List<Filme> listaDeFilmes) throws SQLException {
        PreparedStatement myStmt = conn.prepareStatement("select * from re_aluguel_filme inner join en_filme on re_aluguel_filme.id_filme = en_filme.id_filme where id_aluguel = (?);");

        myStmt.setInt(1, idAluguel);

        ResultSet myRs = myStmt.executeQuery();

        while (myRs.next()) {
            Integer idFilme = myRs.getInt("id_filme");
            java.util.Date dataLancamento = new java.util.Date(myRs.getDate("data_lancamento").getTime());
            String nome = myRs.getString("nome");
            String descricao = myRs.getString("descricao");

            Filme filmeParaLista = new Filme(idFilme, dataLancamento, nome, descricao);

            listaDeFilmes.add(filmeParaLista);
        }


    }

    @Override
    public Collection<Aluguel> list(Connection conn) throws Exception {

        PreparedStatement myStmt = conn.prepareStatement("select * from en_aluguel where  status = true order by id_aluguel");

        ResultSet myRs = myStmt.executeQuery();

        Collection<Aluguel> items = new ArrayList<>();

        while (myRs.next()) {
            Integer idAluguel = myRs.getInt("id_aluguel");
            List<Filme> listaDeFilmes = new ArrayList<>();
            Cliente cliente = this.clienteDAO.find(conn, myRs.getInt("id_cliente"));
            java.util.Date dataAluguel = new java.util.Date(myRs.getDate("data_aluguel").getTime());
            float valor = myRs.getFloat("valor");
            this.getFilmes(conn, idAluguel, listaDeFilmes);

            items.add(new Aluguel(idAluguel, listaDeFilmes, cliente, dataAluguel, valor));
        }


        return items;
    }
}
