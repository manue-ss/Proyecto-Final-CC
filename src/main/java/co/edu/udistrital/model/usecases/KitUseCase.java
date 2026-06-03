package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.daos.KitDAO;
import co.edu.udistrital.model.daos.OperacionDAO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.entities.Operacion;
import co.edu.udistrital.model.enums.EstadoKit;
import co.edu.udistrital.model.enums.TipoKit;
import co.edu.udistrital.model.enums.TipoOperacion;
import co.edu.udistrital.model.structures.SimpleLinkedList;
import co.edu.udistrital.model.structures.Stack;

public class KitUseCase {

    private final KitDAO dao;
    private final OperacionDAO operacionDAO;

    public KitUseCase(KitDAO dao, OperacionDAO operacionDAO) {
        this.dao = dao;
        this.operacionDAO = operacionDAO;
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
        
        String idOperacion = "OP-" + (operacionDAO.getHistory().size() + 1);
        Operacion opReparado = new Operacion(idOperacion, TipoOperacion.KIT_REPARADO,
                "Kit reparado y disponible: " + kitReparado.getId(), null, null, null, kitReparado.getId());
        operacionDAO.registerOperation(opReparado);
    }

    public SimpleLinkedList<Kit> obtenerTodos() {
        return dao.getAll();
    }

    public Stack<Kit> obtenerPilaDisponibles() {
        return dao.getReadyStack();
    }

    public Stack<Kit> obtenerPilaMantenimiento() {
        return dao.getMaintenanceStack();
    }
}
