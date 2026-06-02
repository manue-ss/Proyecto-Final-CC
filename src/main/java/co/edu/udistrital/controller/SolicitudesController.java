package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.Solicitud;
import co.edu.udistrital.model.enums.EstadoSolicitud;
import co.edu.udistrital.model.enums.NivelCriticidad;
import co.edu.udistrital.model.enums.TipoSolicitud;
import co.edu.udistrital.model.usecases.SolicitudUseCase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class SolicitudesController {

    private final SolicitudUseCase useCase;

    @FXML private TextField txtIdCliente;
    @FXML private ComboBox<TipoSolicitud> cmbTipo;
    @FXML private ComboBox<NivelCriticidad> cmbCriticidad;
    @FXML private TextField txtDescripcion;

    @FXML private TableView<Solicitud> tablaSolicitudes;
    @FXML private TableColumn<Solicitud, Integer> colId;
    @FXML private TableColumn<Solicitud, String> colTipo;
    @FXML private TableColumn<Solicitud, String> colCriticidad;
    @FXML private TableColumn<Solicitud, String> colEstado;
    @FXML private TableColumn<Solicitud, String> colDescripcion;

    public SolicitudesController(SolicitudUseCase useCase) {
        this.useCase = useCase;
    }

    @FXML public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList(TipoSolicitud.values()));
        cmbCriticidad.setItems(FXCollections.observableArrayList(NivelCriticidad.values()));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colCriticidad.setCellValueFactory(new PropertyValueFactory<>("criticidad"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        cargarTabla();
    }

    @FXML private void registrarSolicitud() {
        if (txtIdCliente.getText().isEmpty() || txtDescripcion.getText().isEmpty() || 
            cmbTipo.getValue() == null || cmbCriticidad.getValue() == null) {
            mostrarAlerta("Debe llenar todos los campos del formulario.");
            return;
        }

        try {
            int idCliente = Integer.parseInt(txtIdCliente.getText());
            useCase.registrarSolicitud(idCliente, txtDescripcion.getText(), cmbTipo.getValue(), cmbCriticidad.getValue());
            
            txtIdCliente.clear();
            txtDescripcion.clear();
            cmbTipo.setValue(null);
            cmbCriticidad.setValue(null);
            cargarTabla();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error: El ID del cliente debe ser un número.");
        }
    }

    @FXML private void marcarEnEjecucion() {
        Solicitud s = tablaSolicitudes.getSelectionModel().getSelectedItem();
        if (s != null) { 
            useCase.cambiarEstadoSolicitud(s.getId(), EstadoSolicitud.EN_EJECUCION); 
            cargarTabla(); 
        } else {
            mostrarAlerta("Seleccione una solicitud de la tabla primero.");
        }
    }

    @FXML private void finalizarSolicitud() {
        Solicitud s = tablaSolicitudes.getSelectionModel().getSelectedItem();
        if (s != null) { 
            useCase.cambiarEstadoSolicitud(s.getId(), EstadoSolicitud.ATENDIDA); 
            cargarTabla(); 
        } else {
            mostrarAlerta("Seleccione una solicitud de la tabla primero.");
        }
    }

    private void cargarTabla() {
        ObservableList<Solicitud> lista = FXCollections.observableArrayList();
        Iterable<Solicitud> datos = useCase.obtenerTodasLasSolicitudes();
        if (datos != null) { for (Solicitud s : datos) { lista.add(s); } }
        tablaSolicitudes.setItems(lista);
    }

    private void mostrarAlerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }
}