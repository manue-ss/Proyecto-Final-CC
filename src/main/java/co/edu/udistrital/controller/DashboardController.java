package co.edu.udistrital.controller;

import co.edu.udistrital.model.daos.UnidadServicioDAO;
import co.edu.udistrital.model.enums.EstadoUnidad;
import co.edu.udistrital.model.util.AsyncManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class DashboardController {

    @FXML private Label lblDisponibles;
    @FXML private Label lblOcupadas;
    @FXML private Label lblMantenimiento;
    @FXML private StackPane panelContenido;

    private final UnidadServicioDAO unidadDAO;
    private final Navigator navegador;

    public DashboardController(UnidadServicioDAO unidadDAO, Navigator navegador) {
        this.unidadDAO = unidadDAO;
        this.navegador = navegador;
    }

    @FXML
    public void initialize() {
        cargarIndicadoresAsync();
        
        // Intenta cargar la vista de solicitudes al arrancar. 
        // Si aún no has creado el FXML, el navegador atrapará el null y dejará el panel vacío pero seguro.
        abrirSolicitudes(); 
    }

    private void cargarIndicadoresAsync() {
        AsyncManager.ejecutarAsync(
            () -> new int[]{
                unidadDAO.getByState(EstadoUnidad.DISPONIBLE).size(),
                unidadDAO.getByState(EstadoUnidad.OCUPADA).size(),
                unidadDAO.getByState(EstadoUnidad.EN_MANTENIMIENTO).size()
            },
            (resultados) -> {
                lblDisponibles.setText(String.valueOf(resultados[0]));
                lblOcupadas.setText(String.valueOf(resultados[1]));
                lblMantenimiento.setText(String.valueOf(resultados[2]));
            }
        );
    }

    // --- Eventos de los Botones del Menú ---
    @FXML private void abrirSolicitudes() { incrustarVista(Paths.MODULO_SOLICITUDES); }
    @FXML private void abrirUnidades() { incrustarVista(Paths.MODULO_UNIDADES); }
    @FXML private void abrirTecnicos() { incrustarVista(Paths.MODULO_TECNICOS); }
    @FXML private void abrirKits() { incrustarVista(Paths.MODULO_KITS); }
    @FXML private void abrirClientes() { incrustarVista(Paths.MODULO_CLIENTES); }
    @FXML private void abrirAdministracion() { incrustarVista(Paths.MODULO_ADMINISTRACION); }

    private void incrustarVista(Paths ruta) {
        Parent nuevaVista = navegador.cargarVistaDinamica(ruta);
        if (nuevaVista != null) {
            panelContenido.getChildren().clear();
            panelContenido.getChildren().add(nuevaVista);
        } else {
            // Si el FXML no existe, puedes poner un Label temporal en la pantalla
            panelContenido.getChildren().clear();
            Label lblConstruccion = new Label("Módulo en construcción: " + ruta.name());
            lblConstruccion.setStyle("-fx-font-size: 24px; -fx-text-fill: #7f8c8d;");
            panelContenido.getChildren().add(lblConstruccion);
        }
    }
}