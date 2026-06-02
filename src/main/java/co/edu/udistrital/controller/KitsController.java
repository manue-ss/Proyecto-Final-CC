package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.TipoKit;
import co.edu.udistrital.model.usecases.KitUseCase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class KitsController {
    private final KitUseCase useCase;

    @FXML private ComboBox<TipoKit> cmbTipoKit;
    @FXML private TableView<Kit> tablaKits;
    @FXML private TableColumn<Kit, Integer> colId;
    @FXML private TableColumn<Kit, String> colCodigo;
    @FXML private TableColumn<Kit, String> colTipo;
    @FXML private TableColumn<Kit, String> colEstado;

    public KitsController(KitUseCase useCase) { this.useCase = useCase; }

    @FXML public void initialize() {
        cmbTipoKit.setItems(FXCollections.observableArrayList(TipoKit.values()));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo")); 
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cargarTabla();
    }

    @FXML private void registrarKit() {
        if (cmbTipoKit.getValue() != null) {
            useCase.registrarNuevoKit(cmbTipoKit.getValue());
            cmbTipoKit.setValue(null);
            cargarTabla();
        }
    }

    @FXML private void repararKit() {
        try {
            useCase.repararKitEnBodega();
            cargarTabla();
        } catch (IllegalStateException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
        }
    }
    
    private void cargarTabla() {
        ObservableList<Kit> lista = FXCollections.observableArrayList();
        Iterable<Kit> datos = useCase.obtenerTodos(); 
        if (datos != null) { for (Kit k : datos) { lista.add(k); } }
        tablaKits.setItems(lista);
    }
}