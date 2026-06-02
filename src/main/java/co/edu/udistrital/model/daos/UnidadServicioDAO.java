package co.edu.udistrital.model.daos;

import co.edu.udistrital.model.entities.UnidadServicio;
import co.edu.udistrital.model.enums.EstadoUnidad;
import co.edu.udistrital.model.structures.DoubleLinkedList;
import co.edu.udistrital.model.util.GestorArchivosBinarios;

public class UnidadServicioDAO {

    private DoubleLinkedList<UnidadServicio> unidades;
    private final GestorArchivosBinarios<DoubleLinkedList<UnidadServicio>> gestorBinario;
    private final String rutaArchivo = "unidades.dat";

    public UnidadServicioDAO() {
        this.gestorBinario = new GestorArchivosBinarios<>();
        this.unidades = gestorBinario.cargarDatos(rutaArchivo);

        if (this.unidades == null) {
            this.unidades = new DoubleLinkedList<>();
        }
    }

    public void agregar(UnidadServicio unidad) {
        unidades.addLast(unidad);
        guardarRespaldos();
    }

    public DoubleLinkedList<UnidadServicio> obtenerDisponibles() {
        DoubleLinkedList<UnidadServicio> disponibles = new DoubleLinkedList<>();

        for (UnidadServicio u : unidades) {
            if (u.isDisponibilidad() && u.getEstado() == EstadoUnidad.DISPONIBLE) {
                disponibles.addLast(u);
            }
        }
        return disponibles;
    }

    public UnidadServicio buscarPorId(String uuid) {
        for (UnidadServicio u : unidades) {
            if (u.getUuid().equals(uuid)) {
                return u;
            }
        }
        return null;
    }

    public void actualizarEstado(UnidadServicio unidadModificada) {
        UnidadServicio existente = buscarPorId(unidadModificada.getUuid());

        if (existente != null) {
            existente.setEstado(unidadModificada.getEstado());
            existente.setDisponibilidad(unidadModificada.isDisponibilidad());
            guardarRespaldos();
        }
    }

    private void guardarRespaldos() {
        gestorBinario.guardarDatos(rutaArchivo, unidades);
    }

}
