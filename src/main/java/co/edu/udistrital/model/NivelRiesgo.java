package co.edu.udistrital.model;

public enum NivelRiesgo {
    BAJO(1),
    MEDIO(3),
    ALTO(5);

    private final int peso;

    NivelRiesgo(int peso) {
        this.peso = peso;
    }

    public int getPeso() {
        return peso;
    }
}
