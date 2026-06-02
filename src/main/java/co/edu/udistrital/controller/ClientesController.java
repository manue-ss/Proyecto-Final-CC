package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.Cliente;
import co.edu.udistrital.model.enums.TipoCliente;
import co.edu.udistrital.model.usecases.ClienteUseCase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.UUID;

public class ClientesController {

    private final ClienteUseCase useCase;

    @FXML private TextField txtNombre;
    @FXML private ComboBox<TipoCliente> cmbTipoCliente;
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colTipo;

    public ClientesController(ClienteUseCase useCase) { 
        this.useCase = useCase; 
    }

    @FXML public void initialize() {
        cmbTipoCliente.setItems(FXCollections.observableArrayList(TipoCliente.values()));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoCliente")); 
        cargarTabla();
    }

    @FXML private void registrarCliente() {
        if(txtNombre.getText().isEmpty() || cmbTipoCliente.getValue() == null) return;
        
        String idTemp = UUID.randomUUID().toString().substring(0, 8);
        useCase.registrarNuevoCliente(txtNombre.getText(), idTemp, cmbTipoCliente.getValue());
        
        txtNombre.clear(); 
        cmbTipoCliente.setValue(null); 
        cargarTabla();
    }

    private void cargarTabla() {
        ObservableList<Cliente> lista = FXCollections.observableArrayList();
        Iterable<Cliente> datos = useCase.obtenerTodos(); 
        if (datos != null) { for (Cliente c : datos) { lista.add(c); } }
        tablaClientes.setItems(lista);
    }
}