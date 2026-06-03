package co.edu.udistrital.model.entities;

import co.edu.udistrital.model.enums.TipoUnidad;
import co.edu.udistrital.model.enums.EstadoUnidad;
import co.edu.udistrital.model.enums.Zonas;
import java.util.UUID;
import java.io.Serializable;

public class UnidadServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;
    private TipoUnidad tipo;
    private EstadoUnidad estado;
    private Zonas zona;
    private boolean disponibilidad;

    public UnidadServicio() {
    }

    public UnidadServicio(TipoUnidad tipo, EstadoUnidad estado, Zonas zona, boolean disponibilidad) {
        this.uuid = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.estado = estado;
        this.zona = zona;
        this.disponibilidad = disponibilidad;
    }

    public String getUuid() {
        return uuid;
    }

    public TipoUnidad getTipo() {
        return tipo;
    }

    public EstadoUnidad getEstado() {
        return estado;
    }

    public boolean isDisponibilidad() {
        this.disponibilidad = (this.estado == EstadoUnidad.DISPONIBLE);
        return this.disponibilidad;
    }

    public void setUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public void setTipo(TipoUnidad tipo) {
        this.tipo = tipo;
    }

    public void setEstado(EstadoUnidad estado) {
        this.estado = estado;
        this.disponibilidad = (this.estado == EstadoUnidad.DISPONIBLE);
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Zonas getZona() {
        return zona;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

}
