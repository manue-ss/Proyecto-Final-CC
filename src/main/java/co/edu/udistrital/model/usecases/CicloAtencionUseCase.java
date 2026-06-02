package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.daos.KitDAO;
import co.edu.udistrital.model.daos.OperacionDAO;
import co.edu.udistrital.model.daos.SolicitudDAO;
import co.edu.udistrital.model.daos.TecnicoDAO;
import co.edu.udistrital.model.daos.UnidadServicioDAO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.entities.Operacion;
import co.edu.udistrital.model.entities.Solicitud;
import co.edu.udistrital.model.entities.Tecnico;
import co.edu.udistrital.model.entities.UnidadServicio;
import co.edu.udistrital.model.enums.EstadoSolicitud;
import co.edu.udistrital.model.enums.EstadoTecnico;
import co.edu.udistrital.model.enums.EstadoUnidad;
import co.edu.udistrital.model.enums.NivelCriticidad;
import co.edu.udistrital.model.enums.TipoOperacion;
import co.edu.udistrital.model.enums.TipoSolicitud;
import java.util.UUID;

/**
 * Motor orquestador para el ciclo de vida completo de una emergencia.
 */
public class CicloAtencionUseCase {

    private final SolicitudDAO solicitudDAO;
    private final TecnicoDAO tecnicoDAO;
    private final UnidadServicioDAO unidadDAO;
    private final KitDAO kitDAO;
    private final OperacionDAO operacionDAO;

    // Inyección de dependencias por constructor
    public CicloAtencionUseCase(SolicitudDAO solicitudDAO, TecnicoDAO tecnicoDAO, 
                                UnidadServicioDAO unidadDAO, KitDAO kitDAO, OperacionDAO operacionDAO) {
        this.solicitudDAO = solicitudDAO;
        this.tecnicoDAO = tecnicoDAO;
        this.unidadDAO = unidadDAO;
        this.kitDAO = kitDAO;
        this.operacionDAO = operacionDAO;
    }

    // ==========================================
    // 1. CREAR SOLICITUD
    // ==========================================
    /**
     * Registra una nueva solicitud en el sistema y la envía a la cola de triaje.
     */
    public Solicitud crearSolicitud(int idCliente, String descripcion, TipoSolicitud tipo, NivelCriticidad criticidad) {
        // Autogeneramos un ID sumando 1 al tamaño del historial (Simulando un AUTO_INCREMENT)
        int nuevoId = solicitudDAO.getFullHistory().size() + 1;

        Solicitud nuevaSolicitud = new Solicitud(
                nuevoId, 
                idCliente, 
                null, // Aún no tiene unidad asignada
                0,    // Aún no tiene técnico
                descripcion, 
                tipo, 
                criticidad, 
                EstadoSolicitud.PENDIENTE
        );

        // El DAO la guarda en el archivo binario y la encola (al MaxHeap o a la FIFO automáticamente)
        solicitudDAO.registerRequest(nuevaSolicitud);
        
        return nuevaSolicitud;
    }

    // ==========================================
    // 2. ASIGNAR RECURSOS (VEHÍCULO, TÉCNICO Y KIT)
    // ==========================================
    /**
     * Vincula todos los recursos físicos y humanos a la incidencia.
     * @return El Kit asignado (o null si no se solicitó).
     */
    public Kit despacharServicio(Solicitud solicitud, Tecnico tecnico, UnidadServicio unidad, boolean requiereKit) {
        
        // --- A. VALIDACIONES BLINDADAS ---
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue atendida o está en ejecución.");
        }
        if (!tecnico.isDisponible()) {
            throw new IllegalStateException("El técnico seleccionado no está disponible.");
        }
        if (!unidad.isDisponibilidad() || unidad.getEstado() != EstadoUnidad.DISPONIBLE) {
            throw new IllegalStateException("La unidad de servicio no está disponible.");
        }

        // --- B. ASIGNACIÓN DEL KIT (USANDO EL STACK DEL DAO) ---
        Kit kitAsignado = null;
        if (requiereKit) {
            kitAsignado = kitDAO.popReadyKit(); // Extrae de tu Stack<Kit> en memoria
            if (kitAsignado == null) {
                throw new IllegalStateException("No hay kits de herramientas disponibles en bodega.");
            }
        }

        // --- C. ACTUALIZACIÓN DE ESTADOS EN ENTIDADES ---
        solicitud.setEstado(EstadoSolicitud.EN_EJECUCION);
        solicitud.setIdTecnico(tecnico.getId());
        solicitud.setUuid(unidad.getUuid());

        tecnico.setEstado(EstadoTecnico.ASIGNADO);
        unidad.setEstado(EstadoUnidad.OCUPADA);
        unidad.setDisponibilidad(false);

        // --- D. PERSISTENCIA EN DAOS ---
        solicitudDAO.saveAll();
        tecnicoDAO.update();
        unidadDAO.update();
        // El KitDAO ya hace saveAll() internamente al hacer popReadyKit()

        // --- E. AUDITORÍA (REGISTRO DE OPERACIÓN) ---
        Integer idKit = (kitAsignado != null) ? kitAsignado.getId() : null;
        
        Operacion opTecnico = new Operacion(UUID.randomUUID().toString(), TipoOperacion.ASIGNACION_TECNICO, 
                "Técnico " + tecnico.getId() + " asignado a caso " + solicitud.getId(), 
                solicitud.getId(), tecnico.getId(), null, null);
                
        Operacion opUnidad = new Operacion(UUID.randomUUID().toString(), TipoOperacion.ASIGNACION_UNIDAD, 
                "Unidad " + unidad.getUuid() + " despachada con Kit " + idKit, 
                solicitud.getId(), null, unidad.getUuid(), idKit);

        operacionDAO.registerOperation(opTecnico);
        operacionDAO.registerOperation(opUnidad);

        return kitAsignado;
    }

    // ==========================================
    // 3. FINALIZAR SOLICITUD
    // ==========================================
    /**
     * Cierra el caso, libera al técnico/vehículo y devuelve el kit.
     */
    public void finalizarSolicitud(Solicitud solicitud, Kit kitUsado, boolean kitDanado) {
        
        if (solicitud.getEstado() != EstadoSolicitud.EN_EJECUCION) {
            throw new IllegalStateException("Solo se pueden finalizar solicitudes en ejecución.");
        }

        // --- A. RECUPERAR RECURSOS DESDE LOS DAOS ---
        Tecnico tecnico = tecnicoDAO.findById(solicitud.getIdTecnico());
        UnidadServicio unidad = unidadDAO.findByUuid(solicitud.getUuid());

        // --- B. LIBERACIÓN Y CAMBIO DE ESTADOS ---
        solicitud.setEstado(EstadoSolicitud.ATENDIDA);
        
        if (tecnico != null) {
            tecnico.setEstado(EstadoTecnico.DISPONIBLE);
        }
        
        if (unidad != null) {
            unidad.setEstado(EstadoUnidad.DISPONIBLE);
            unidad.setDisponibilidad(true);
        }

        // --- C. DEVOLUCIÓN DEL KIT (USANDO EL DAO) ---
        if (kitUsado != null) {
            // El DAO decide si va al Stack de Listos o al Stack de Mantenimiento
            kitDAO.returnFromService(kitUsado, kitDanado); 
        }

        // --- D. PERSISTENCIA FINAL ---
        solicitudDAO.saveAll();
        tecnicoDAO.update();
        unidadDAO.update();

        // --- E. AUDITORÍA ---
        Operacion opFin = new Operacion(UUID.randomUUID().toString(), TipoOperacion.CAMBIO_ESTADO_SOLICITUD, 
                "Solicitud " + solicitud.getId() + " FINALIZADA. Recursos liberados.", 
                solicitud.getId(), solicitud.getIdTecnico(), solicitud.getUuid(), null);
                
        operacionDAO.registerOperation(opFin);
    }
}