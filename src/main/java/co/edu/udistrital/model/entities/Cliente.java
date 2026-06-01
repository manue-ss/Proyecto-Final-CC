package co.edu.udistrital.model.entities;
import java.io.Serializable;
import co.edu.udistrital.model.enums.TipoCliente;

public class Cliente implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private TipoCliente tipo;

    public Cliente () {
    }

    public Cliente(int id, String nombre, TipoCliente tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(TipoCliente tipo) {
        this.tipo = tipo;
    }

}
