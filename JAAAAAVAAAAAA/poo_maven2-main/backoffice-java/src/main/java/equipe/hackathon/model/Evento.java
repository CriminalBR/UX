package equipe.hackathon.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento {
    private int id;
    private String titulo;
    private String descricao;
    private LocalDate data;
    private LocalTime hora;
    private String curso;
    private String lugar;
    private String fotoUrl;
    private int palestranteId;

    public Evento() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public int getPalestranteId() {
        return palestranteId;
    }
    public void setPalestranteId(int palestranteId) { this.palestranteId = palestranteId; }
}