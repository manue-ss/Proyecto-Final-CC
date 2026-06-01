package co.edu.udistrital.model.enumeraciones;

public enum TipoCliente {
    PARTICULAR(1),
    ASEGURADORA(3),
    EMPRESA_TRANSPORTE(5);

    private final int peso;

    TipoCliente(int peso) {
        this.peso = peso;
    }

    public int getPeso() {
        return peso;
    }
}
