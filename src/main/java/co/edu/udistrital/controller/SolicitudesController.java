package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.Solicitud;
import co.edu.udistrital.model.entities.Tecnico;
import co.edu.udistrital.model.entities.UnidadServicio;
import co.edu.udistrital.model.enums.NivelCriticidad;
import co.edu.udistrital.model.enums.TipoSolicitud;
import co.edu.udistrital.model.structures.SimpleLinkedList;
import co.edu.udistrital.model.structures.DoubleLinkedList;
import co.edu.udistrital.model.usecases.SolicitudUseCase;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

public class SolicitudesController {

    private final SolicitudUseCase useCase;

    @FXML
    private TextField txtIdCliente;
    @FXML
    private ComboBox<TipoSolicitud> cmbTipo;
    @FXML
    private ComboBox<NivelCriticidad> cmbCriticidad;
    @FXML
    private TextField txtDescripcion;

    @FXML
    private TableView<Solicitud> tablaSolicitudes;
    @FXML
    private TableColumn<Solicitud, Integer> colId;
    @FXML
    private TableColumn<Solicitud, String> colTipo;
    @FXML
    private TableColumn<Solicitud, String> colCriticidad;
    @FXML
    private TableColumn<Solicitud, String> colEstado;
    @FXML
    private TableColumn<Solicitud, String> colTecnico;
    @FXML
    private TableColumn<Solicitud, String> colUnidad;
    @FXML
    private TableColumn<Solicitud, String> colKit;
    @FXML
    private TableColumn<Solicitud, String> colFecha;
    @FXML
    private TableColumn<Solicitud, String> colDescripcion;

    @FXML
    private ComboBox<Integer> cmbTecnicosDisponibles;
    @FXML
    private ComboBox<String> cmbUnidadesDisponibles;
    @FXML
    private CheckBox chkRequiereKit;
    @FXML
    private CheckBox chkKitDanado;

    public SolicitudesController(SolicitudUseCase useCase) {
        this.useCase = useCase;
    }

    @FXML
    public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList(TipoSolicitud.values()));
        cmbCriticidad.setItems(FXCollections.observableArrayList(NivelCriticidad.values()));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colCriticidad.setCellValueFactory(new PropertyValueFactory<>("criticidad"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        colTecnico.setCellValueFactory(cellData -> {
            Integer idTecnico = cellData.getValue().getIdTecnico();
            return new SimpleStringProperty(idTecnico == null || idTecnico == 0 ? "N/A" : String.valueOf(idTecnico));
        });

        colUnidad.setCellValueFactory(cellData -> {
            String uuid = cellData.getValue().getUuid();
            return new SimpleStringProperty(uuid == null || uuid.trim().isEmpty() ? "N/A" : uuid);
        });

        colKit.setCellValueFactory(cellData -> {
            int idKit = cellData.getValue().getIdKit();
            return new SimpleStringProperty(idKit == 0 ? "No lleva" : "Kit #" + idKit);
        });

        colFecha.setCellValueFactory(cellData -> {
            LocalDate fecha = cellData.getValue().getFechaResolucion();
            return new SimpleStringProperty(fecha == null ? "Pendiente" : fecha.toString());
        });

        actualizarDatosVisuales();

        tablaSolicitudes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null && newSel.getEstado() == co.edu.udistrital.model.enums.EstadoSolicitud.PENDIENTE) {
                Solicitud prox = useCase.obtenerProximaSolicitud();
                if (prox != null && newSel.getId() != prox.getId()) {
                    javafx.application.Platform.runLater(()
                            -> tablaSolicitudes.getSelectionModel().select(prox)
                    );
                }
            }
        });
    }

    @FXML
    private void registrarSolicitud() {
        if (txtIdCliente.getText().trim().isEmpty() || txtDescripcion.getText().trim().isEmpty()
                || cmbTipo.getValue() == null || cmbCriticidad.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Debe llenar todos los campos del formulario.");
            return;
        }

        try {
            String idCliente = txtIdCliente.getText().trim();
            useCase.registrarSolicitud(idCliente, txtDescripcion.getText().trim(), cmbTipo.getValue(), cmbCriticidad.getValue());

            txtIdCliente.clear();
            txtDescripcion.clear();
            cmbTipo.setValue(null);
            cmbCriticidad.setValue(null);
            actualizarDatosVisuales();
            EventoGlobal.notificarCambio();
        }
        catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    private void despacharSolicitud() {
        Integer idTecnico = cmbTecnicosDisponibles.getValue();
        String uuidUnidad = cmbUnidadesDisponibles.getValue();

        if (idTecnico == null || uuidUnidad == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Debe seleccionar un Técnico y una Unidad para despachar.");
            return;
        }

        try {
            useCase.despacharServicio(idTecnico, uuidUnidad, chkRequiereKit.isSelected());
            chkRequiereKit.setSelected(false);
            actualizarDatosVisuales();
            EventoGlobal.notificarCambio();
        }
        catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    private void finalizarSolicitud() {
        Solicitud s = tablaSolicitudes.getSelectionModel().getSelectedItem();
        if (s != null) {
            try {
                useCase.finalizarSolicitudAtendida(s.getId(), chkKitDanado.isSelected());
                chkKitDanado.setSelected(false);
                actualizarDatosVisuales();
                EventoGlobal.notificarCambio();
            }
            catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccione una solicitud en ejecución de la tabla.");
        }
    }

    private void actualizarDatosVisuales() {
        ObservableList<Solicitud> listaSol = FXCollections.observableArrayList();
        SimpleLinkedList<Solicitud> datosSol = useCase.obtenerTodasLasSolicitudes();
        if (datosSol != null) {
            for (Solicitud s : datosSol) {
                listaSol.add(s);
            }
        }
        tablaSolicitudes.setItems(listaSol);

        ObservableList<Integer> listaTecnicos = FXCollections.observableArrayList();
        DoubleLinkedList<Tecnico> tecnicosDisp = useCase.obtenerTecnicosDisponibles();
        if (tecnicosDisp != null) {
            for (Tecnico t : tecnicosDisp) {
                listaTecnicos.add(t.getId());
            }
        }
        cmbTecnicosDisponibles.setItems(listaTecnicos);

        ObservableList<String> listaUnidades = FXCollections.observableArrayList();
        DoubleLinkedList<UnidadServicio> unidadesDisp = useCase.obtenerUnidadesDisponibles();
        if (unidadesDisp != null) {
            for (UnidadServicio u : unidadesDisp) {
                listaUnidades.add(u.getUuid());
            }
        }
        cmbUnidadesDisponibles.setItems(listaUnidades);
        tablaSolicitudes.refresh();

        Solicitud proxima = useCase.obtenerProximaSolicitud();
        if (proxima != null) {
            tablaSolicitudes.getSelectionModel().select(proxima);
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String msg) {
        new Alert(tipo, msg).showAndWait();
    }
}
