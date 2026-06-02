package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.daos.OperacionDAO;
import co.edu.udistrital.model.daos.UnidadServicioDAO;
import co.edu.udistrital.model.entities.Operacion;
import co.edu.udistrital.model.entities.UnidadServicio;
import co.edu.udistrital.model.enums.EstadoUnidad;
import co.edu.udistrital.model.enums.TipoOperacion;
import co.edu.udistrital.model.enums.TipoUnidad;

public class UnidadUseCase {

    private final UnidadServicioDAO dao;
    private final OperacionDAO operacionDAO;

    public UnidadUseCase(UnidadServicioDAO dao, OperacionDAO operacionDAO) {
        this.dao = dao;
        this.operacionDAO = operacionDAO;
    }

    public UnidadServicio registrarNuevaUnidad(String tipo, String zona) {

        UnidadServicio nuevaUnidad = new UnidadServicio(TipoUnidad.valueOf(tipo),
                EstadoUnidad.DISPONIBLE, zona, true);
        nuevaUnidad.setEstado(EstadoUnidad.DISPONIBLE);

        dao.add(nuevaUnidad);

        return nuevaUnidad;
    }

    public void enviarUnidadAMantenimiento(String uuid) {
        UnidadServicio us = dao.findByUuid(uuid);

        if (us == null) {
            throw new IllegalArgumentException("Error: La unidad especificada no existe en el sistema.");
        }

        us.setEstado(EstadoUnidad.EN_MANTENIMIENTO);
        dao.update();

        String idOperacion = "OP-" + (operacionDAO.getHistory().size() + 1);
        Operacion opMantenimiento = new Operacion(
                idOperacion,
                TipoOperacion.MANTENIMIENTO_RECURSO,
                "Unidad enviada a mantenimiento: " + uuid,
                null,
                null,
                uuid,
                null
        );
        operacionDAO.registerOperation(opMantenimiento);
    }

    public void liberarUnidadDeMantenimiento(String uuid) {
        UnidadServicio us = dao.findByUuid(uuid);

        if (us == null) {
            throw new IllegalArgumentException("Error: La unidad especificada no existe en el sistema.");
        }

        us.setEstado(EstadoUnidad.DISPONIBLE);
        dao.update();
    }

    public void darDeBajaUnidad(String uuid) {
        UnidadServicio us = dao.findByUuid(uuid);

        if (us == null) {
            throw new IllegalArgumentException("Error: La unidad especificada no existe en el sistema.");
        }

        us.setEstado(EstadoUnidad.INACTIVA);
        dao.update();
    }
}
