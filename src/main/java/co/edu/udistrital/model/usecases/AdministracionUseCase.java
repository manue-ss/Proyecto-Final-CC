package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.daos.*;
import co.edu.udistrital.model.entities.*;
import co.edu.udistrital.model.enums.*;
import co.edu.udistrital.model.structures.SimpleLinkedList;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
            System.out.println("El historial de operaciones está vacío. No hay nada que deshacer.");
            return;
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
                System.out.println("Tipo de operación no reconocido.");
        }

        if (exito) {
            System.out.println("ÉXITO: Se ha revertido -> " + op.getDescripcionUI());
        } else {
            System.out.println("ERROR: No se pudo revertir la operación.");
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
                unidad.setEstado(EstadoUnidad.DISPONIBLE);
                unidadDAO.update();
                return true;
            }
        } else if (op.getIdTecnico() != null) {
            Tecnico tecnico = tecnicoDAO.findById(op.getIdTecnico());
            if (tecnico != null) {
                tecnico.setEstado(EstadoTecnico.DISPONIBLE);
                tecnicoDAO.update();
                return true;
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

    public void generarReporteDiarioCSV() {
        SimpleLinkedList<Solicitud> historialCompleto = solicitudDAO.getFullHistory();

        String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nombreArchivo = "Reporte_Diario_" + fechaHoy + ".csv";

        try (FileWriter escritor = new FileWriter(nombreArchivo)) {

            escritor.append("ID Solicitud,ID Cliente,Tipo de Caso,Criticidad,Estado,ID Tecnico Asignado,Unidad UUID,ID Kit Usado\n");

            int casosAtendidos = 0;

            for (Solicitud solicitud : historialCompleto) {

                if (solicitud.getEstado() == EstadoSolicitud.ATENDIDA) {

                    escritor.append(String.valueOf(solicitud.getId())).append(",");
                    escritor.append(String.valueOf(solicitud.getIdCliente())).append(",");
                    escritor.append(solicitud.getTipo().toString()).append(",");
                    escritor.append(solicitud.getCriticidad().toString()).append(",");
                    escritor.append(solicitud.getEstado().toString()).append(",");

                    // Manejo de posibles nulos para evitar NullPointerExceptions en el texto
                    escritor.append(solicitud.getIdTecnico() != 0 ? String.valueOf(solicitud.getIdTecnico()) : "N/A").append(",");
                    escritor.append(solicitud.getUuid() != null ? solicitud.getUuid() : "N/A").append(",");
                    escritor.append(solicitud.getIdKit() != 0 ? String.valueOf(solicitud.getIdKit()) : "N/A").append("\n");

                    casosAtendidos++;
                }
            }

            System.out.println("¡Reporte generado exitosamente! Se exportaron " + casosAtendidos + " casos al archivo: " + nombreArchivo);

        }
        catch (IOException e) {
            System.err.println("Error crítico al intentar generar el archivo CSV: " + e.getMessage());
        }
    }
}
