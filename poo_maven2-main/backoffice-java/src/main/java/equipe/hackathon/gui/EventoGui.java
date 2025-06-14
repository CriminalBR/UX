package equipe.hackathon.gui;

import equipe.hackathon.model.Evento;
import equipe.hackathon.model.Palestrante;
import equipe.hackathon.service.EventoService;
import equipe.hackathon.service.PalestranteService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EventoGui extends JFrame {
    private JTextField tfId;
    private JTextField tfTitulo;
    private JTextField tfDescricao;
    private JTextField tfData;
    private JTextField tfHora;
    private JTextField tfCurso;
    private JTextField tfLugar;
    private JTextField tfFoto;
    private JComboBox<Palestrante> cbPalestrantes;
    private JTable tbEventos;

    private final EventoService eventoService = new EventoService();
    private final PalestranteService palestranteService = new PalestranteService();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public EventoGui() {
        setTitle("Cadastro de Eventos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        carregarPalestrantes();
        carregarEventos();
    }

    private void initComponents() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // Painel de formulário
        JPanel painelForm = new JPanel(new GridBagLayout());
        GuiUtils guiUtils = new GuiUtils();

        tfId = new JTextField(10);
        tfId.setEditable(false);
        tfTitulo = new JTextField(20);
        tfDescricao = new JTextField(20);
        tfData = new JTextField(10);
        tfHora = new JTextField(5);
        tfCurso = new JTextField(15);
        tfLugar = new JTextField(20);
        tfFoto = new JTextField(30);
        cbPalestrantes = new JComboBox<>();

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(this::salvarEvento);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(_ -> limparCampos());

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(this::excluirEvento);

        // Adicionando componentes ao painel de formulário
        painelForm.add(new JLabel("ID:"), guiUtils.montarConstraints(0, 0));
        painelForm.add(tfId, guiUtils.montarConstraints(1, 0));

        painelForm.add(new JLabel("Título:"), guiUtils.montarConstraints(0, 1));
        painelForm.add(tfTitulo, guiUtils.montarConstraints(1, 1));

        painelForm.add(new JLabel("Descrição:"), guiUtils.montarConstraints(0, 2));
        painelForm.add(tfDescricao, guiUtils.montarConstraints(1, 2));

        painelForm.add(new JLabel("Data:"), guiUtils.montarConstraints(0, 3));
        painelForm.add(tfData, guiUtils.montarConstraints(1, 3));

        painelForm.add(new JLabel("Hora:"), guiUtils.montarConstraints(0, 4));
        painelForm.add(tfHora, guiUtils.montarConstraints(1, 4));

        painelForm.add(new JLabel("Curso:"), guiUtils.montarConstraints(0, 5));
        painelForm.add(tfCurso, guiUtils.montarConstraints(1, 5));

        painelForm.add(new JLabel("Lugar:"), guiUtils.montarConstraints(0, 6));
        painelForm.add(tfLugar, guiUtils.montarConstraints(1, 6));

        painelForm.add(new JLabel("Foto:"), guiUtils.montarConstraints(0, 7));
        painelForm.add(tfFoto, guiUtils.montarConstraints(1, 7));

        painelForm.add(new JLabel("Palestrante:"), guiUtils.montarConstraints(0, 8));
        painelForm.add(cbPalestrantes, guiUtils.montarConstraints(1, 8));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnExcluir);

        painelForm.add(painelBotoes, guiUtils.montarConstraints(0, 8, 2, 1));

        // Tabela de eventos
        tbEventos = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Título", "Data", "Hora", "Curso", "Lugar", "Palestrante"}, 0
        ));
        tbEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbEventos.getSelectionModel().addListSelectionListener(this::selecionarEvento);

        JScrollPane scrollPane = new JScrollPane(tbEventos);

        // Adicionando ao painel principal
        painelPrincipal.add(painelForm, BorderLayout.NORTH);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        add(painelPrincipal);
    }

    private void carregarPalestrantes() {
        cbPalestrantes.removeAllItems();
        palestranteService.listarPalestrantes().forEach(cbPalestrantes::addItem);
    }

    private void carregarEventos() {
        DefaultTableModel model = (DefaultTableModel) tbEventos.getModel();
        model.setRowCount(0);

        eventoService.listarEventos().forEach(evento -> {
            Palestrante palestrante = palestranteService.buscarPalestrantePorId((long) evento.getPalestranteId());
            String nomePalestrante = palestrante != null ? palestrante.getNome() : "N/A";

            model.addRow(new Object[]{
                    evento.getId(),
                    evento.getTitulo(),
                    evento.getData().format(dateFormatter),
                    evento.getHora().format(timeFormatter),
                    evento.getCurso(),
                    evento.getLugar(),
                    nomePalestrante
            });
        });
    }

    private void salvarEvento(ActionEvent event) {
        try {
            Evento evento = new Evento();

            if (!tfId.getText().isEmpty()) {
                evento.setId(Long.parseLong(tfId.getText()));
            }

            evento.setTitulo(tfTitulo.getText());
            evento.setDescricao(tfDescricao.getText());
            evento.setData(LocalDate.parse(tfData.getText(), dateFormatter));
            evento.setHora(LocalTime.parse(tfHora.getText(), timeFormatter));
            evento.setCurso(tfCurso.getText());
            evento.setLugar(tfLugar.getText());
            evento.setFoto(tfFoto.getText());

            Palestrante palestrante = (Palestrante) cbPalestrantes.getSelectedItem();
            if (palestrante != null) {
                evento.setPalestranteId(palestrante.getId());
            }

            boolean sucesso;
            if (evento.getId() == 0) {
                sucesso = eventoService.cadastrarEvento(evento);
            } else {
                sucesso = eventoService.atualizarEvento(evento);
            }

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Evento salvo com sucesso!");
                limparCampos();
                carregarEventos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar evento", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void excluirEvento(ActionEvent event) {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para excluir", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este evento?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean sucesso = eventoService.removerEvento(Long.parseLong(tfId.getText()));
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Evento excluído com sucesso!");
                limparCampos();
                carregarEventos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir evento", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void selecionarEvento(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            int linha = tbEventos.getSelectedRow();
            if (linha >= 0) {
                Long id = Long.parseLong(tbEventos.getValueAt(linha, 0).toString());
                Evento evento = eventoService.buscarEventoPorId(id);
                if (evento != null) {
                    preencherFormulario(evento);
                }
            }
        }
    }

    private void preencherFormulario(Evento evento) {
        tfId.setText(String.valueOf(evento.getId()));
        tfTitulo.setText(evento.getTitulo());
        tfDescricao.setText(evento.getDescricao());
        tfData.setText(evento.getData() != null ? dateFormatter.format(evento.getData()) : "");
        tfHora.setText(evento.getHora() != null ? timeFormatter.format(evento.getHora()) : "");
        tfCurso.setText(evento.getCurso());
        tfLugar.setText(evento.getLugar());
        tfFoto.setText(evento.getFoto());

        // Seleciona o palestrante correspondente
        for (int i = 0; i < cbPalestrantes.getItemCount(); i++) {
            if (cbPalestrantes.getItemAt(i).getId() == evento.getPalestranteId()) {
                cbPalestrantes.setSelectedIndex(i);
                break;
            }
        }
    }

    private void atualizarPreviewImagem(String fotoUrl) {

    }

    private void limparCampos() {
        tfId.setText("");
        tfTitulo.setText("");
        tfDescricao.setText("");
        tfData.setText("");
        tfHora.setText("");
        tfCurso.setText("");
        tfLugar.setText("");
        tfFoto.setText("");
        cbPalestrantes.setSelectedIndex(-1);
    }
}