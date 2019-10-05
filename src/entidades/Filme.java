package entidades;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Filme {

    private Integer idFilme;
    private Date dataLancamento;
    private String nome;
    private String descricao;

    public Filme() {
    }

    public Filme(Integer idFilme, Date dataLancamento, String nome, String descricao) {
        this.idFilme = idFilme;
        this.dataLancamento = dataLancamento;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Integer getIdFilme() {
        return this.idFilme;
    }

    public Filme setIdFilme(Integer idFilme) {
        this.idFilme = idFilme;
        return this;
    }

    public Date getDataLancamento() {
        return this.dataLancamento;
    }

    public Filme setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Filme setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Filme setDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "Filme{" +
                "idFilme=" + this.idFilme +
                ", dataLancamento=" + sdf.format(this.dataLancamento) +
                ", nome='" + this.nome + '\'' +
                ", descricao='" + this.descricao + '\'' +
                '}';
    }
}
