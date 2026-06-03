package co.edu.udistrital.controller;

import co.edu.udistrital.model.entities.Solicitud;
import co.edu.udistrital.model.entities.Tecnico;
import co.edu.udistrital.model.entities.UnidadServicio;
import co.edu.udistrital.model.enums.NivelCriticidad;
import co.edu.udistrital.model.enums.TipoSolicitud;
import co.edu.udistrital.model.enums.Zonas;
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
    private ComboBox<Zonas> cmbZona;
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
    private TableColumn<Solicitud, String> colZona;
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
    private ComboBox<Tecnico> cmbTecnicosDisponibles;
    @FXML
    private ComboBox<UnidadServicio> cmbUnidadesDisponibles;
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
        cmbZona.setItems(FXCollections.observableArrayList(Zonas.values()));

        cmbTecnicosDisponibles.setConverter(new javafx.util.StringConverter<Tecnico>() {
            @Override
            public String toString(Tecnico t) {
                return t == null ? null : t.getId() + " - " + t.getEspecialidad();
            }

            @Override
            public Tecnico fromString(String s) {
                return null;
            }
        });

        cmbUnidadesDisponibles.setConverter(new javafx.util.StringConverter<UnidadServicio>() {
            @Override
            public String toString(UnidadServicio u) {
                return u == null ? null : u.getUuid().substring(0, 8) + " (" + u.getTipo() + ")";
            }

            @Override
            public UnidadServicio fromString(String s) {
                return null;
            }
        });

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colCriticidad.setCellValueFactory(new PropertyValueFactory<>("criticidad"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zona"));
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
            Solicitud prox = useCase.obtenerProximaSolicitud();
            if (newSel != null && prox != null && newSel.getEstado() == co.edu.udistrital.model.enums.EstadoSolicitud.PENDIENTE) {
                if (newSel.getId() != prox.getId()) {
                    javafx.application.Platform.runLater(() -> tablaSolicitudes.getSelectionModel().select(prox));
                }
            }
        });
    }

    @FXML
    private void registrarSolicitud() {
        if (txtIdCliente.getText().trim().isEmpty() || txtDescripcion.getText().trim().isEmpty()
                || cmbTipo.getValue() == null || cmbCriticidad.getValue() == null || cmbZona.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Debe llenar todos los campos del formulario.");
            return;
        }

        try {
            String idCliente = txtIdCliente.getText().trim();
            useCase.registrarSolicitud(idCliente, txtDescripcion.getText().trim(), cmbTipo.getValue(), cmbCriticidad.getValue(), cmbZona.getValue());

            txtIdCliente.clear();
            txtDescripcion.clear();
            cmbTipo.setValue(null);
            cmbCriticidad.setValue(null);
            cmbZona.setValue(null);
            actualizarDatosVisuales();
            // EventoGlobal.notificarCambio();
        }
        catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    private void despacharSolicitud() {
        Tecnico t = cmbTecnicosDisponibles.getValue();
        UnidadServicio u = cmbUnidadesDisponibles.getValue();

        if (t == null || u == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Debe seleccionar un Técnico y una Unidad para despachar.");
            return;
        }

        try {
            useCase.despacharServicio(t.getId(), u.getUuid(), chkRequiereKit.isSelected());
            chkRequiereKit.setSelected(false);
            actualizarDatosVisuales();
            // EventoGlobal.notificarCambio();
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
                // EventoGlobal.notificarCambio();
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
        tablaSolicitudes.refresh();

        Solicitud proxima = useCase.obtenerProximaSolicitud();
        if (proxima != null) {
            // Auto seleccionar en tabla
            tablaSolicitudes.getSelectionModel().select(proxima);

            // CARGAMOS COMBOS BASADOS PURAMENTE EN LA PRÓXIMA (La de mayor prioridad)
            ObservableList<Tecnico> obsTechs = FXCollections.observableArrayList();
            DoubleLinkedList<Tecnico> techs = useCase.obtenerTecnicosDisponibles(proxima.getZona());
            if (techs != null) {
                for (Tecnico t : techs) {
                    obsTechs.add(t);
                }
            }

            ObservableList<UnidadServicio> obsUnits = FXCollections.observableArrayList();
            DoubleLinkedList<UnidadServicio> units = useCase.obtenerUnidadesDisponibles(proxima.getZona());
            if (units != null) {
                for (UnidadServicio u : units) {
                    obsUnits.add(u);
                }
            }

            cmbTecnicosDisponibles.setItems(obsTechs);
            cmbUnidadesDisponibles.setItems(obsUnits);
        } else {
            tablaSolicitudes.getSelectionModel().clearSelection();
            cmbTecnicosDisponibles.setItems(FXCollections.observableArrayList());
            cmbUnidadesDisponibles.setItems(FXCollections.observableArrayList());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String msg) {
        new Alert(tipo, msg).showAndWait();
    }
}
