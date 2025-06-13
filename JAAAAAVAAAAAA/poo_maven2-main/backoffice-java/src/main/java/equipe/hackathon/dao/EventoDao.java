package equipe.hackathon.dao;

import equipe.hackathon.model.Evento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDao extends Dao implements DaoInterface {

    @Override
    public Boolean insert(Object entity) {
        try {
            Evento evento = (Evento) entity;
            String sql = "INSERT INTO eventos(titulo, descricao, data, hora, curso, lugar, foto_url, palestrante_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, evento.getTitulo());
            ps.setString(2, evento.getDescricao());
            ps.setDate(3, java.sql.Date.valueOf(evento.getData()));
            ps.setTime(4, java.sql.Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getCurso());
            ps.setString(6, evento.getLugar());
            ps.setString(7, evento.getFotoUrl());
            ps.setInt(8, evento.getPalestranteId());

            int result = ps.executeUpdate();

            // Obter o ID gerado
            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    evento.setId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao inserir evento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean uptade(Object entity) {
        return null;
    }

    // Método removido pois já existe o update()

    @Override
    public Boolean update(Object entity) {
        try {
            Evento evento = (Evento) entity;
            String sql = "UPDATE eventos SET titulo=?, descricao=?, data=?, hora=?, curso=?, lugar=?, foto_url=?, palestrante_id=? WHERE id=?";

            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, evento.getTitulo());
            ps.setString(2, evento.getDescricao());
            ps.setDate(3, java.sql.Date.valueOf(evento.getData()));
            ps.setTime(4, java.sql.Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getCurso());
            ps.setString(6, evento.getLugar());
            ps.setString(7, evento.getFotoUrl());
            ps.setInt(8, evento.getPalestranteId());
            ps.setInt(9, evento.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar evento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean delete(Long pk) {
        try {
            String sql = "DELETE FROM eventos WHERE id=?";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setLong(1, pk);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erro ao deletar evento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Object> select(Long pk) {
        List<Object> eventos = new ArrayList<>();
        try {
            String sql = "SELECT * FROM eventos WHERE id=?";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setLong(1, pk);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventos.add(mapearEvento(rs));
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar evento: " + e.getMessage());
        }
        return eventos;
    }

    @Override
    public List<Object> selectALL() {
        List<Object> eventos = new ArrayList<>();
        try {
            String sql = "SELECT * FROM eventos";
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                eventos.add(mapearEvento(rs));
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar eventos: " + e.getMessage());
        }
        return eventos;
    }

    private Evento mapearEvento(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setId(rs.getInt("id"));
        evento.setTitulo(rs.getString("titulo"));
        evento.setDescricao(rs.getString("descricao"));
        evento.setData(rs.getDate("data").toLocalDate());
        evento.setHora(rs.getTime("hora").toLocalTime());
        evento.setCurso(rs.getString("curso"));
        evento.setLugar(rs.getString("lugar"));
        evento.setFotoUrl(rs.getString("foto_url"));
        evento.setPalestranteId(rs.getInt("palestrante_id"));
        return evento;
    }
}