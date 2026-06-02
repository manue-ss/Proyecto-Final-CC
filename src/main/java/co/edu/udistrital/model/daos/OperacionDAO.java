package co.edu.udistrital.model.daos;

import co.edu.udistrital.model.entities.Operacion;
import co.edu.udistrital.model.structures.Stack;
import co.edu.udistrital.model.util.GestorArchivosBinarios;

public final class OperacionDAO {

    private static final String FILE_PATH = "historial_operaciones.dat";
    private final GestorArchivosBinarios<Stack<Operacion>> binaryManager;

    private Stack<Operacion> historyStack;

    public OperacionDAO() {
        this.binaryManager = new GestorArchivosBinarios<>();
        this.historyStack = binaryManager.cargarDatos(FILE_PATH);

        if (this.historyStack == null) {
            this.historyStack = new Stack<>();
        }
    }

    public void registerOperation(Operacion op) {
        historyStack.push(op);
        saveAll();
    }

    public Operacion popLastOperation() {
        if (!historyStack.isEmpty()) {
            Operacion last = historyStack.pop();
            saveAll();
            return last;
        }
        return null;
    }
    public Stack<Operacion> getHistory() {
        return this.historyStack;
    }

    private void saveAll() {
        binaryManager.guardarDatos(FILE_PATH, historyStack);
    }
}
