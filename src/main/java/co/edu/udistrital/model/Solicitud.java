/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.udistrital.model;
import co.edu.udistrital.model.Cliente;
import java.time.LocalDateTime;
import java.util.UUID;
/**
 *
 * @author acurr
 */
public class Solicitud {
    private int id;
    private int idCliente;
    private String uuid;
    private int idTecnico;
    private String descripcion;
    private TipoSolicitud tipo;
    private NivelCriticidad criticidad;
    private EstadoSolicitud estado;
    private LocalDateTime fecha_hora;

    public Solicitud() {
    }
    
    public Solicitud(int id, int idCliente, int idTecnico, String descripcion, TipoSolicitud tipo, NivelCriticidad criticidad, EstadoSolicitud estado , LocalDateTime fecha_hora) {
        this.id = id;
        this.idCliente = idCliente;
        this.uuid = UUID.randomUUID().toString();
        this.idTecnico = idTecnico;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.criticidad = criticidad;
        this.estado = estado;
        this.fecha_hora = fecha_hora;
    }

    public int getId() {
        return id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getUuid() {
        return uuid;
    }

    public int getIdTecnico() {
        return idTecnico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoSolicitud getTipo() {
        return tipo;
    }

    public NivelCriticidad getCriticidad() {
        return criticidad;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public LocalDateTime getFecha_hora() {
        return fecha_hora;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipo(TipoSolicitud tipo) {
        this.tipo = tipo;
    }

    public void setCriticidad(NivelCriticidad criticidad) {
        this.criticidad = criticidad;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public void setFecha_hora(LocalDateTime fecha_hora) {
        this.fecha_hora = fecha_hora;
    }
    
    
}
