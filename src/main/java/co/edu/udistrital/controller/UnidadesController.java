package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.UnidadServicio;
import co.edu.udistrital.model.enums.TipoUnidad;
import co.edu.udistrital.model.enums.Zonas;
import co.edu.udistrital.model.usecases.UnidadUseCase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UnidadesController {

    private final UnidadUseCase useCase;

    @FXML
    private ComboBox<Zonas> cmbZona;
    @FXML
    private ComboBox<TipoUnidad> cmbTipoUnidad;
    @FXML
    private TableView<UnidadServicio> tablaUnidades;
    @FXML
    private TableColumn<UnidadServicio, String> colUuid;
    @FXML
    private TableColumn<UnidadServicio, String> colTipo;
    @FXML
    private TableColumn<UnidadServicio, String> colZona;
    @FXML
    private TableColumn<UnidadServicio, String> colEstado;
    @FXML
    private TableColumn<UnidadServicio, String> colDisponibilidad;

    public UnidadesController(UnidadUseCase useCase) {
        this.useCase = useCase;
    }

    @FXML
    public void initialize() {
        cmbTipoUnidad.setItems(FXCollections.observableArrayList(TipoUnidad.values()));
        cmbZona.setItems(FXCollections.observableArrayList(Zonas.values()));
        colUuid.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zona"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDisponibilidad.setCellValueFactory(cellData
                -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().isDisponibilidad() ? "Sí" : "No")
        );
        cargarTabla();
    }

    @FXML
    private void registrarUnidad() {
        if (cmbZona.getValue() == null || cmbTipoUnidad.getValue() == null) {
            return;
        }
        useCase.registrarNuevaUnidad(cmbTipoUnidad.getValue(), cmbZona.getValue());
        cmbZona.setValue(null);
        cmbTipoUnidad.setValue(null);
        cargarTabla();
        EventoGlobal.notificarCambio();
    }

    @FXML
    private void enviarMantenimiento() {
        UnidadServicio u = tablaUnidades.getSelectionModel().getSelectedItem();
        if (u != null) {
            useCase.enviarUnidadAMantenimiento(u.getUuid());
            cargarTabla();
            EventoGlobal.notificarCambio();
        }
    }

    @FXML
    private void liberarMantenimiento() {
        UnidadServicio u = tablaUnidades.getSelectionModel().getSelectedItem();
        if (u != null) {
            useCase.liberarUnidadDeMantenimiento(u.getUuid());
            cargarTabla();
            EventoGlobal.notificarCambio();
        }
    }

    @FXML
    private void darDeBaja() {
        UnidadServicio u = tablaUnidades.getSelectionModel().getSelectedItem();
        if (u != null) {
            useCase.darDeBajaUnidad(u.getUuid());
            cargarTabla();
            EventoGlobal.notificarCambio();
        }
    }

    private void cargarTabla() {
        ObservableList<UnidadServicio> lista = FXCollections.observableArrayList();
        Iterable<UnidadServicio> datos = useCase.obtenerTodas();
        if (datos != null) {
            for (UnidadServicio u : datos) {
                lista.add(u);
            }
        }
        tablaUnidades.setItems(lista);
        tablaUnidades.refresh();
    }
}
