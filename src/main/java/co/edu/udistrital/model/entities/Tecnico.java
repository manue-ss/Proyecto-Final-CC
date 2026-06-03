package co.edu.udistrital.model.entities;

import co.edu.udistrital.model.enums.Especialidades;
import java.io.Serializable;
import co.edu.udistrital.model.enums.EstadoTecnico;
import co.edu.udistrital.model.enums.Zonas;

public class Tecnico implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private EstadoTecnico estado;
    private Especialidades especialidad;
    private Zonas zona;
    private boolean disponibilidad;

    public Tecnico() {
    }

    public int getId() {
        return id;
    }

    public Especialidades getEspecialidad() {
        return especialidad;
    }

    public EstadoTecnico getEstado() {
        return estado;
    }

    public Zonas getZona() {
        return zona;
    }

    public boolean isDisponible() {
        this.disponibilidad = this.estado == EstadoTecnico.DISPONIBLE;
        return this.disponibilidad;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEstado(EstadoTecnico estado) {
        this.estado = estado;
    }

    public void setEspecialidad(Especialidades especialidad) {
        this.especialidad = especialidad;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

}
