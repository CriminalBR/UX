package equipe.hackathon.model;

public class Palestrante {
    private int id;
    private String nome;
    private String tema;
    private String fotoUrl;
    private String miniCurriculo;

    public Palestrante() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String getMiniCurriculo() { return miniCurriculo; }
    public void setMiniCurriculo(String miniCurriculo) { this.miniCurriculo = miniCurriculo; }

    @Override
    public String toString() {
        return nome + " - " + tema;
    }
}