package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.daos.OperacionDAO;
import co.edu.udistrital.model.daos.TecnicoDAO;
import co.edu.udistrital.model.entities.Operacion;
import co.edu.udistrital.model.entities.Tecnico;
import co.edu.udistrital.model.enums.EstadoTecnico;
import co.edu.udistrital.model.enums.TipoOperacion;

public class TecnicoUseCase {

    private final TecnicoDAO dao;
    private final OperacionDAO operacionDAO;

    public TecnicoUseCase(TecnicoDAO dao, OperacionDAO operacionDAO) {
        this.dao = dao;
        this.operacionDAO = operacionDAO;
    }

    public Tecnico registrarNuevoTecnico(String especialidad, String zona) {
        int nuevoId = 1;

        while (dao.findById(nuevoId) != null) {
            nuevoId++;
        }

        Tecnico nuevoTecnico = new Tecnico();
        nuevoTecnico.setId(nuevoId);
        nuevoTecnico.setEspecialidad(especialidad);
        nuevoTecnico.setZona(zona);
        nuevoTecnico.setEstado(EstadoTecnico.DISPONIBLE);

        dao.add(nuevoTecnico);

        return nuevoTecnico;
    }

    public void enviarTecnicoADescanso(int id) {
        Tecnico tecnico = dao.findById(id);

        if (tecnico == null) {
            throw new IllegalArgumentException("El técnico especificado no existe en el sistema.");
        }

        tecnico.setEstado(EstadoTecnico.EN_DESCANSO);
        dao.update();

        String idOperacion = "OP-" + (operacionDAO.getHistory().size() + 1);
        Operacion opMantenimiento = new Operacion(
                idOperacion,
                TipoOperacion.MANTENIMIENTO_RECURSO,
                "Unidad enviada a mantenimiento: " + id,
                null,
                id,
                null,
                null
        );
        operacionDAO.registerOperation(opMantenimiento);
    }

    public void retornarTecnicoDeDescanso(int id) {
        Tecnico tecnico = dao.findById(id);

        if (tecnico == null) {
            throw new IllegalArgumentException("El técnico especificado no existe en el sistema.");
        }

        tecnico.setEstado(EstadoTecnico.DISPONIBLE);
        dao.update();
    }

    public void despedirTecnico(int id) {
        Tecnico tecnico = dao.findById(id);

        if (tecnico == null) {
            throw new IllegalArgumentException("El técnico especificado no existe en el sistema.");
        }

        tecnico.setEstado(EstadoTecnico.RETIRADO);
        dao.update();
    }
}
