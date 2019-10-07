package entidades;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Aluguel {
    private Integer idAluguel;
    private List<Filme> filmes;
    private Cliente cliente;
    private Date dataAluguel;
    private float valor;

    public Aluguel() {
    }

    public Aluguel(Integer idAluguel, List<Filme> filmes, Cliente cliente, Date dataAluguel, float valor) {
        this.idAluguel = idAluguel;
        this.filmes = filmes;
        this.cliente = cliente;
        this.dataAluguel = dataAluguel;
        this.valor = valor;
    }

    public Integer getIdAluguel() {
        return this.idAluguel;
    }

    public Aluguel setIdAluguel(Integer idAluguel) {
        this.idAluguel = idAluguel;
        return this;
    }

    public List<Filme> getFilmes() {
        return this.filmes;
    }

    public Aluguel setFilmes(List<Filme> filmes) {
        this.filmes = filmes;
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public Aluguel setCliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public Date getDataAluguel() {
        return this.dataAluguel;
    }

    public Aluguel setDataAluguel(Date dataAluguel) {
        this.dataAluguel = dataAluguel;
        return this;
    }

    public float getValor() {
        return this.valor;
    }

    public Aluguel setValor(float valor) {
        this.valor = valor;
        return this;
    }

    public List<String> getNomesFilmes() {
        return this.filmes.stream().map(
                Filme::getNome
        ).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return "Aluguel{" +
                "idAluguel=" + this.idAluguel +
                ", filmes=" + this.getNomesFilmes() +
                ", cliente=" + this.cliente +
                ", dataAluguel=" + sdf.format(this.dataAluguel) +
                ", valor=" + NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(this.valor) +
                '}';
    }
}
