package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.daos.*;
import co.edu.udistrital.model.entities.*;
import co.edu.udistrital.model.enums.*;
import co.edu.udistrital.model.structures.SimpleLinkedList;
import co.edu.udistrital.model.structures.Stack;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class AdministracionUseCase {

    private final SolicitudDAO solicitudDAO;
    private final TecnicoDAO tecnicoDAO;
    private final UnidadServicioDAO unidadDAO;
    private final KitDAO kitDAO;
    private final OperacionDAO operacionDAO;

    public AdministracionUseCase(SolicitudDAO solicitudDAO, TecnicoDAO tecnicoDAO,
            UnidadServicioDAO unidadDAO, KitDAO kitDAO, OperacionDAO operacionDAO) {
        this.solicitudDAO = solicitudDAO;
        this.tecnicoDAO = tecnicoDAO;
        this.unidadDAO = unidadDAO;
        this.kitDAO = kitDAO;
        this.operacionDAO = operacionDAO;
    }

    public void deshacerUltimaOperacion() {
        Operacion op = operacionDAO.popLastOperation();

        if (op == null) {
            throw new IllegalStateException("El historial de operaciones está vacío. No hay nada que deshacer.");
        }

        boolean exito = false;

        switch (op.getTipo()) {
            case ASIGNACION_SERVICIO ->
                exito = deshacerAsignacion(op);
            case CIERRE_SERVICIO ->
                exito = deshacerCierre(op);
            case MANTENIMIENTO_RECURSO ->
                exito = deshacerMantenimiento(op);
            default ->
                throw new IllegalStateException("Tipo de operación no reconocido en el historial.");
        }

        if (!exito) {
            throw new IllegalStateException("No se pudo revertir la operación seleccionada debido a un conflicto de estados.");
        }
    }

    public boolean deshacerAsignacion(Operacion op) {
        Solicitud solicitud = solicitudDAO.getById(op.getIdSolicitud());
        Tecnico tecnico = tecnicoDAO.findById(op.getIdTecnico());
        UnidadServicio unidad = unidadDAO.findByUuid(op.getUuidUnidad());

        if (solicitud != null) {
            solicitud.setEstado(EstadoSolicitud.PENDIENTE);
            solicitud.setIdTecnico(0);
            solicitud.setUuid(null);
            solicitud.setIdKit(0);
            solicitudDAO.update();
            solicitudDAO.sortPendingRequests();
        }

        if (tecnico != null) {
            tecnico.setEstado(EstadoTecnico.DISPONIBLE);
            tecnicoDAO.update();
        }

        if (unidad != null) {
            unidad.setEstado(EstadoUnidad.DISPONIBLE);
            unidadDAO.update();
        }

        if (op.getIdKit() != null) {
            Kit kit = kitDAO.getById(op.getIdKit());
            if (kit != null) {
                kitDAO.returnFromService(kit, false);
            }
        }

        return true;
    }

    public boolean deshacerCierre(Operacion op) {
        Solicitud solicitud = solicitudDAO.getById(op.getIdSolicitud());
        Tecnico tecnico = tecnicoDAO.findById(op.getIdTecnico());
        UnidadServicio unidad = unidadDAO.findByUuid(op.getUuidUnidad());

        if (solicitud != null) {
            solicitud.setEstado(EstadoSolicitud.EN_EJECUCION);
            solicitud.setFechaResolucion(null);
            solicitudDAO.update();
        }

        if (tecnico != null) {
            tecnico.setEstado(EstadoTecnico.ASIGNADO);
            tecnicoDAO.update();
        }

        if (unidad != null) {
            unidad.setEstado(EstadoUnidad.OCUPADA);
            unidadDAO.update();
        }

        if (op.getIdKit() != null) {
            Kit kit = kitDAO.getById(op.getIdKit());
            if (kit != null) {
                kit.setEstado(EstadoKit.ASIGNADO);
                kitDAO.update();
            }
        }

        return true;
    }

    public boolean deshacerMantenimiento(Operacion op) {
        if (op.getUuidUnidad() != null) {
            UnidadServicio unidad = unidadDAO.findByUuid(op.getUuidUnidad());
            if (unidad != null) {
                if (unidad.getEstado() == EstadoUnidad.EN_MANTENIMIENTO) {
                    unidad.setEstado(EstadoUnidad.DISPONIBLE);
                    unidadDAO.update();
                    return true;
                }
                return false;
            }
        } else if (op.getIdTecnico() != null) {
            Tecnico tecnico = tecnicoDAO.findById(op.getIdTecnico());
            if (tecnico != null) {
                if (tecnico.getEstado() == EstadoTecnico.EN_DESCANSO) {
                    tecnico.setEstado(EstadoTecnico.DISPONIBLE);
                    tecnicoDAO.update();
                    return true;
                }
                return false;
            }
        } else if (op.getIdKit() != null) {
            Kit kit = kitDAO.getById(op.getIdKit());
            if (kit != null) {
                kitDAO.finishMaintenance(kit);
                return true;
            }
        }
        return false;
    }

    public void generarReporteDiarioCSV(String rutaAbsoluta) throws IOException {
        SimpleLinkedList<Solicitud> historialCompleto = solicitudDAO.getFullHistory();
        LocalDate hoy = LocalDate.now();
        boolean existenCasosHoy = false;

        if (historialCompleto != null) {
            for (Solicitud solicitud : historialCompleto) {
                if (solicitud.getEstado() == EstadoSolicitud.ATENDIDA && hoy.equals(solicitud.getFechaResolucion())) {
                    existenCasosHoy = true;
                    break;
                }
            }
        }

        if (!existenCasosHoy) {
            throw new IllegalStateException("No se han resuelto solicitudes en el día de hoy.");
        }

        try (FileWriter escritor = new FileWriter(rutaAbsoluta)) {
            escritor.append("ID Solicitud,ID Cliente,Tipo de Caso,Criticidad,Estado,ID Tecnico Asignado,Unidad UUID,ID Kit Usado\n");

            for (Solicitud solicitud : historialCompleto) {
                if (solicitud.getEstado() == EstadoSolicitud.ATENDIDA && hoy.equals(solicitud.getFechaResolucion())) {
                    escritor.append(String.valueOf(solicitud.getId())).append(",");
                    escritor.append(String.valueOf(solicitud.getIdCliente())).append(",");
                    escritor.append(solicitud.getTipo().toString()).append(",");
                    escritor.append(solicitud.getCriticidad().toString()).append(",");
                    escritor.append(solicitud.getEstado().toString()).append(",");
                    escritor.append(solicitud.getIdTecnico() != 0 ? String.valueOf(solicitud.getIdTecnico()) : "N/A").append(",");
                    escritor.append(solicitud.getUuid() != null ? solicitud.getUuid() : "N/A").append(",");
                    escritor.append(solicitud.getIdKit() != 0 ? String.valueOf(solicitud.getIdKit()) : "N/A").append("\n");
                }
            }
        }
    }

    public Stack<Operacion> obtenerAuditoria() {
        return operacionDAO.getHistory();
    }
}
