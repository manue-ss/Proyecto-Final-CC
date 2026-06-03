package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.TipoKit;
import co.edu.udistrital.model.structures.Stack;
import co.edu.udistrital.model.usecases.KitUseCase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class KitsController {

    private final KitUseCase useCase;

    @FXML
    private ComboBox<TipoKit> cmbTipoKit;
    @FXML
    private TableView<Kit> tablaDisponibles;
    @FXML
    private TableView<Kit> tablaMantenimiento;

    @FXML
    private TableColumn<Kit, Integer> colIdDisp, colIdMant;
    @FXML
    private TableColumn<Kit, String> colTipoDisp, colTipoMant;

    public KitsController(KitUseCase useCase) {
        this.useCase = useCase;
    }

    @FXML
    public void initialize() {
        cmbTipoKit.setItems(FXCollections.observableArrayList(TipoKit.values()));

        colIdDisp.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipoDisp.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        colIdMant.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipoMant.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        cargarTablas();
    }

    @FXML
    private void registrarKit() {
        if (cmbTipoKit.getValue() != null) {
            useCase.registrarNuevoKit(cmbTipoKit.getValue());
            cmbTipoKit.setValue(null);
            cargarTablas();
        } else {
            new Alert(Alert.AlertType.WARNING, "Seleccione un tipo de kit.").showAndWait();
        }
    }

    @FXML
    private void repararKit() {
        try {
            useCase.repararKitEnBodega();
            cargarTablas();
            new Alert(Alert.AlertType.INFORMATION, "Kit reparado y disponible.").showAndWait();
        }
        catch (IllegalStateException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
        }
    }

    private void cargarTablas() {
        ObservableList<Kit> disp = FXCollections.observableArrayList();
        ObservableList<Kit> mant = FXCollections.observableArrayList();

        Stack<Kit> pilaDisponibles = useCase.obtenerPilaDisponibles();
        if (pilaDisponibles != null && !pilaDisponibles.isEmpty()) {
            Stack<Kit> clonDisp = pilaDisponibles.clone();
            while (!clonDisp.isEmpty()) {
                disp.add(clonDisp.pop());
            }
        }

        Stack<Kit> pilaMantenimiento = useCase.obtenerPilaMantenimiento();
        if (pilaMantenimiento != null && !pilaMantenimiento.isEmpty()) {
            Stack<Kit> clonMant = pilaMantenimiento.clone();
            while (!clonMant.isEmpty()) {
                mant.add(clonMant.pop());
            }
        }

        tablaDisponibles.setItems(disp);
        tablaMantenimiento.setItems(mant);
    }
}
