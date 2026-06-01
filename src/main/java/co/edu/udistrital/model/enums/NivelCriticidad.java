package co.edu.udistrital.model.enums;

public enum NivelCriticidad {
    SIN_PRIORIDAD(0),
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
