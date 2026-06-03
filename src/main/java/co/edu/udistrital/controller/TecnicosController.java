package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.Tecnico;
import co.edu.udistrital.model.enums.Especialidades;
import co.edu.udistrital.model.enums.Zonas;
import co.edu.udistrital.model.usecases.TecnicoUseCase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TecnicosController {

    private final TecnicoUseCase useCase;

    @FXML
    private ComboBox<Especialidades> cmbEspecialidad;
    @FXML
    private ComboBox<Zonas> cmbZona;

    @FXML
    private TableView<Tecnico> tablaTecnicos;
    @FXML
    private TableColumn<Tecnico, Integer> colId;
    @FXML
    private TableColumn<Tecnico, String> colEspecialidad;
    @FXML
    private TableColumn<Tecnico, String> colZona;
    @FXML
    private TableColumn<Tecnico, String> colEstado;
    @FXML
    private TableColumn<Tecnico, String> colDisponibilidad;

    public TecnicosController(TecnicoUseCase useCase) {
        this.useCase = useCase;
    }

    @FXML
    public void initialize() {
        cmbEspecialidad.setItems(FXCollections.observableArrayList(Especialidades.values()));
        cmbZona.setItems(FXCollections.observableArrayList(Zonas.values()));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zona"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDisponibilidad.setCellValueFactory(cellData -> {
            boolean esDisponible = cellData.getValue().isDisponible();
            return new javafx.beans.property.SimpleStringProperty(esDisponible ? "Sí" : "No");
        });
        cargarTabla();
    }

    @FXML
    private void registrarTecnico() {
        if (cmbEspecialidad.getValue() == null || cmbZona.getValue() == null) {
            return;
        }
        useCase.registrarNuevoTecnico(cmbEspecialidad.getValue(), cmbZona.getValue());
        cmbEspecialidad.setValue(null);
        cmbZona.setValue(null);
        cargarTabla();
    }

    @FXML
    private void enviarDescanso() {
        Tecnico seleccionado = tablaTecnicos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            useCase.enviarTecnicoADescanso(seleccionado.getId());
            cargarTabla();
        } else {
            mostrarAlerta();
        }
    }

    @FXML
    private void retornarDescanso() {
        Tecnico seleccionado = tablaTecnicos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            useCase.retornarTecnicoDeDescanso(seleccionado.getId());
            cargarTabla();
        } else {
            mostrarAlerta();
        }
    }

    @FXML
    private void despedir() {
        Tecnico seleccionado = tablaTecnicos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            useCase.despedirTecnico(seleccionado.getId());
            cargarTabla();
        } else {
            mostrarAlerta();
        }
    }

    private void cargarTabla() {
        ObservableList<Tecnico> lista = FXCollections.observableArrayList();
        Iterable<Tecnico> datos = useCase.obtenerTodos();
        if (datos != null) {
            for (Tecnico t : datos) {
                lista.add(t);
            }
        }
        tablaTecnicos.setItems(lista);
        tablaTecnicos.refresh();
    }

    private void mostrarAlerta() {
        new Alert(Alert.AlertType.WARNING, "Seleccione un técnico de la tabla primero.").showAndWait();
    }
}
