package visao;

import entidades.Filme;
import recursos.Constantes;
import servico.LocadoraService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static recursos.Constantes.*;

public class FilmeView extends JFrame {
    private static Logger logger = Logger.getLogger(FilmeView.class.getName());


    private static final long serialVersionUID = 682096748353793283L;
    private JPanel panelAcoes;
    private JPanel panelListaFilmes;
    private JPanel panelVoltar;
    private JPanel panelPrincipal;
    private JButton botaoCadastro;
    private JButton botaoEdicao;
    private JButton botaoDelecao;
    private JTable tabela;

    public FilmeView() throws HeadlessException {
        this.botaoCadastro = new JButton(Constantes.CADASTRAR_FILME);
        this.panelAcoes = new JPanel();
        this.panelListaFilmes = new JPanel();
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
        this.setTitle(Constantes.TELA_FILMES);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(700, 700);

        this.panelPrincipal.setLayout(new GridBagLayout());
        this.panelListaFilmes.setLayout(new GridBagLayout());
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

        this.panelListaFilmes.add(new JLabel(LISTAGEM_DE_FILMES), constraints);
        this.criarTabelaFilmes();
        this.panelListaFilmes.add(new JScrollPane(this.tabela), constraints);

        this.panelVoltar.add(voltar, constraints);

        this.panelPrincipal.add(this.panelAcoes, constraints);
        this.panelPrincipal.add(this.panelListaFilmes, constraints);
        this.panelPrincipal.add(this.panelVoltar, constraints);
        this.add(this.panelPrincipal);
    }

    private void botarAcoesNosBotoes() {
        this.botaoCadastro.addActionListener(
                cadastrar -> {
                    JTextField nome = new JTextField();
                    JTextField data = new JTextField();
                    JTextField descricao = new JTextField();
                    Object[] message = {
                            NOME, nome,
                            DATA + EX_DATA, data,
                            DESCRICAO, descricao,
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, CADASTRAR_FILME, JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (!nome.getText().isEmpty() && !data.getText().isEmpty() && !descricao.getText().isEmpty() && data.getText().length() == 8) {
                            try {
                                LocadoraService.getInstance().createFilme(nome.getText(), data.getText(), descricao.getText());
                                this.criarTabelaFilmes();
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
                                boolean cliente = LocadoraService.getInstance().hasFilme(idToInteger);
                                if (cliente) {
                                    LocadoraService.getInstance().deleteFilme(idToInteger);
                                    this.criarTabelaFilmes();
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
                        Filme filme = null;
                        try {
                            filme = LocadoraService.getInstance().findFilme(id);
                        } catch (Exception e) {
                            logger.log(SEVERE, Constantes.EXCECAO, e);
                        }
                        JLabel idLabel = new JLabel("" + id);
                        JTextField nomeFilme = new JTextField(filme.getNome());
                        JTextField data = LocadoraService.getInstance().getTextData(filme.getDataLancamento());
                        JTextField descricao = new JTextField(filme.getDescricao());
                        Object[] message = {
                                ID, idLabel,
                                NOME, nomeFilme,
                                DATA + EX_DATA, data,
                                DESCRICAO, descricao,
                        };

                        int option = JOptionPane.showConfirmDialog(null, message, Constantes.CADASTRAR_CLIENTE, JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            if (!nomeFilme.getText().isEmpty() && !data.getText().isEmpty() && data.getText().length() == 8 && !descricao.getText().isEmpty()) {
                                try {
                                    LocadoraService.getInstance().editFilme(id, nomeFilme.getText(), data.getText(), descricao.getText());
                                    this.criarTabelaFilmes();
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

    private void criarTabelaFilmes() throws Exception {
        String[] colunas = {ID, NOME, DATA, DESCRICAO};
        Collection<Filme> filmes = LocadoraService.getInstance().getFilmes();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(colunas);
        filmes.forEach(
                filme -> model.addRow(new Object[]{
                        filme.getIdFilme(),
                        filme.getNome(),
                        new SimpleDateFormat("dd/MM/yyyy").format(filme.getDataLancamento()),
                        filme.getDescricao()
                })
        );
        this.tabela.setModel(model);
    }
}
