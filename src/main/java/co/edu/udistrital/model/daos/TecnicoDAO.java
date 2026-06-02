package co.edu.udistrital.model.daos;

import co.edu.udistrital.model.entities.Tecnico;
import co.edu.udistrital.model.enums.EstadoTecnico;
import co.edu.udistrital.model.structures.DoubleLinkedList;
import co.edu.udistrital.model.util.GestorArchivosBinarios;

public class TecnicoDAO {

    private DoubleLinkedList<Tecnico> tecnicos;
    private final GestorArchivosBinarios<DoubleLinkedList<Tecnico>> gestorBinario;
    private static final String rutaArchivo = "tecnicos.dat";

    public TecnicoDAO() {
        this.gestorBinario = new GestorArchivosBinarios<>();
        this.tecnicos = gestorBinario.cargarDatos(rutaArchivo);

        if (this.tecnicos == null) {
            this.tecnicos = new DoubleLinkedList<>();
        }
    }

    public void agregar(Tecnico tecnico) {
        tecnicos.addLast(tecnico);
        guardarRespaldos();
    }

    public DoubleLinkedList<Tecnico> obtenerDisponibles() {
        DoubleLinkedList<Tecnico> disponibles = new DoubleLinkedList<>();

        for (Tecnico t : tecnicos) {
            if (t.isDisponibilidad() && t.getEstado() == EstadoTecnico.DISPONIBLE) {
                disponibles.addLast(t);
            }
        }
        return disponibles;
    }

    public Tecnico buscarPorId(int id) {
        for (Tecnico t : tecnicos) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public void actualizarEstado(Tecnico tecnicoModificado) {
        Tecnico existente = buscarPorId(tecnicoModificado.getId());

        if (existente != null) {
            existente.setEstado(tecnicoModificado.getEstado());
            existente.setDisponibilidad(tecnicoModificado.isDisponibilidad());
            guardarRespaldos();
        }
    }

    private void guardarRespaldos() {
        gestorBinario.guardarDatos(rutaArchivo, tecnicos);
    }
}
