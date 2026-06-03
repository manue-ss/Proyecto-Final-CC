package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.daos.OperacionDAO;
import co.edu.udistrital.model.daos.UnidadServicioDAO;
import co.edu.udistrital.model.entities.Operacion;
import co.edu.udistrital.model.entities.UnidadServicio;
import co.edu.udistrital.model.enums.EstadoUnidad;
import co.edu.udistrital.model.enums.TipoOperacion;
import co.edu.udistrital.model.enums.TipoUnidad;
import co.edu.udistrital.model.structures.DoubleLinkedList;

public class UnidadUseCase {

    private final UnidadServicioDAO dao;
    private final OperacionDAO operacionDAO;

    public UnidadUseCase(UnidadServicioDAO dao, OperacionDAO operacionDAO) {
        this.dao = dao;
        this.operacionDAO = operacionDAO;
    }

    public UnidadServicio registrarNuevaUnidad(String tipo, String zona) {
        UnidadServicio nuevaUnidad = new UnidadServicio(TipoUnidad.valueOf(tipo), EstadoUnidad.DISPONIBLE, zona, true);
        nuevaUnidad.setEstado(EstadoUnidad.DISPONIBLE);
        dao.add(nuevaUnidad);
        return nuevaUnidad;
    }

    public void enviarUnidadAMantenimiento(String uuid) {
        UnidadServicio us = dao.findByUuid(uuid);
        if (us != null) {
            if (us.getEstado() != EstadoUnidad.DISPONIBLE) {
                throw new IllegalStateException("La unidad no se puede enviar a mantenimiento porque está " + us.getEstado());
            }

            us.setEstado(EstadoUnidad.EN_MANTENIMIENTO);
            dao.update();

            String idOperacion = "OP-" + (operacionDAO.getHistory().size() + 1);
            Operacion opMantenimiento = new Operacion(idOperacion, TipoOperacion.UNIDAD_A_MANTENIMIENTO,
                    "Unidad enviada a mantenimiento: " + uuid, null, null, uuid, null);
            operacionDAO.registerOperation(opMantenimiento);
        }
    }

    public void liberarUnidadDeMantenimiento(String uuid) {
        UnidadServicio us = dao.findByUuid(uuid);
        if (us == null) {
            throw new IllegalArgumentException("Error: La unidad especificada no existe en el sistema.");
        }

        if (us.getEstado() != EstadoUnidad.EN_MANTENIMIENTO) {
            throw new IllegalStateException("La unidad no puede ser liberada porque su estado actual es: " + us.getEstado());
        }

        us.setEstado(EstadoUnidad.DISPONIBLE);
        dao.update();
        
        String idOperacion = "OP-" + (operacionDAO.getHistory().size() + 1);
        Operacion opLibera = new Operacion(idOperacion, TipoOperacion.UNIDAD_LIBERADA,
                "Unidad liberada de mantenimiento: " + uuid, null, null, uuid, null);
        operacionDAO.registerOperation(opLibera);
    }

    public void darDeBajaUnidad(String uuid) {
        UnidadServicio us = dao.findByUuid(uuid);
        if (us == null) {
            throw new IllegalArgumentException("Error: La unidad especificada no existe en el sistema.");
        }
        
        if (us.getEstado() == EstadoUnidad.OCUPADA) {
            throw new IllegalStateException("No se puede dar de baja la unidad porque actualmente está atendiendo una emergencia.");
        }

        us.setEstado(EstadoUnidad.INACTIVA);
        dao.update();
    }

    public DoubleLinkedList<UnidadServicio> obtenerUnidadesPorEstado(EstadoUnidad estado) {
        return dao.getByState(estado);
    }

    public DoubleLinkedList<UnidadServicio> obtenerTodas() {
        return dao.getAll();
    }
}
