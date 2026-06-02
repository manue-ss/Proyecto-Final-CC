package co.edu.udistrital.model.daos;

import co.edu.udistrital.model.entities.Tecnico;
import co.edu.udistrital.model.enums.EstadoTecnico;
import co.edu.udistrital.model.structures.DoubleLinkedList;
import co.edu.udistrital.model.util.GestorArchivosBinarios;

public final class TecnicoDAO {

    private static final String FILE_PATH = "tecnicos.dat";

    private final GestorArchivosBinarios<DoubleLinkedList<Tecnico>> binaryManager;

    private DoubleLinkedList<Tecnico> technicians;

    public TecnicoDAO() {
        this.binaryManager = new GestorArchivosBinarios<>();
        this.technicians = binaryManager.cargarDatos(FILE_PATH);

        if (this.technicians == null) {
            this.technicians = new DoubleLinkedList<>();
        }
    }

    public void add(Tecnico technician) {
        technicians.addLast(technician);
        saveAll();
    }

    public DoubleLinkedList<Tecnico> getAvailable() {
        DoubleLinkedList<Tecnico> availableList = new DoubleLinkedList<>();

        for (Tecnico t : technicians) {
            if (t.isDisponible()) {
                availableList.addLast(t);
            }
        }
        return availableList;
    }

    public Tecnico findById(int id) {
        for (Tecnico t : technicians) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public void update() {
        saveAll();
    }

    public void softDelete(int id) {
        Tecnico t = findById(id);
        if (t != null) {

            t.setEstado(EstadoTecnico.RETIRADO);
            saveAll();
        }
    }

    public void hardDelete(int id) {
        Tecnico t = findById(id);
        if (t != null) {

            technicians.remove(t);
            saveAll();
        }
    }

    public DoubleLinkedList<Tecnico> getAll() {
        return this.technicians;
    }

    private void saveAll() {
        binaryManager.guardarDatos(FILE_PATH, technicians);
    }
}
