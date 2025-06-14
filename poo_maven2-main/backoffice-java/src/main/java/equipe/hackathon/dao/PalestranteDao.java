package equipe.hackathon.dao;

import equipe.hackathon.model.Palestrante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PalestranteDao extends Dao implements DaoInterface {

    @Override
    public Boolean insert(Object entity) {
        try {
            Palestrante palestrante = (Palestrante) entity;
            String sql = "INSERT INTO palestrantes(nome, tema, foto, descricao) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, palestrante.getNome());
            ps.setString(2, palestrante.getTema());
            ps.setString(3, palestrante.getFoto());
            ps.setString(4, palestrante.getDescricao());

            int result = ps.executeUpdate();

            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    palestrante.setId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao inserir palestrante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean uptade(Object entity) {
        return null;
    }

    @Override
    public Boolean update(Object entity) {
        try {
            Palestrante palestrante = (Palestrante) entity;
            String sql = "UPDATE palestrantes SET nome=?, tema=?, foto=?, descricao=? WHERE id=?";

            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, palestrante.getNome());
            ps.setString(2, palestrante.getTema());
            ps.setString(3, palestrante.getFoto());
            ps.setString(4, palestrante.getDescricao());
            ps.setInt(5, palestrante.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar palestrante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean delete(Long pk) {
        try {
            String sql = "DELETE FROM palestrantes WHERE id=?";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setLong(1, pk);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erro ao deletar palestrante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Object> select(Long pk) {
        List<Object> palestrantes = new ArrayList<>();
        try {
            String sql = "SELECT * FROM palestrantes WHERE id=?";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setLong(1, pk);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                palestrantes.add(mapearPalestrante(rs));
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar palestrante: " + e.getMessage());
        }
        return palestrantes;
    }

    @Override
    public List<Object> selectALL() {
        List<Object> palestrantes = new ArrayList<>();
        try {
            String sql = "SELECT * FROM palestrantes";
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                palestrantes.add(mapearPalestrante(rs));
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar palestrantes: " + e.getMessage());
        }
        return palestrantes;
    }

    private Palestrante mapearPalestrante(ResultSet rs) throws SQLException {
        Palestrante palestrante = new Palestrante();
        palestrante.setId(rs.getInt("id"));
        palestrante.setNome(rs.getString("nome"));
        palestrante.setTema(rs.getString("tema"));
        palestrante.setFoto(rs.getString("foto"));
        palestrante.setDescricao(rs.getString("descricao"));
        return palestrante;
    }
}