package visao;

import entidades.Cliente;
import recursos.Constantes;
import servico.LocadoraService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static recursos.Constantes.ID;
import static recursos.Constantes.NOME;

public class ClienteView extends JFrame {
    private static final long serialVersionUID = 1105246255900986748L;
    private static Logger logger = Logger.getLogger(ClienteView.class.getName());
    private JPanel panelAcoes;
    private JPanel panelListaClientes;
    private JPanel panelVoltar;
    private JPanel panelPrincipal;
    private JButton botaoCadastro;
    private JButton botaoEdicao;
    private JButton botaoDelecao;
    private JTable tabela;

    public ClienteView() throws HeadlessException {
        this.botaoCadastro = new JButton(Constantes.CADASTRAR_CLIENTE);
        this.panelAcoes = new JPanel();
        this.panelListaClientes = new JPanel();
        this.panelVoltar = new JPanel();
        this.panelPrincipal = new JPanel();
        this.tabela = new JTable();
        this.botaoEdicao = new JButton(Constantes.EDITAR);
        this.botaoDelecao = new JButton(Constantes.DELETAR);
    }

    public void ativar() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void desativar() {
        this.setVisible(false);
    }

    public void renderizar(JButton voltar) throws Exception {
        this.setTitle(Constantes.TELA_CLIENTES);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setSize(700, 700);
        this.setLocationRelativeTo(null);

        this.panelPrincipal.setLayout(new GridBagLayout());
        this.panelListaClientes.setLayout(new GridBagLayout());
        this.panelAcoes.setLayout(new GridBagLayout());
        this.panelVoltar.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.weightx = 1;
        constraints.weighty = .25;
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.BOTH;


        this.panelAcoes.add(Constantes.ACOES, constraints);
        this.botarAcoesNosBotoes();
        this.panelAcoes.add(this.botaoCadastro, constraints);
        this.panelAcoes.add(this.botaoEdicao, constraints);
        this.panelAcoes.add(this.botaoDelecao, constraints);

        this.add(this.panelAcoes);

        this.panelListaClientes.add(new JLabel("Listagem de Clientes"), constraints);
        this.criarTabelaClientes();
        this.panelListaClientes.add(new JScrollPane(this.tabela), constraints);

        this.panelVoltar.add(voltar, constraints);

        this.panelPrincipal.add(this.panelAcoes, constraints);
        this.panelPrincipal.add(this.panelListaClientes, constraints);
        this.panelPrincipal.add(this.panelVoltar, constraints);
        this.add(this.panelPrincipal);
    }

    private void botarAcoesNosBotoes() {
        this.botaoCadastro.addActionListener(
                cadastrar -> {
                    JTextField nome = new JTextField();
                    Object[] message = {
                            NOME, nome,
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, Constantes.CADASTRAR_CLIENTE, JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (!nome.getText().isEmpty()) {
                            try {
                                LocadoraService.getInstance().createCliente(nome.getText());
                                this.criarTabelaClientes();
                            } catch (Exception ex) {
                                logger.log(SEVERE, Constantes.EXCECAO, ex);
                            }
                        } else {
                            JOptionPane.showMessageDialog(new JFrame(), Constantes.EM_BRANCO, Constantes.ERRO,
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
        );

        this.botaoDelecao.addActionListener(
                deletar -> {
                    JTextField id = new JTextField();
                    Object[] message = {
                            "Digite o ID:", id,
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, Constantes.DELETAR, JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (!id.getText().isEmpty()) {
                            try {
                                int idToInteger = Integer.parseInt(id.getText());
                                boolean cliente = LocadoraService.getInstance().hasCliente(idToInteger);
                                if (cliente) {
                                    LocadoraService.getInstance().deleteCliente(idToInteger);
                                    this.criarTabelaClientes();
                                } else {
                                    JOptionPane.showMessageDialog(new JFrame(), Constantes.NAO_EXISTE, Constantes.ERRO,
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (Exception ex) {
                                logger.log(SEVERE, Constantes.EXCECAO, deletar);
                            }
                        } else {
                            JOptionPane.showMessageDialog(new JFrame(), Constantes.EM_BRANCO, Constantes.ERRO,
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
        );

        this.botaoEdicao.addActionListener(
                editar -> {
                    int linhaSelecionada = this.tabela.getSelectedRow();
                    if (linhaSelecionada < 0) {
                        JOptionPane.showMessageDialog(new JFrame(), Constantes.SELECIONE, Constantes.ERRO,
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        Integer id = (Integer) this.tabela.getModel().getValueAt(linhaSelecionada, 0);
                        Cliente cliente = null;
                        try {
                            cliente = LocadoraService.getInstance().findCliente(id);
                        } catch (Exception e) {
                            logger.log(SEVERE, Constantes.EXCECAO, e);
                        }
                        JLabel idLabel = new JLabel("" + id);
                        JTextField nomeCliente = new JTextField(cliente.getNome());
                        Object[] message = {
                                ID, idLabel,
                                NOME, nomeCliente,
                        };

                        int option = JOptionPane.showConfirmDialog(null, message, Constantes.CADASTRAR_CLIENTE, JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            if (!nomeCliente.getText().isEmpty()) {
                                try {
                                    LocadoraService.getInstance().editCliente(id, nomeCliente.getText());
                                    this.criarTabelaClientes();
                                } catch (Exception ex) {
                                    logger.log(SEVERE, Constantes.EXCECAO, ex);
                                }
                            } else {
                                JOptionPane.showMessageDialog(new JFrame(), Constantes.EM_BRANCO, Constantes.ERRO,
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                }
        );
    }

    public void criarTabelaClientes() throws Exception {
        String[] colunas = {Constantes.ID, NOME};
        Collection<Cliente> clientes = LocadoraService.getInstance().getClientes();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(colunas);
        clientes.forEach(
                cliente -> model.addRow(new Object[]{cliente.getIdCliente(), cliente.getNome()})
        );
        this.tabela.setModel(model);
    }
}
