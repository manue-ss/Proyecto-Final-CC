package co.edu.udistrital.model.entities;

import co.edu.udistrital.model.enums.TipoOperacion;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Operacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idOperacion;
    private LocalDateTime fechaHora;
    private TipoOperacion tipo;
    private String descripcionUI;

    private Integer idSolicitud;
    private Integer idTecnico;
    private String uuidUnidad;
    private Integer idKit;

    public Operacion(String idOperacion, TipoOperacion tipo, String descripcionUI,
            Integer idSolicitud, Integer idTecnico, String uuidUnidad, Integer idKit) {
        this.idOperacion = idOperacion;
        this.fechaHora = LocalDateTime.now();
        this.tipo = tipo;
        this.descripcionUI = descripcionUI;
        this.idSolicitud = idSolicitud;
        this.idTecnico = idTecnico;
        this.uuidUnidad = uuidUnidad;
        this.idKit = idKit;
    }

    public String getDescripcionUI() {
        return descripcionUI;
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public Integer getIdTecnico() {
        return idTecnico;
    }

    public String getUuidUnidad() {
        return uuidUnidad;
    }

    public Integer getIdKit() {
        return idKit;
    }

    public TipoOperacion getTipo() {
        return tipo;
    }
}
