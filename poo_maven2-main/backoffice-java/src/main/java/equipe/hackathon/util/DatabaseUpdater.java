package equipe.hackathon.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUpdater {
    private static final String URL = "jdbc:mysql://localhost:3306/db_eventos?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void updateSchema() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Verifica se a coluna 'foto' já existe na tabela 'eventos'
            boolean columnExists = false;
            try {
                stmt.executeQuery("SELECT foto FROM eventos LIMIT 1");
                columnExists = true;
            } catch (SQLException e) {
                // Coluna não existe, vamos criá-la
            }

            if (!columnExists) {
                // Adiciona a coluna 'foto' se não existir
                stmt.executeUpdate("ALTER TABLE eventos ADD COLUMN foto VARCHAR(255) AFTER palestrante_id");
                System.out.println("Coluna 'foto' adicionada à tabela 'eventos'");
            } else {
                // Atualiza o tipo da coluna se ela já existir
                stmt.executeUpdate("ALTER TABLE eventos MODIFY COLUMN foto VARCHAR(255)");
                System.out.println("Tipo da coluna 'foto' atualizado para VARCHAR(255)");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o esquema do banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
