package co.edu.udistrital.model.daos;

import co.edu.udistrital.model.entities.Cliente;
import co.edu.udistrital.model.structures.SimpleLinkedList;
import co.edu.udistrital.model.util.GestorArchivosBinarios;

public final class ClienteDAO {

    private static final String FILE_PATH = "clientes.dat";
    private final GestorArchivosBinarios<SimpleLinkedList<Cliente>> binaryManager;
    private SimpleLinkedList<Cliente> clientes;

    public ClienteDAO() {
        this.binaryManager = new GestorArchivosBinarios<>();
        this.clientes = binaryManager.cargarDatos(FILE_PATH);

        if (this.clientes == null) {
            this.clientes = new SimpleLinkedList<>();
        }
    }

    public void add(Cliente cliente) {
        clientes.addLast(cliente);
        saveAll();
    }

    public Cliente findById(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public Cliente findByIdentificador(String identificador) {
        for (Cliente c : clientes) {
            if (c.getIdentificador().equals(identificador)) {
                return c;
            }
        }
        return null;
    }

    public SimpleLinkedList<Cliente> getAll() {
        return this.clientes;
    }

    public void update() {
        saveAll();
    }

    private void saveAll() {
        binaryManager.guardarDatos(FILE_PATH, clientes);
    }
}