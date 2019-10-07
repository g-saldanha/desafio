package dao.jdbc;

import dao.FilmeDAO;
import entidades.Filme;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class FilmeDAOImpl implements FilmeDAO {
    @Override
    public void insert(Connection conn, Filme filme) throws Exception {

        PreparedStatement myStmt = conn.prepareStatement("insert into en_filme(id_filme, data_lancamento, nome, descricao) values (?, ?, ?, ?)");

        Integer idFilme = this.getNextId(conn);

        myStmt.setInt(1, idFilme);
        myStmt.setDate(2, new Date(filme.getDataLancamento().getTime()));
        myStmt.setString(3, filme.getNome());
        myStmt.setString(4, filme.getDescricao());

        myStmt.execute();
        conn.commit();

        filme.setIdFilme(idFilme);
    }

    @Override
    public Integer getNextId(Connection conn) throws Exception {
        PreparedStatement myStmt = conn.prepareStatement("select nextval('seq_en_filme')");
        ResultSet rs = myStmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    @Override
    public void edit(Connection conn, Filme filme) throws Exception {
        PreparedStatement myStmt = conn.prepareStatement("update en_filme set nome = (?), descricao = (?), data_lancamento = (?) where id_filme = (?)");

        myStmt.setString(1, filme.getNome());
        myStmt.setString(2, filme.getDescricao());
        myStmt.setDate(3, new Date(filme.getDataLancamento().getTime()));
        myStmt.setInt(4, filme.getIdFilme());

        myStmt.execute();
        conn.commit();
    }

    @Override
    public void delete(Connection conn, Integer idFilme) throws Exception {
        this.deleteRelationship(conn, idFilme);
        PreparedStatement myStmt = conn.prepareStatement("update en_filme set status = false where id_filme = (?)");

        myStmt.setInt(1, idFilme);
        myStmt.execute();
        conn.commit();
    }

    private void deleteRelationship(Connection conn, Integer idFilme) throws SQLException {
        PreparedStatement myStmt = conn.prepareStatement("delete from re_aluguel_filme where id_filme = (?)");

        myStmt.setInt(1, idFilme);
        myStmt.execute();
        conn.commit();
    }

    @Override
    public Filme find(Connection conn, Integer idFilme) throws Exception {
        PreparedStatement myStmt = conn.prepareStatement("select * from en_filme where id_filme = (?) and status = true");
        myStmt.setInt(1, idFilme);

        ResultSet myRs = myStmt.executeQuery();

        if (!myRs.next()) {
            return null;
        }

        String nome = myRs.getString("nome");
        String descricao = myRs.getString("descricao");
        Date dataLancamentoSql = myRs.getDate("data_lancamento");
        java.util.Date dataLancamentoUtil = new java.util.Date(dataLancamentoSql.getTime());

        return new Filme(idFilme, dataLancamentoUtil, nome, descricao);
    }

    @Override
    public Collection<Filme> list(Connection conn) throws Exception {
        PreparedStatement myStmt = conn.prepareStatement("select * from en_filme where status = true order by nome;");
        ResultSet myRs = myStmt.executeQuery();

        Collection<Filme> filmes = new ArrayList<>();

        while (myRs.next()) {
            Integer idFilme = myRs.getInt("id_filme");
            String nome = myRs.getString("nome");
            String descricao = myRs.getString("descricao");
            Date dataLancamento = myRs.getDate("data_lancamento");
            new java.util.Date(dataLancamento.getTime());

            filmes.add(new Filme(idFilme, dataLancamento, nome, descricao));
        }


        return filmes;
    }
}
