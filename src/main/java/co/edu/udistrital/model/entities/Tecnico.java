package co.edu.udistrital.model.entities;

import java.io.Serializable;
import co.edu.udistrital.model.enums.EstadoTecnico;

public class Tecnico implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String especialidad;
    private EstadoTecnico estado;
    private String zona;
    private boolean disponibilidad;

    public Tecnico() {
    }

    public int getId() {
        return id;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public EstadoTecnico getEstado() {
        return estado;
    }

    public String getZona() {
        return zona;
    }

    public boolean isDisponible() {
        this.disponibilidad = this.estado == EstadoTecnico.DISPONIBLE;
        return this.disponibilidad;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setEstado(EstadoTecnico estado) {
        this.estado = estado;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

}
