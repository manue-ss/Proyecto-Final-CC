/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.udistrital.model;

/**
 *
 * @author acurr
 */
public class Tecnico {
    private int id;
    private String especialidad;
    private String estado;
    private String zona;
    private boolean disponivilidad;

    public Tecnico(int id, String especialidad, String estado, String zona, boolean disponivilidad) {
        this.id = id;
        this.especialidad = especialidad;
        this.estado = estado;
        this.zona = zona;
        this.disponivilidad = disponivilidad;
    }

    public int getId() {
        return id;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getEstado() {
        return estado;
    }

    public String getZona() {
        return zona;
    }

    public boolean isDisponivilidad() {
        return disponivilidad;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public void setDisponivilidad(boolean disponivilidad) {
        this.disponivilidad = disponivilidad;
    }
    
     
    
}
