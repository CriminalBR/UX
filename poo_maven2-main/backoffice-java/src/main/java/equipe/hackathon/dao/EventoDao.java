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
            PreparedStatement ps = getConnection().prepareStatement(getInsertQuery(), Statement.RETURN_GENERATED_KEYS);
            setInsertParameters(ps, evento);

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
                eventos.add(createEntityFromResultSet(rs));
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
                eventos.add(createEntityFromResultSet(rs));
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar eventos: " + e.getMessage());
        }
        return eventos;
    }
}