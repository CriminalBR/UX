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
    private JTextField tfFotoUrl;
    private JButton btnSelecionarImagem;
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
        tfFotoUrl = new JTextField(30);
        tfFotoUrl.setEditable(false);
        btnSelecionarImagem = new JButton("Selecionar Imagem");
        btnSelecionarImagem.addActionListener(this::selecionarImagem);
        cbPalestrantes = new JComboBox<>();

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(this::salvarEvento);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());

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

        painelForm.add(new JLabel("Imagem:"), guiUtils.montarConstraints(0, 7));
        JPanel painelImagem = new JPanel(new BorderLayout(5, 0));
        painelImagem.add(tfFotoUrl, BorderLayout.CENTER);
        painelImagem.add(btnSelecionarImagem, BorderLayout.EAST);
        painelForm.add(painelImagem, guiUtils.montarConstraints(1, 7));

        painelForm.add(new JLabel("Palestrante:"), guiUtils.montarConstraints(0, 8));
        painelForm.add(cbPalestrantes, guiUtils.montarConstraints(1, 8));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnExcluir);

        painelForm.add(painelBotoes, guiUtils.montarConstraints(0, 9, 2, 1));

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
                evento.setId(Integer.parseInt(tfId.getText()));
            }

            evento.setTitulo(tfTitulo.getText());
            evento.setDescricao(tfDescricao.getText());
            LocalDate data = LocalDate.parse(tfData.getText(), dateFormatter);
            LocalTime hora = LocalTime.parse(tfHora.getText(), timeFormatter);
            evento.setData(data);
            evento.setHora(hora);
            evento.setCurso(tfCurso.getText());
            evento.setLugar(tfLugar.getText());
            evento.setFotoUrl(tfFotoUrl.getText());

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
            // Remove a imagem associada ao evento se existir
            String currentImage = tfFotoUrl.getText();
            if (currentImage != null && !currentImage.isEmpty()) {
                FileUploadUtil.removeFile(currentImage);
            }
            
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
        if (event.getValueIsAdjusting()) {
            return;
        }

        int linhaSelecionada = tbEventos.getSelectedRow();
        if (linhaSelecionada < 0) {
            return;
        }

        try {
            // Obtém o ID do evento selecionado
            Integer idEvento = (Integer) tbEventos.getValueAt(linhaSelecionada, 0);
            if (idEvento == null) {
                return;
            }

            // Busca os dados completos do evento
            Evento evento = eventoService.buscarEventoPorId(idEvento.longValue());
            if (evento == null) {
                JOptionPane.showMessageDialog(this, 
                    "Não foi possível carregar os detalhes do evento selecionado.",
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Preenche os campos do formulário
            preencherFormulario(evento);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar evento: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void preencherFormulario(Evento evento) {
        // Limpa campos antes de preencher
        limparCampos();
        
        // Preenche campos básicos
        tfId.setText(String.valueOf(evento.getId()));
        tfTitulo.setText(evento.getTitulo());
        tfDescricao.setText(evento.getDescricao());
        
        // Formata e preenche data e hora
        if (evento.getData() != null) {
            tfData.setText(evento.getData().format(dateFormatter));
        }
        if (evento.getHora() != null) {
            tfHora.setText(evento.getHora().format(timeFormatter));
        }
        
        tfCurso.setText(evento.getCurso());
        tfLugar.setText(evento.getLugar() != null ? evento.getLugar() : "");
        tfFotoUrl.setText(evento.getFotoUrl() != null ? evento.getFotoUrl() : "");
        
        // Seleciona o palestrante correspondente
        selecionarPalestranteNoCombo(evento.getPalestranteId());
    }
    
    private void selecionarPalestranteNoCombo(int palestranteId) {
        if (palestranteId <= 0) {
            cbPalestrantes.setSelectedIndex(-1);
            return;
        }
        
        for (int i = 0; i < cbPalestrantes.getItemCount(); i++) {
            Palestrante p = cbPalestrantes.getItemAt(i);
            if (p != null && p.getId() == palestranteId) {
                cbPalestrantes.setSelectedIndex(i);
                return;
            }
        }
        
        // Se não encontrou o palestrante, limpa a seleção
        cbPalestrantes.setSelectedIndex(-1);
    }

    private void selecionarImagem(ActionEvent event) {
        String filePath = FileUploadUtil.uploadFile(this);
        if (filePath != null) {
            // Remove a imagem anterior se existir
            String currentImage = tfFotoUrl.getText();
            if (currentImage != null && !currentImage.isEmpty()) {
                FileUploadUtil.removeFile(currentImage);
            }
            tfFotoUrl.setText(filePath);
        }
    }

    private void limparCampos() {
        // Remove a imagem associada ao evento se existir
        String currentImage = tfFotoUrl.getText();
        if (currentImage != null && !currentImage.isEmpty()) {
            FileUploadUtil.removeFile(currentImage);
        }
        
        tfId.setText("");
        tfTitulo.setText("");
        tfDescricao.setText("");
        tfData.setText("");
        tfHora.setText("");
        tfCurso.setText("");
        tfLugar.setText("");
        tfFotoUrl.setText("");
        cbPalestrantes.setSelectedIndex(-1);
    }
}