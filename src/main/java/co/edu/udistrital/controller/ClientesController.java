package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.Cliente;
import co.edu.udistrital.model.enums.TipoCliente;
import co.edu.udistrital.model.structures.SimpleLinkedList;
import co.edu.udistrital.model.usecases.ClienteUseCase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientesController {

    private final ClienteUseCase useCase;

    @FXML
    private TextField txtIdentificador;
    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<TipoCliente> cmbTipoCliente;

    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente, Integer> colId;
    @FXML
    private TableColumn<Cliente, String> colIdentificador;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, String> colTipo;

    public ClientesController(ClienteUseCase useCase) {
        this.useCase = useCase;
    }

    @FXML
    public void initialize() {
        cmbTipoCliente.setItems(FXCollections.observableArrayList(TipoCliente.values()));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdentificador.setCellValueFactory(new PropertyValueFactory<>("identificador"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        cargarTabla();
    }

    @FXML
    private void registrarCliente() {
        if (txtNombre.getText().trim().isEmpty() || txtIdentificador.getText().trim().isEmpty() || cmbTipoCliente.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor llene todos los campos.");
            return;
        }

        try {
            useCase.registrarNuevoCliente(txtNombre.getText().trim(), txtIdentificador.getText().trim(), cmbTipoCliente.getValue());

            txtNombre.clear();
            txtIdentificador.clear();
            cmbTipoCliente.setValue(null);

            cargarTabla();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente registrado correctamente.");

        }
        catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de validación", e.getMessage());
        }
    }

    private void cargarTabla() {
        ObservableList<Cliente> lista = FXCollections.observableArrayList();
        SimpleLinkedList<Cliente> datos = useCase.obtenerTodos();

        if (datos != null) {
            for (Cliente c : datos) {
                lista.add(c);
            }
        }
        tablaClientes.setItems(lista);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
