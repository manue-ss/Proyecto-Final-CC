package co.edu.udistrital.controller;

import co.edu.udistrital.model.enums.EstadoUnidad;
import co.edu.udistrital.model.usecases.UnidadUseCase;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;

public class DashboardController {

    @FXML private Label lblDisponibles;
    @FXML private Label lblOcupadas;
    @FXML private Label lblMantenimiento;
    @FXML private StackPane panelContenido;

    private final UnidadUseCase unidadUseCase;
    private final Navigator navegador;

    public DashboardController(UnidadUseCase unidadUseCase, Navigator navegador) {
        this.unidadUseCase = unidadUseCase;
        this.navegador = navegador;
    }

    @FXML public void initialize() {
        co.edu.udistrital.controller.EventoGlobal.cambiarDatos = this::cargarIndicadoresAsync;
        cargarIndicadoresAsync();
        abrirSolicitudes();
    }

    private void cargarIndicadoresAsync() {
        int disponibles = unidadUseCase.obtenerUnidadesPorEstado(EstadoUnidad.DISPONIBLE).size();
        int ocupadas = unidadUseCase.obtenerUnidadesPorEstado(EstadoUnidad.OCUPADA).size();
        int mantenimiento = unidadUseCase.obtenerUnidadesPorEstado(EstadoUnidad.EN_MANTENIMIENTO).size();

        lblDisponibles.setText(String.valueOf(disponibles));
        lblOcupadas.setText(String.valueOf(ocupadas));
        lblMantenimiento.setText(String.valueOf(mantenimiento));
    }

    @FXML private void abrirSolicitudes() {
        incrustarVista(Paths.MODULO_SOLICITUDES);
    }

    @FXML private void abrirUnidades() {
        incrustarVista(Paths.MODULO_UNIDADES);
    }

    @FXML private void abrirTecnicos() {
        incrustarVista(Paths.MODULO_TECNICOS);
    }

    @FXML private void abrirKits() {
        incrustarVista(Paths.MODULO_KITS);
    }

    @FXML private void abrirClientes() {
        incrustarVista(Paths.MODULO_CLIENTES);
    }

    @FXML
    private void abrirAdministracion() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Acceso Restringido");
        dialog.setHeaderText("Ingrese la contraseña:");

        PasswordField pwd = new PasswordField();
        VBox content = new VBox(pwd);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return pwd.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(password -> {
            if ("1234".equals(password)) {
                incrustarVista(Paths.MODULO_ADMINISTRACION);
            } else {
                new Alert(Alert.AlertType.ERROR, "Contraseña incorrecta. Acceso denegado.").showAndWait();
            }
        });
    }

    private void incrustarVista(Paths ruta) {

        cargarIndicadoresAsync();

        Parent nuevaVista = navegador.cargarVistaDinamica(ruta);
        if (nuevaVista != null) {
            panelContenido.getChildren().clear();
            panelContenido.getChildren().add(nuevaVista);
        } else {
            panelContenido.getChildren().clear();
            Label lblConstruccion = new Label("Módulo en construcción: " + ruta.name());
            lblConstruccion.setStyle("-fx-font-size: 24px; -fx-text-fill: #7f8c8d;");
            panelContenido.getChildren().add(lblConstruccion);
        }
    }
}
