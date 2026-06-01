/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.udistrital.model;

import java.util.UUID;

/**
 *
 * @author acurr
 */
public class UnidadServicio {
    private String uuid;
    private TipoUnidad tipo;
    private EstadoUnidad estado;
    private String zona;
    private boolean disponibilidad;

    public UnidadServicio() {
    }

    public UnidadServicio( TipoUnidad tipo, EstadoUnidad estado, String zona, boolean disponibilidad) {
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

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
