package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.UnidadServicio;
import co.edu.udistrital.model.enums.TipoUnidad;
import co.edu.udistrital.model.usecases.UnidadUseCase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UnidadesController {

    private final UnidadUseCase useCase;

    @FXML private TextField txtZona;
    @FXML private ComboBox<TipoUnidad> cmbTipoUnidad;
    @FXML private TableView<UnidadServicio> tablaUnidades;
    @FXML private TableColumn<UnidadServicio, String> colUuid;
    @FXML private TableColumn<UnidadServicio, String> colTipo;
    @FXML private TableColumn<UnidadServicio, String> colEstado;

    public UnidadesController(UnidadUseCase useCase) { this.useCase = useCase; }

    @FXML public void initialize() {
        cmbTipoUnidad.setItems(FXCollections.observableArrayList(TipoUnidad.values()));
        colUuid.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cargarTabla(); 
    }

    @FXML private void registrarUnidad() {
        if (txtZona.getText().isEmpty() || cmbTipoUnidad.getValue() == null) return;
        useCase.registrarNuevaUnidad(cmbTipoUnidad.getValue().name(), txtZona.getText());
        txtZona.clear(); cmbTipoUnidad.setValue(null); 
        cargarTabla();
    }

    @FXML private void enviarMantenimiento() {
        UnidadServicio u = tablaUnidades.getSelectionModel().getSelectedItem();
        if (u != null) { useCase.enviarUnidadAMantenimiento(u.getUuid()); cargarTabla(); }
    }

    @FXML private void liberarMantenimiento() {
        UnidadServicio u = tablaUnidades.getSelectionModel().getSelectedItem();
        if (u != null) { useCase.liberarUnidadDeMantenimiento(u.getUuid()); cargarTabla(); }
    }

    @FXML private void darDeBaja() {
        UnidadServicio u = tablaUnidades.getSelectionModel().getSelectedItem();
        if (u != null) { useCase.darDeBajaUnidad(u.getUuid()); cargarTabla(); }
    }

    private void cargarTabla() {
        ObservableList<UnidadServicio> lista = FXCollections.observableArrayList();
        Iterable<UnidadServicio> datos = useCase.obtenerTodas(); 
        if (datos != null) { for (UnidadServicio u : datos) { lista.add(u); } }
        tablaUnidades.setItems(lista);
    }
}