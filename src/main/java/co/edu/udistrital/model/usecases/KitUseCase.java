package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.daos.KitDAO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.EstadoKit;
import co.edu.udistrital.model.enums.TipoKit;
import co.edu.udistrital.model.structures.SimpleLinkedList;

public class KitUseCase {

    private final KitDAO dao;

    public KitUseCase(KitDAO dao) {
        this.dao = dao;
    }

    public Kit registrarNuevoKit(TipoKit tipo) {
        int nuevoId = 1;
        while (dao.getById(nuevoId) != null) {
            nuevoId++;
        }

        Kit nuevoKit = new Kit(nuevoId, tipo, EstadoKit.DISPONIBLE);
        dao.addKit(nuevoKit);

        return nuevoKit;
    }

    public void repararKitEnBodega() {
        Kit kitReparado = dao.popMaintenanceKit();
        if (kitReparado == null) {
            throw new IllegalStateException("No hay kits en la pila de mantenimiento para reparar.");
        }
        dao.finishMaintenance(kitReparado);
    }

    public SimpleLinkedList<Kit> obtenerTodos() {
        return dao.getAll();
    }

    public SimpleLinkedList<Kit> obtenerPorEstado(EstadoKit estado) {
        SimpleLinkedList<Kit> filtrados = new SimpleLinkedList<>();
        for (Kit k : dao.getAll()) {
            if (k.getEstado() == estado) {
                filtrados.addLast(k);
            }
        }
        return filtrados;
    }
}
