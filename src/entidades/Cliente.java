package entidades;

public class Cliente {

    private Integer idCliente;
    private String nome;

    public Cliente() {
    }

    public Cliente(Integer idCliente, String nome) {
        this.idCliente = idCliente;
        this.nome = nome;
    }

    public Cliente(String nome) {
        this.nome = nome;
    }

    public Integer getIdCliente() {
        return this.idCliente;
    }

    public Cliente setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Cliente setNome(String nome) {
        this.nome = nome;
        return this;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + this.idCliente +
                ", nome='" + this.nome + '\'' +
                '}';
    }
}