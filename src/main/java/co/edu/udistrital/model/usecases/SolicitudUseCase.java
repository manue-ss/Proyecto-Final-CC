package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.daos.*;
import co.edu.udistrital.model.entities.*;
import co.edu.udistrital.model.enums.*;
import co.edu.udistrital.model.structures.DoubleLinkedList;
import co.edu.udistrital.model.structures.SimpleLinkedList;
import java.time.LocalDate;

public class SolicitudUseCase {

    private final SolicitudDAO solicitudDAO;
    private final TecnicoDAO tecnicoDAO;
    private final UnidadServicioDAO unidadDAO;
    private final KitDAO kitDAO;
    private final OperacionDAO operacionDAO;
    private final ClienteDAO clienteDAO;

    public SolicitudUseCase(SolicitudDAO solicitudDAO, TecnicoDAO tecnicoDAO,
            UnidadServicioDAO unidadDAO, KitDAO kitDAO, OperacionDAO operacionDAO, ClienteDAO clienteDAO) {
        this.solicitudDAO = solicitudDAO;
        this.tecnicoDAO = tecnicoDAO;
        this.unidadDAO = unidadDAO;
        this.kitDAO = kitDAO;
        this.operacionDAO = operacionDAO;
        this.clienteDAO = clienteDAO;
    }

    public Solicitud registrarSolicitud(String identificadorCliente, String descripcion, TipoSolicitud tipo, NivelCriticidad criticidad) {
        Cliente cliente = clienteDAO.findByIdentificador(identificadorCliente);

        if (cliente == null) {
            throw new IllegalArgumentException("El cliente con identificador " + identificadorCliente + " no se encuentra registrado.");
        }

        int nuevoId = solicitudDAO.getFullHistory().size() + 1;

        Solicitud nuevaSolicitud = new Solicitud(
                nuevoId,
                cliente.getId(),
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

    public void despacharServicio(int idTecnico, String uuidUnidad, boolean requiereKit) {
        Solicitud solicitud = solicitudDAO.pollNextIncident();
        if (solicitud == null) {
            throw new IllegalStateException("No hay solicitudes pendientes en la cola.");
        }

        Tecnico tecnico = tecnicoDAO.findById(idTecnico);
        UnidadServicio unidad = unidadDAO.findByUuid(uuidUnidad);

        if (tecnico == null || unidad == null) {
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

        Kit kitAsignado = null;
        if (requiereKit) {
            kitAsignado = kitDAO.popReadyKit();
            if (kitAsignado == null) {
                throw new IllegalStateException("No hay kits de herramientas disponibles en bodega.");
            }
        }

        int idKit = (kitAsignado != null) ? kitAsignado.getId() : 0;

        solicitud.setEstado(EstadoSolicitud.EN_EJECUCION);
        solicitud.setIdTecnico(tecnico.getId());
        solicitud.setUuid(unidad.getUuid());
        solicitud.setIdKit(idKit);

        tecnico.setEstado(EstadoTecnico.ASIGNADO);
        unidad.setEstado(EstadoUnidad.OCUPADA);

        solicitudDAO.update();
        tecnicoDAO.update();
        unidadDAO.update();

        String idOperacion = "OP-" + (operacionDAO.getHistory().size() + 1);
        Operacion opDespacho = new Operacion(
                idOperacion, TipoOperacion.DESPACHO_SOLICITUD,
                "Asignación Atómica: Solicitud " + solicitud.getId(),
                solicitud.getId(), tecnico.getId(), unidad.getUuid(), idKit
        );
        operacionDAO.registerOperation(opDespacho);
    }

    public void finalizarSolicitudAtendida(int idSolicitud, boolean kitDanado) {
        Solicitud solicitud = solicitudDAO.getById(idSolicitud);

        if (solicitud == null || solicitud.getEstado() != EstadoSolicitud.EN_EJECUCION) {
            throw new IllegalStateException("La solicitud no existe o no está en ejecución.");
        }

        Tecnico tecnico = tecnicoDAO.findById(solicitud.getIdTecnico());
        UnidadServicio unidad = unidadDAO.findByUuid(solicitud.getUuid());

        solicitud.setEstado(EstadoSolicitud.ATENDIDA);
        solicitud.setFechaResolucion(LocalDate.now());

        if (tecnico != null) {
            tecnico.setEstado(EstadoTecnico.DISPONIBLE);
        }
        if (unidad != null) {
            unidad.setEstado(EstadoUnidad.DISPONIBLE);
        }

        if (solicitud.getIdKit() != 0) {
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
                idOperacion, TipoOperacion.FINALIZACION_SOLICITUD,
                "Cierre Atómico: Solicitud " + solicitud.getId() + " finalizada.",
                solicitud.getId(), solicitud.getIdTecnico(),
                solicitud.getUuid(), solicitud.getIdKit()
        );
        operacionDAO.registerOperation(opFin);

        if (kitDanado) {
            String idOperacionMantenimiento = "OP-" + (operacionDAO.getHistory().size() + 1);
            Operacion opMantenimiento = new Operacion(
                    idOperacionMantenimiento,
                    TipoOperacion.KIT_A_REVISION,
                    "Kit enviado a mantenimiento: " + solicitud.getIdKit(),
                    null,
                    null,
                    null,
                    solicitud.getIdKit()
            );
            operacionDAO.registerOperation(opMantenimiento);
        }
    }

    public Solicitud obtenerProximaSolicitud() {
        return solicitudDAO.peekNextIncident();
    }

    public SimpleLinkedList<Solicitud> obtenerTodasLasSolicitudes() {
        return solicitudDAO.getFullHistory();
    }

    public DoubleLinkedList<Tecnico> obtenerTecnicosDisponibles() {
        return tecnicoDAO.getAvailable();
    }

    public DoubleLinkedList<UnidadServicio> obtenerUnidadesDisponibles() {
        return unidadDAO.getByState(EstadoUnidad.DISPONIBLE);
    }
}
