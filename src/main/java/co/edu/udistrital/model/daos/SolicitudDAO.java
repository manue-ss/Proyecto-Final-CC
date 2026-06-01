package co.edu.udistrital.model.daos;

import co.edu.udistrital.model.entities.Solicitud;
import co.edu.udistrital.model.enums.EstadoSolicitud;
import co.edu.udistrital.model.enums.NivelCriticidad;
import co.edu.udistrital.model.structures.MaxHeapPriorityQueue;
import co.edu.udistrital.model.structures.Queue;
import co.edu.udistrital.model.structures.SimpleLinkedList;
import co.edu.udistrital.model.util.GestorArchivosBinarios;

public class SolicitudDAO {

    private final String RUTA_ARCHIVO = "solicitudes.dat";
    private final GestorArchivosBinarios<SimpleLinkedList<Solicitud>> gestorBinario;

    // Las estructuras en memoria solicitadas en tu Issue
    private MaxHeapPriorityQueue<Solicitud> colaCriticas;
    private Queue<Solicitud> colaOrdinarias;

    public SolicitudDAO() {
        this.gestorBinario = new GestorArchivosBinarios<>();
        this.colaCriticas = new MaxHeapPriorityQueue<>();
        this.colaOrdinarias = new Queue<>();
    }

    // --- TAREA 1: Lectura y Escritura ---
    
    // Método para guardar todo el bloque de datos al archivo
    public void guardarTodas(SimpleLinkedList<Solicitud> todasLasSolicitudes) {
        gestorBinario.guardarDatos(RUTA_ARCHIVO, todasLasSolicitudes);
    }

    // Método privado auxiliar para traer los datos crudos del disco
    private SimpleLinkedList<Solicitud> cargarTodas() {
        SimpleLinkedList<Solicitud> datos = gestorBinario.cargarDatos(RUTA_ARCHIVO);
        // Si el archivo no existe (primera ejecución), retornamos una lista vacía
        return (datos != null) ? datos : new SimpleLinkedList<>();
    }
    // --- TAREA 2: Implementar obtenerPendientesOrdenadas() ---
    
    public void obtenerPendientesOrdenadas() {
        // 1. Limpiamos las colas para no duplicar datos si recargamos la vista
        colaCriticas.clear();
        colaOrdinarias.clear();

        // 2. Traemos todos los registros de la base de datos (archivo binario)
        SimpleLinkedList<Solicitud> historialCompleto = cargarTodas();

        // 3. Iteramos (usando el iterador que configuraste en tu SimpleLinkedList)
        for (Solicitud solicitud : historialCompleto) {
            
            // Solo procesamos las que necesitan atención
            if (solicitud.getEstado() == EstadoSolicitud.PENDIENTE) {
                
                // Criterio de separación: CRITICA y MEDIA van al Max-Heap. 
                // Las demás (ORDINARIA, SIN_PRIORIDAD) van a la cola FIFO.
                if (solicitud.getCriticidad() == NivelCriticidad.CRITICA || 
                    solicitud.getCriticidad() == NivelCriticidad.MEDIA) {
                    
                    // El MaxHeap las ordenará automáticamente usando el compareTo que hiciste
                    colaCriticas.enqueue(solicitud); 
                } else {
                    // La cola simple guarda el estricto orden de llegada
                    colaOrdinarias.enqueue(solicitud);
                }
            }
        }
        System.out.println("Triaje completado. Críticas: " + colaCriticas.size() + 
                           " | Ordinarias: " + colaOrdinarias.size());
    }
    // --- TAREA 3: Método de extracción jerárquica ---
    
    public Solicitud extraerSiguienteIncidencia() {
        // Primera prioridad: Drenar el Max-Heap
        if (!colaCriticas.isEmpty()) {
            return colaCriticas.dequeue();
        } 
        // Segunda prioridad: Drenar la Cola FIFO
        else if (!colaOrdinarias.isEmpty()) {
            return colaOrdinarias.dequeue();
        } 
        // Si ambas están vacías
        else {
            return null; // O lanzar una excepción personalizada si prefieres
        }
    }
}