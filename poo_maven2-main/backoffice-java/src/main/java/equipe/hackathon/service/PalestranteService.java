package equipe.hackathon.service;

import equipe.hackathon.dao.PalestranteDao;
import equipe.hackathon.model.Palestrante;

import java.util.ArrayList;
import java.util.List;

public class PalestranteService {

    public boolean cadastrarPalestrante(Palestrante palestrante) {
        PalestranteDao dao = new PalestranteDao();
        return dao.insert(palestrante);
    }

    public boolean atualizarPalestrante(Palestrante palestrante) {
        PalestranteDao dao = new PalestranteDao();
        return dao.update(palestrante);
    }

    public boolean removerPalestrante(Long id) {
        PalestranteDao dao = new PalestranteDao();
        return dao.delete(id);
    }

    public Palestrante buscarPalestrantePorId(Long id) {
        PalestranteDao dao = new PalestranteDao();
        List<Object> palestrantes = dao.select(id);
        return palestrantes.isEmpty() ? null : (Palestrante) palestrantes.get(0);
    }

    public List<Palestrante> listarPalestrantes() {
        PalestranteDao dao = new PalestranteDao();
        List<Object> objetos = dao.selectALL();
        List<Palestrante> palestrantes = new ArrayList<>();

        for (Object obj : objetos) {
            palestrantes.add((Palestrante) obj);
        }
        return palestrantes;
    }
}