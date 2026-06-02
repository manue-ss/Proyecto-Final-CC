package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.Solicitud;
import co.edu.udistrital.model.usecases.SolicitudUseCase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SolicitudesController {

    private final SolicitudUseCase useCase;

    // === ELEMENTOS DE LA INTERFAZ ===
    @FXML private TableView<Solicitud> tablaSolicitudes;
    @FXML private TableColumn<Solicitud, Integer> colId;
    @FXML private TableColumn<Solicitud, String> colTipo;
    @FXML private TableColumn<Solicitud, String> colCriticidad;
    @FXML private TableColumn<Solicitud, String> colEstado;
    @FXML private TableColumn<Solicitud, String> colDescripcion;

    public SolicitudesController(SolicitudUseCase useCase) {
        this.useCase = useCase;
    }

    @FXML 
    public void initialize() {
        // 1. Configurar cómo cada columna obtiene su dato de la clase Solicitud
        // El String DEBE coincidir exactamente con el nombre de tu atributo en la clase
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colCriticidad.setCellValueFactory(new PropertyValueFactory<>("criticidad"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // 2. Cargar los datos en la tabla
        cargarDatosEnTabla();
    }

    private void cargarDatosEnTabla() {
        // Obtenemos tu estructura de datos nativa desde el motor (ej. SimpleLinkedList)
        Iterable<Solicitud> historial = useCase.obtenerTodasLasSolicitudes();

        // JavaFX exige una ObservableList. Creamos una vacía.
        ObservableList<Solicitud> listaObservable = FXCollections.observableArrayList();

        // Iteramos tu estructura y pasamos los objetos a la lista visual si el historial no es nulo
        if (historial != null) {
            for (Solicitud solicitud : historial) {
                listaObservable.add(solicitud);
            }
        }

        // Finalmente, le inyectamos los datos a la tabla gráfica
        tablaSolicitudes.setItems(listaObservable);
    }
}