package equipe.hackathon;

import equipe.hackathon.gui.EventoGui;
import equipe.hackathon.gui.PalestranteGui;
import equipe.hackathon.util.DatabaseUpdater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {

    public static void main(String[] args) {
        // Atualiza o esquema do banco de dados antes de iniciar a aplicação
        DatabaseUpdater.updateSchema();
        
        // Inicia a interface gráfica
        SwingUtilities.invokeLater(Main::iniciar);
    }

    private static void iniciar() {
        var menuPrincipal = new JFrame("Sistema de Eventos");
        menuPrincipal.setSize(400, 300);
        menuPrincipal.setLocationRelativeTo(null);
        menuPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 1));
        JButton btnEventos = new JButton("Gerenciar Eventos");
        JButton btnPalestrantes = new JButton("Gerenciar Palestrantes");

        btnEventos.addActionListener((ActionEvent e) -> {
            var eventoGui = new EventoGui();
            eventoGui.setVisible(true);
        });

        btnPalestrantes.addActionListener((ActionEvent e) -> {
            var palestranteGui = new PalestranteGui();
            palestranteGui.setVisible(true);
        });

        panel.add(btnEventos);
        panel.add(btnPalestrantes);
        menuPrincipal.add(panel);
        menuPrincipal.setVisible(true);
    }
}