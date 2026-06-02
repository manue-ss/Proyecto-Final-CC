package co.edu.udistrital.model.daos;

import co.edu.udistrital.model.entities.Solicitud;
import co.edu.udistrital.model.enums.EstadoSolicitud;
import co.edu.udistrital.model.enums.NivelCriticidad;
import co.edu.udistrital.model.structures.MaxHeapPriorityQueue;
import co.edu.udistrital.model.structures.Queue;
import co.edu.udistrital.model.structures.SimpleLinkedList;
import co.edu.udistrital.model.util.GestorArchivosBinarios;

public final class SolicitudDAO {

    private final String FILE_PATH = "solicitudes.dat";
    private final GestorArchivosBinarios<SimpleLinkedList<Solicitud>> binaryManager;

    private SimpleLinkedList<Solicitud> fullHistory;

    private MaxHeapPriorityQueue<Solicitud> criticalQueue;
    private Queue<Solicitud> ordinaryQueue;

    public SolicitudDAO() {
        this.binaryManager = new GestorArchivosBinarios<>();
        this.criticalQueue = new MaxHeapPriorityQueue<>();
        this.ordinaryQueue = new Queue<>();

        this.fullHistory = loadAll();

        sortPendingRequests();
    }

    private SimpleLinkedList<Solicitud> loadAll() {
        SimpleLinkedList<Solicitud> data = binaryManager.cargarDatos(FILE_PATH);
        return (data != null) ? data : new SimpleLinkedList<>();
    }

    public void saveAll() {
        binaryManager.guardarDatos(FILE_PATH, this.fullHistory);
    }

    public void registerRequest(Solicitud newRequest) {
        this.fullHistory.addLast(newRequest);
        saveAll();
        sortPendingRequests();
    }

    public void sortPendingRequests() {
        criticalQueue.clear();
        ordinaryQueue.clear();

        for (Solicitud request : fullHistory) {

            if (request.getEstado() == EstadoSolicitud.PENDIENTE) {

                // Using the Enum's numeric value: > 1 goes to Heap, <= 1 goes to FIFO
                if (request.getCriticidad().getNivel() > NivelCriticidad.ORDINARIA.getNivel()) {
                    criticalQueue.enqueue(request);
                } else {
                    ordinaryQueue.enqueue(request);
                }
            }
        }

        System.out.println("Triage completed. Critical: " + criticalQueue.size()
                + " | Ordinary: " + ordinaryQueue.size());
    }

    public Solicitud pollNextIncident() {
        Solicitud nextIncident = null;

        if (!criticalQueue.isEmpty()) {
            nextIncident = criticalQueue.dequeue();
        } else if (!ordinaryQueue.isEmpty()) {
            nextIncident = ordinaryQueue.dequeue();
        }

        if (nextIncident != null) {

            saveAll();
        }

        return nextIncident;
    }

    public SimpleLinkedList<Solicitud> getFullHistory() {
        return this.fullHistory;
    }

    public SimpleLinkedList<Solicitud> getActiveRequests() {
        SimpleLinkedList<Solicitud> activeList = new SimpleLinkedList<>();

        for (Solicitud request : fullHistory) {
            if (request.getEstado() == EstadoSolicitud.EN_EJECUCION) {
                activeList.addLast(request);
            }
        }
        return activeList;
    }
}
