package co.edu.udistrital.model.enums;

public enum ImpactoMovilidad {
    BAJO(1),
    MEDIO(3),
    ALTO(5);

    private final int peso;

    ImpactoMovilidad(int peso) {
        this.peso = peso;
    }

    public int getPeso() {
        return peso;
    }
}
