package visao;

import recursos.Constantes;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class PrincipalView extends JFrame {
    private static final long serialVersionUID = -209724459757572825L;
    private static Logger logger = Logger.getLogger(PrincipalView.class.getName());

    private JPanel panel = new JPanel();
    private JButton botaoAluguel = new JButton(Constantes.ALUGUEIS);
    private JButton botaoFilmes = new JButton(Constantes.FILMES);
    private JButton botaoClientes = new JButton(Constantes.CLIENTES);
    private JButton botaoVoltarAluguel = new JButton(Constantes.VOLTAR);
    private JButton botaoVoltarFilme = new JButton(Constantes.VOLTAR);
    private JButton botaoVoltarClientes = new JButton(Constantes.VOLTAR);
    private AluguelView aluguelView = new AluguelView();
    private ClienteView clienteView = new ClienteView();
    private FilmeView filmeView = new FilmeView();

    public PrincipalView() throws Exception {
        this.acoesBotoesVoltar();
        this.aluguelView.renderizar(this.botaoVoltarAluguel);
        this.clienteView.renderizar(this.botaoVoltarClientes);
        this.filmeView.renderizar(this.botaoVoltarFilme);
    }

    private void acoesBotoesVoltar() {
        this.botaoVoltarAluguel.addActionListener(
                e -> {
                    this.aluguelView.desativar();
                    this.ativar();
                }
        );

        this.botaoVoltarClientes.addActionListener(
                e -> {
                    this.clienteView.desativar();
                    this.ativar();
                }
        );

        this.botaoVoltarFilme.addActionListener(
                e -> {
                    this.filmeView.desativar();
                    this.ativar();
                }
        );
    }


    public void renderizar() {
        this.setTitle(Constantes.BOAS_VINDAS);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setSize(700, 700);
        this.setLocationRelativeTo(null);
        this.panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 1;
        c.weighty = .25;
        c.insets = new Insets(5, 0, 5, 0);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;

        this.botaoAluguel.addActionListener(e -> {
            this.desativar();
            this.aluguelView.ativar();
        });

        this.botaoClientes.addActionListener(e -> {
            this.desativar();
            try {
                this.clienteView.criarTabelaClientes();
            } catch (Exception ex) {
                logger.log(SEVERE, Constantes.EXCECAO, ex);
            }
            this.clienteView.ativar();
        });

        this.botaoFilmes.addActionListener(e -> {
            this.desativar();
            this.filmeView.ativar();
        });


        this.panel.add(this.botaoAluguel, c);
        this.panel.add(this.botaoClientes, c);
        this.panel.add(this.botaoFilmes, c);

        this.add(this.panel);
        this.setVisible(true);

    }

    private void desativar() {
        this.setVisible(false);
    }

    private void ativar() {
        this.setVisible(true);
    }
}
