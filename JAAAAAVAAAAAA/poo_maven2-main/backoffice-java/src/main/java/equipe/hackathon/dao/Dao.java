package equipe.hackathon.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class Dao {
    public static final String URL = "jdbc:mysql://localhost:3306/db_eventos?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private Connection connection;

    public Dao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexão estabelecida com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro na conexão: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public abstract Boolean update(Object entity);
}