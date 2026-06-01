package co.edu.udistrital.model;

import co.edu.udistrital.model.EstadoTecnico;

public class Tecnico {

    private int id;
    private String especialidad;
    private EstadoTecnico estado;
    private String zona;
    private boolean disponibilidad;

    public Tecnico() {
    }

    public Tecnico(int id, String especialidad, EstadoTecnico estado, String zona, boolean disponibilidad) {
        this.id = id;
        this.especialidad = especialidad;
        this.estado = estado;
        this.zona = zona;
        this.disponibilidad = disponibilidad;
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

    public boolean isDisponivilidad() {
        return disponibilidad;
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

    public void setDisponivilidad(boolean disponivilidad) {
        this.disponibilidad = disponivilidad;
    }

}
