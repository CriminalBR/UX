package equipe.hackathon.dao;

import equipe.hackathon.model.Evento;

import java.sql.*;

public class Dao {
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

    protected String getInsertQuery() {
        return "INSERT INTO eventos (titulo, descricao, data, hora, curso, lugar, palestrante_id, foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    protected void setInsertParameters(PreparedStatement stmt, Evento evento) throws SQLException {
        stmt.setString(1, evento.getTitulo());
        stmt.setString(2, evento.getDescricao());
        stmt.setDate(3, evento.getData() != null ? Date.valueOf(evento.getData()) : null);
        stmt.setTime(4, evento.getHora() != null ? Time.valueOf(evento.getHora()) : null);
        stmt.setString(5, evento.getCurso());
        stmt.setString(6, evento.getLugar());
        stmt.setInt(7, evento.getPalestranteId());
        stmt.setString(8, evento.getFoto());
    }

    protected String getUpdateQuery() {
        return "UPDATE eventos SET titulo = ?, descricao = ?, data = ?, hora = ?, curso = ?, lugar = ?, palestrante_id = ?, foto = ? WHERE id = ?";
    }

    protected void setUpdateParameters(PreparedStatement stmt, Evento evento) throws SQLException {
        setInsertParameters(stmt, evento);
        stmt.setLong(9, evento.getId());
    }

    public Boolean update(Object entity) {
        try {
            Evento evento = (Evento) entity;
            PreparedStatement ps = getConnection().prepareStatement(getUpdateQuery());
            setUpdateParameters(ps, evento);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar evento: " + e.getMessage());
            return false;
        }
    }

    protected Evento createEntityFromResultSet(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setId(rs.getLong("id"));
        evento.setTitulo(rs.getString("titulo"));
        evento.setDescricao(rs.getString("descricao"));

        Date data = rs.getDate("data");
        evento.setData(data != null ? data.toLocalDate() : null);

        Time hora = rs.getTime("hora");
        evento.setHora(hora != null ? hora.toLocalTime() : null);

        evento.setCurso(rs.getString("curso"));
        evento.setLugar(rs.getString("lugar"));
        evento.setPalestranteId(rs.getInt("palestrante_id"));
        evento.setFoto(rs.getString("foto"));

        return evento;
    }
}