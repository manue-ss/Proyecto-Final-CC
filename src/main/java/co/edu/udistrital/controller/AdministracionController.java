package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.Operacion;
import co.edu.udistrital.model.usecases.AdministracionUseCase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdministracionController {
    private final AdministracionUseCase useCase;

    @FXML private TableView<Operacion> tablaAuditoria;
    @FXML private TableColumn<Operacion, String> colId;
    @FXML private TableColumn<Operacion, String> colTipo;
    @FXML private TableColumn<Operacion, String> colDescripcion;

    public AdministracionController(AdministracionUseCase useCase) { this.useCase = useCase; }

    @FXML public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cargarTabla();
    }

    @FXML private void deshacerOperacion() {
        try {
            useCase.deshacerUltimaOperacion();
            cargarTabla();
            new Alert(Alert.AlertType.INFORMATION, "La operación se deshizo correctamente.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "No se pudo deshacer: " + e.getMessage()).show();
        }
    }

    @FXML private void exportarCSV() {
        useCase.generarReporteDiarioCSV();
        new Alert(Alert.AlertType.INFORMATION, "Reporte exportado exitosamente.").show();
    }

    private void cargarTabla() {
        ObservableList<Operacion> lista = FXCollections.observableArrayList();
        Iterable<Operacion> datos = useCase.obtenerAuditoria(); 
        if (datos != null) { for (Operacion o : datos) { lista.add(o); } }
        tablaAuditoria.setItems(lista);
    }
}