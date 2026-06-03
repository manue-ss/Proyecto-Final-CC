package co.edu.udistrital.model.daos;

import co.edu.udistrital.model.entities.UnidadServicio;
import co.edu.udistrital.model.enums.EstadoUnidad;
import co.edu.udistrital.model.structures.DoubleLinkedList;
import co.edu.udistrital.model.util.GestorArchivosBinarios;

public final class UnidadServicioDAO {

    private static final String FILE_NAME = "unidades.dat";

    private final GestorArchivosBinarios<DoubleLinkedList<UnidadServicio>> binaryManager;

    private DoubleLinkedList<UnidadServicio> units;

    public UnidadServicioDAO() {
        this.binaryManager = new GestorArchivosBinarios<>();
        this.units = binaryManager.cargarDatos(FILE_NAME);

        if (this.units == null) {
            this.units = new DoubleLinkedList<>();
        }
    }

    public void add(UnidadServicio unit) {
        units.addLast(unit);
        saveAll();
    }

    public DoubleLinkedList<UnidadServicio> getByState(EstadoUnidad targetState) {
        DoubleLinkedList<UnidadServicio> filteredList = new DoubleLinkedList<>();

        for (UnidadServicio u : units) {
            if (u.getEstado() == targetState) {
                filteredList.addLast(u);
            }
        }
        return filteredList;
    }

    public UnidadServicio findByUuid(String uuid) {
        for (UnidadServicio u : units) {
            if (u.getUuid().equals(uuid)) {
                return u;
            }
        }
        return null;
    }

    public void update() {
        saveAll();
    }

    public void softDelete(String uuid) {
        UnidadServicio u = findByUuid(uuid);
        if (u != null) {
            u.setEstado(EstadoUnidad.INACTIVA);
            saveAll();
        }
    }

    public void hardDelete(String uuid) {
        UnidadServicio u = findByUuid(uuid);
        if (u != null) {
            units.remove(u);
            saveAll();
        }
    }

    public DoubleLinkedList<UnidadServicio> getAll() {
        return this.units;
    }

    private void saveAll() {
        binaryManager.guardarDatos(FILE_NAME, units);
    }
}
