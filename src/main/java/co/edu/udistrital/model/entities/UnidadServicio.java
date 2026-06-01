package co.edu.udistrital.model.entities;

import co.edu.udistrital.model.enums.TipoUnidad;
import co.edu.udistrital.model.enums.EstadoUnidad;
import java.util.UUID;
import java.io.Serializable;

public class UnidadServicio implements Serializable {
    
    private static final long serialVersionUID = 1L;    
    
    private String uuid;
    private TipoUnidad tipo;
    private EstadoUnidad estado;
    private String zona;
    private boolean disponibilidad;

    public UnidadServicio() {
    }

    public UnidadServicio(TipoUnidad tipo, EstadoUnidad estado, String zona, boolean disponibilidad) {
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

    public String getZona() {
        return zona;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public void setTipo(TipoUnidad tipo) {
        this.tipo = tipo;
    }

    public void setEstado(EstadoUnidad estado) {
        this.estado = estado;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

}
