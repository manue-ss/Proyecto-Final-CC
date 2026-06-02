package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.daos.*;
import co.edu.udistrital.model.entities.*;
import co.edu.udistrital.model.enums.*;
import java.util.Objects;

public class SolicitudUseCase {

    private final SolicitudDAO solicitudDAO;
    private final TecnicoDAO tecnicoDAO;
    private final UnidadServicioDAO unidadDAO;
    private final KitDAO kitDAO;
    private final OperacionDAO operacionDAO;

    public SolicitudUseCase(SolicitudDAO solicitudDAO, TecnicoDAO tecnicoDAO,
            UnidadServicioDAO unidadDAO, KitDAO kitDAO, OperacionDAO operacionDAO) {
        this.solicitudDAO = solicitudDAO;
        this.tecnicoDAO = tecnicoDAO;
        this.unidadDAO = unidadDAO;
        this.kitDAO = kitDAO;
        this.operacionDAO = operacionDAO;
    }

    public Solicitud crearSolicitud(int idCliente, String descripcion, TipoSolicitud tipo, NivelCriticidad criticidad) {
        int nuevoId = solicitudDAO.getFullHistory().size() + 1;

        Solicitud nuevaSolicitud = new Solicitud(
                nuevoId,
                idCliente,
                null,
                0,
                descripcion,
                tipo,
                criticidad,
                EstadoSolicitud.PENDIENTE
        );

        solicitudDAO.registerRequest(nuevaSolicitud);
        return nuevaSolicitud;
    }

    public void despacharServicio(int idSolicitud, int idTecnico, String uuidUnidad, boolean requiereKit) {

        // 1. Recuperar las referencias REALES de la memoria usando los DAOs
        Solicitud solicitud = solicitudDAO.getById(idSolicitud);
        Tecnico tecnico = tecnicoDAO.findById(idTecnico);
        UnidadServicio unidad = unidadDAO.findByUuid(uuidUnidad);

        // 2. Validaciones de seguridad
        if (solicitud == null || tecnico == null || unidad == null) {
            throw new IllegalArgumentException("Uno de los recursos seleccionados no existe en el sistema.");
        }
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue atendida o está en ejecución.");
        }
        if (tecnico.getEstado() != EstadoTecnico.DISPONIBLE) {
            throw new IllegalStateException("El técnico seleccionado no está disponible.");
        }
        if (unidad.getEstado() != EstadoUnidad.DISPONIBLE) {
            throw new IllegalStateException("La unidad de servicio no está disponible.");
        }

        // 3. Extracción del Kit si se requiere
        Kit kitAsignado = null;
        if (requiereKit) {
            kitAsignado = kitDAO.popReadyKit();
            if (kitAsignado == null) {
                throw new IllegalStateException("No hay kits de herramientas disponibles en bodega.");
            }
        }

        // 4. Modificamos los estados y enlazamos TODO dentro de la Solicitud
        Integer idKit = (kitAsignado != null) ? kitAsignado.getId() : null;

        solicitud.setEstado(EstadoSolicitud.EN_EJECUCION);
        solicitud.setIdTecnico(tecnico.getId());
        solicitud.setUuid(unidad.getUuid());
        solicitud.setIdKit(idKit);

        tecnico.setEstado(EstadoTecnico.ASIGNADO);
        unidad.setEstado(EstadoUnidad.OCUPADA);

        // 5. Persistencia
        solicitudDAO.update();
        tecnicoDAO.update();
        unidadDAO.update();

        // 6. Registro Atómico para el Deshacer
        String idOperacion = "OP-" + (operacionDAO.getHistory().size() + 1);
        Operacion opDespacho = new Operacion(
                idOperacion, TipoOperacion.ASIGNACION_SERVICIO,
                "Asignación Atómica: Solicitud " + solicitud.getId(),
                solicitud.getId(), tecnico.getId(), unidad.getUuid(), idKit
        );
        operacionDAO.registerOperation(opDespacho);
    }

    public void finalizarSolicitud(int idSolicitud, boolean kitDanado) {

        Solicitud solicitud = solicitudDAO.getById(idSolicitud);

        if (solicitud == null || solicitud.getEstado() != EstadoSolicitud.EN_EJECUCION) {
            throw new IllegalStateException("La solicitud no existe o no está en ejecución.");
        }

        Tecnico tecnico = tecnicoDAO.findById(solicitud.getIdTecnico());
        UnidadServicio unidad = unidadDAO.findByUuid(solicitud.getUuid());

        solicitud.setEstado(EstadoSolicitud.ATENDIDA); // O CERRADA

        if (tecnico != null) {
            tecnico.setEstado(EstadoTecnico.DISPONIBLE);
        }
        if (unidad != null) {
            unidad.setEstado(EstadoUnidad.DISPONIBLE);
        }

        if (!Objects.isNull(solicitud.getIdKit())) {

            Kit kitUsado = kitDAO.getById(solicitud.getIdKit());

            if (kitUsado != null) {
                kitDAO.returnFromService(kitUsado, kitDanado);
            }
        }

        solicitudDAO.update();
        tecnicoDAO.update();
        unidadDAO.update();

        String idOperacion = "OP-" + (operacionDAO.getHistory().size() + 1);
        Operacion opFin = new Operacion(
                idOperacion, TipoOperacion.CIERRE_SERVICIO,
                "Cierre Atómico: Solicitud " + solicitud.getId() + " finalizada.",
                solicitud.getId(), solicitud.getIdTecnico(),
                solicitud.getUuid(), solicitud.getIdKit()
        );
        operacionDAO.registerOperation(opFin);
    }
}
