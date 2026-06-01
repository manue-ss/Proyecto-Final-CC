/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.udistrital.model;

import java.time.LocalDateTime;

/**
 *
 * @author acurr
 */
public class Kit {
    private int id;
    private TipoKit tipo;
    private EstadoKit estado;
    private LocalDateTime fecha_hora;

    public Kit() {
    }

    public Kit(int id, TipoKit tipo, EstadoKit estado, LocalDateTime fecha_hora) {
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
        this.fecha_hora = fecha_hora;
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

    public LocalDateTime getFecha_hora() {
        return fecha_hora;
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

    public void setFecha_hora(LocalDateTime fecha_hora) {
        this.fecha_hora = fecha_hora;
    }
    
    
}
