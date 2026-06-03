package co.edu.udistrital.model.entities;

import java.io.Serializable;
import co.edu.udistrital.model.enums.TipoKit;
import co.edu.udistrital.model.enums.EstadoKit;

public class Kit implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private TipoKit tipo;
    private EstadoKit estado;

    public Kit() {
    }

    public Kit(int id, TipoKit tipo, EstadoKit estado) {
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public TipoKit getTipo() {
        return tipo;
    }

    public EstadoKit getEstado() {
        return estado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipo(TipoKit tipo) {
        this.tipo = tipo;
    }

    public void setEstado(EstadoKit estado) {
        this.estado = estado;
    }

}
