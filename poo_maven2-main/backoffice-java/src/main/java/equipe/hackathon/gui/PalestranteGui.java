package equipe.hackathon.gui;

import equipe.hackathon.model.Palestrante;
import equipe.hackathon.service.PalestranteService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PalestranteGui extends JFrame {
    private JTextField tfId;
    private JTextField tfNome;
    private JTextField tfTema;
    private JTextField tfFoto;
    private JTextArea taMiniCurriculo;
    private JTable tbPalestrantes;

    private final PalestranteService service = new PalestranteService();

    public PalestranteGui() {
        setTitle("Cadastro de Palestrantes");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        carregarPalestrantes();
    }

    private void initComponents() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // Painel de formulário
        JPanel painelForm = new JPanel(new GridBagLayout());
        GuiUtils guiUtils = new GuiUtils();

        tfId = new JTextField(10);
        tfId.setEditable(false);
        tfNome = new JTextField(20);
        tfTema = new JTextField(20);
        tfFoto = new JTextField(30);
        taMiniCurriculo = new JTextArea(5, 30);
        JScrollPane scrollCurriculo = new JScrollPane(taMiniCurriculo);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(this::salvarPalestrante);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(_ -> limparCampos());

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(this::excluirPalestrante);

        // Adicionando componentes ao painel de formulário
        painelForm.add(new JLabel("ID:"), guiUtils.montarConstraints(0, 0));
        painelForm.add(tfId, guiUtils.montarConstraints(1, 0));

        painelForm.add(new JLabel("Nome:"), guiUtils.montarConstraints(0, 1));
        painelForm.add(tfNome, guiUtils.montarConstraints(1, 1));

        painelForm.add(new JLabel("Tema:"), guiUtils.montarConstraints(0, 2));
        painelForm.add(tfTema, guiUtils.montarConstraints(1, 2));

        painelForm.add(new JLabel("Foto:"), guiUtils.montarConstraints(0, 3));
        painelForm.add(tfFoto, guiUtils.montarConstraints(1, 3));

        painelForm.add(new JLabel("Descrição:"), guiUtils.montarConstraints(0, 4));
        painelForm.add(scrollCurriculo, guiUtils.montarConstraints(1, 4));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnExcluir);

        painelForm.add(painelBotoes, guiUtils.montarConstraints(0, 5, 2, 1));

        // Tabela de palestrantes
        tbPalestrantes = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Nome", "Tema"}, 0
        ));
        tbPalestrantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbPalestrantes.getSelectionModel().addListSelectionListener(this::selecionarPalestrante);

        JScrollPane scrollPane = new JScrollPane(tbPalestrantes);

        // Adicionando ao painel principal
        painelPrincipal.add(painelForm, BorderLayout.NORTH);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        add(painelPrincipal);
    }

    private void carregarPalestrantes() {
        DefaultTableModel model = (DefaultTableModel) tbPalestrantes.getModel();
        model.setRowCount(0);

        service.listarPalestrantes().forEach(palestrante -> model.addRow(new Object[]{
                palestrante.getId(),
                palestrante.getNome(),
                palestrante.getTema()
        }));
    }

    private void salvarPalestrante(ActionEvent event) {
        try {
            Palestrante palestrante = new Palestrante();

            if (!tfId.getText().isEmpty()) {
                palestrante.setId(Integer.parseInt(tfId.getText()));
            }

            palestrante.setNome(tfNome.getText());
            palestrante.setTema(tfTema.getText());
            palestrante.setFoto(tfFoto.getText());
            palestrante.setDescricao(taMiniCurriculo.getText());

            boolean sucesso;
            if (palestrante.getId() == 0) {
                sucesso = service.cadastrarPalestrante(palestrante);
            } else {
                sucesso = service.atualizarPalestrante(palestrante);
            }

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Palestrante salvo com sucesso!");
                limparCampos();
                carregarPalestrantes();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar palestrante", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void excluirPalestrante(ActionEvent event) {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um palestrante para excluir", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este palestrante?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean sucesso = service.removerPalestrante(Long.parseLong(tfId.getText()));
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Palestrante excluído com sucesso!");
                limparCampos();
                carregarPalestrantes();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir palestrante", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparCampos() {
        tfId.setText("");
        tfNome.setText("");
        tfTema.setText("");
        tfFoto.setText("");
        taMiniCurriculo.setText("");
    }

    private void selecionarPalestrante(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            int linha = tbPalestrantes.getSelectedRow();
            if (linha >= 0) {
                Long id = Long.parseLong(tbPalestrantes.getValueAt(linha, 0).toString());
                Palestrante palestrante = service.buscarPalestrantePorId(id);
                if (palestrante != null) {
                    tfId.setText(String.valueOf(palestrante.getId()));
                    tfNome.setText(palestrante.getNome());
                    tfTema.setText(palestrante.getTema());
                    tfFoto.setText(palestrante.getFoto());
                    taMiniCurriculo.setText(palestrante.getDescricao());
                }
            }
        }
    }
}