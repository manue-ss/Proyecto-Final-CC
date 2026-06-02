package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.daos.ClienteDAO;
import co.edu.udistrital.model.entities.Cliente;
import co.edu.udistrital.model.enums.TipoCliente;
import co.edu.udistrital.model.structures.SimpleLinkedList;

public class ClienteUseCase {

    private final ClienteDAO dao;

    public ClienteUseCase(ClienteDAO dao) {
        this.dao = dao;
    }

    public Cliente registrarNuevoCliente(String nombre, String identificador, TipoCliente tipo) {
        if (dao.findByIdentificador(identificador) != null) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con ese identificador.");
        }

        int nuevoId = 1;
        while (dao.findById(nuevoId) != null) {
            nuevoId++;
        }

        Cliente nuevoCliente = new Cliente(nuevoId, nombre, identificador, tipo);
        dao.add(nuevoCliente);
        return nuevoCliente;
    }


    public SimpleLinkedList<Cliente> obtenerTodos() {
        return dao.getAll();
    }
}
