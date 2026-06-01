package co.edu.udistrital.model;

public enum NivelCriticidad {
    ORDINARIA(1),
    MEDIA(3),
    CRITICA(5);

    private final int nivel;

    NivelCriticidad(int nivel) {
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }
}
