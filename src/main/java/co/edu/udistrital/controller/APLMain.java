package co.edu.udistrital.controller;

import co.edu.udistrital.model.daos.*;
import co.edu.udistrital.model.usecases.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;

public class APLMain extends Application {

    public static void main(String[] args) {
        System.out.println("Arrancando sistema AutoRescate 24/7!");
        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        KitDAO kitDAO = new KitDAO();
        OperacionDAO operacionDAO = new OperacionDAO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        TecnicoDAO tecnicoDAO = new TecnicoDAO();
        UnidadServicioDAO unidadServicioDAO = new UnidadServicioDAO();
        ClienteDAO clienteDAO = new ClienteDAO();

        AdministracionUseCase administracionUseCase = new AdministracionUseCase(solicitudDAO, tecnicoDAO, unidadServicioDAO, kitDAO, operacionDAO);
        ClienteUseCase clienteUseCase = new ClienteUseCase(clienteDAO);
        KitUseCase kitUseCase = new KitUseCase(kitDAO, operacionDAO);
        SolicitudUseCase solicitudUseCase = new SolicitudUseCase(solicitudDAO, tecnicoDAO, unidadServicioDAO, kitDAO, operacionDAO, clienteDAO);
        TecnicoUseCase tecnicoUseCase = new TecnicoUseCase(tecnicoDAO, operacionDAO);
        UnidadUseCase unidadUseCase = new UnidadUseCase(unidadServicioDAO, operacionDAO);

        Navigator navegador = new Navigator(primaryStage);

        Callback<Class<?>, Object> factory = clase -> {

            if (clase == DashboardController.class) {
                return new DashboardController(unidadUseCase, navegador);
            }
            if (clase == SolicitudesController.class) {
                return new SolicitudesController(solicitudUseCase);
            }
            if (clase == UnidadesController.class) {
                return new UnidadesController(unidadUseCase);
            }
            if (clase == TecnicosController.class) {
                return new TecnicosController(tecnicoUseCase);
            }
            if (clase == KitsController.class) {
                return new KitsController(kitUseCase);
            }
            if (clase == ClientesController.class) {
                return new ClientesController(clienteUseCase);
            }
            if (clase == AdministracionController.class) {
                return new AdministracionController(administracionUseCase);
            }

            try {
                return clase.getDeclaredConstructor().newInstance();
            }
            catch (Exception e) {
                System.err.println("No se pudo instanciar el controlador: " + clase.getName());
                return null;
            }
        };

        navegador.setControllerFactory(factory);

        primaryStage.setTitle("Central Operativa - AutoRescate");
        navegador.navegar(Paths.DASHBOARD);
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Apagando la aplicación... Cerrando hilos de JavaFX.");
        super.stop();
    }
}
