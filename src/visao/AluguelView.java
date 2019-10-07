package visao;

import entidades.Aluguel;
import recursos.Constantes;
import servico.LocadoraService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static recursos.Constantes.*;
import static servico.LocadoraService.getInstance;

public class AluguelView extends JFrame {
    private static final long serialVersionUID = -4672259171010305192L;
    private static Logger logger = Logger.getLogger(AluguelView.class.getName());

    private JPanel panelAcoes;
    private JPanel panelListaAlugueis;
    private JPanel panelVoltar;
    private JPanel panelPrincipal;
    private JButton botaoCadastro;
    private JButton botaoEdicao;
    private JButton botaoDelecao;
    private JTable tabela;


    public AluguelView() throws HeadlessException {
        this.botaoCadastro = new JButton(Constantes.CADASTRAR_ALUGUEL);
        this.panelAcoes = new JPanel();
        this.panelListaAlugueis = new JPanel();
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
        this.setTitle(Constantes.TELA_ALUGUEIS);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(700, 700);

        this.panelPrincipal.setLayout(new GridBagLayout());
        this.panelListaAlugueis.setLayout(new GridBagLayout());
        this.panelVoltar.setLayout(new GridBagLayout());
        this.panelAcoes.setLayout(new GridBagLayout());

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

        this.panelListaAlugueis.add(new JLabel("Listagem de Aluguéis"), constraints);
        this.criarTabelaAlugueis();
        this.panelListaAlugueis.add(new JScrollPane(this.tabela), constraints);

        this.panelVoltar.add(voltar, constraints);

        this.panelPrincipal.add(this.panelAcoes, constraints);
        this.panelPrincipal.add(this.panelListaAlugueis, constraints);
        this.panelPrincipal.add(this.panelVoltar, constraints);
        this.add(this.panelPrincipal);
    }

    private void botarAcoesNosBotoes() {
        this.botaoCadastro.addActionListener(
                cadastrar -> {
                    JTextField data = new JTextField();
                    JTextField valor = new JTextField();
                    JComboBox cliente = new JComboBox(getInstance().getOpcaoClientes());
                    JList filmes = new JList(getInstance().getOpcaoFilmes());
                    filmes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    Object[] message = {
                            DATA + EX_DATA, data,
                            VALOR + "ex: 15.50", valor,
                            NOME, cliente,
                            FILMES, new JScrollPane(filmes),
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, CADASTRAR_FILME, JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (!data.getText().isEmpty() && !valor.getText().isEmpty() && cliente.getSelectedItem() != null && !filmes.getSelectedValuesList().isEmpty()) {
                            try {
                                getInstance().createAluguel(cliente.getSelectedItem().toString(), data.getText(), valor.getText(), filmes.getSelectedValuesList());
                                this.criarTabelaAlugueis();
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
                            DIGITE_O_ID, id,
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, Constantes.DELETAR, JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (!id.getText().isEmpty()) {
                            try {
                                int idToInteger = Integer.parseInt(id.getText());
                                boolean alugel = getInstance().hasAlugel(idToInteger);
                                if (alugel) {
                                    getInstance().deleteAluguel(idToInteger);
                                    this.criarTabelaAlugueis();
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
                        Aluguel aluguel = null;
                        Integer id = (Integer) this.tabela.getModel().getValueAt(linhaSelecionada, 0);
                        try {
                            aluguel = LocadoraService.getInstance().findAluguel(id);
                        } catch (Exception e) {
                            logger.log(SEVERE, Constantes.EXCECAO, e);
                        }

                        JLabel idLabelAluguel = new JLabel("" + id);
                        JTextField data = LocadoraService.getInstance().getTextData(aluguel.getDataAluguel());
                        JTextField valor = new JTextField(aluguel.getValor() + "");
                        JComboBox cliente = new JComboBox(getInstance().getOpcaoClientes());
                        JList filmes = new JList(getInstance().getOpcaoFilmes());
                        filmes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                        Object[] message = {
                                ID, idLabelAluguel,
                                DATA + EX_DATA, data,
                                VALOR + "ex: 15.50", valor,
                                NOME, cliente,
                                FILMES, new JScrollPane(filmes),
                        };

                        int option = JOptionPane.showConfirmDialog(null, message, Constantes.CADASTRAR_CLIENTE, JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            if (!data.getText().isEmpty() && !valor.getText().isEmpty() && cliente.getSelectedItem() != null && !filmes.getSelectedValuesList().isEmpty()) {
                                try {
                                    LocadoraService.getInstance().editAlugel(id, data.getText(), valor.getText(), cliente.getSelectedItem(), filmes.getSelectedValuesList());
                                    this.criarTabelaAlugueis();
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

    private void criarTabelaAlugueis() throws Exception {
        String[] colunas = {Constantes.ID, Constantes.DATA, Constantes.VALOR, Constantes.NOME, Constantes.FILMES};
        Collection<Aluguel> alugueis = getInstance().getAlugueis();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(colunas);
        alugueis.forEach(
                aluguel -> model.addRow(new Object[]{
                        aluguel.getIdAluguel(),
                        new SimpleDateFormat("dd/MM/yyyy").format(aluguel.getDataAluguel()),
                        NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(aluguel.getValor()),
                        aluguel.getCliente().getNome(),
                        aluguel.getNomesFilmes()})
        );
        this.tabela.setModel(model);
        this.tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        this.tabela.getColumnModel().getColumn(1).setPreferredWidth(100);
        this.tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
        this.tabela.getColumnModel().getColumn(3).setPreferredWidth(100);
        this.tabela.getColumnModel().getColumn(4).setPreferredWidth(400);
    }

}