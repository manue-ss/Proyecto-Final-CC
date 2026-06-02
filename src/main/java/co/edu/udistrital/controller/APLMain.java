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
        ClienteDAO clienteDao = new ClienteDAO();

        AdministracionUseCase administracionUseCase = new AdministracionUseCase(
                solicitudDAO, tecnicoDAO, unidadServicioDAO, kitDAO,
                operacionDAO);
        ClienteUseCase ClienteUseCase = new ClienteUseCase(clienteDao);
        KitUseCase kitUseCase = new KitUseCase(kitDAO);
        SolicitudUseCase solicitudUseCase = new SolicitudUseCase(solicitudDAO,
                tecnicoDAO, unidadServicioDAO, kitDAO, operacionDAO);
        TecnicoUseCase tecnicoUseCase = new TecnicoUseCase(tecnicoDAO, operacionDAO);
        UnidadUseCase unidadUseCase = new UnidadUseCase(unidadServicioDAO, operacionDAO);

        Navigator navegador = new Navigator(primaryStage);

        Callback<Class<?>, Object> factory = clase -> {
            /*
            if (clase == ControladorMenu.class) {
                return new ControladorMenu(navegador);
            }
            if (clase == ControladorEmergencias.class) {
                // Inyectamos la lógica de negocio puramente en memoria
                return new ControladorEmergencias(navegador, emergenciaUC);
            }
             */
            return null;
        };

        navegador.setControllerFactory(factory);

        // navegador.navegar(Rutas.TEMPLATE.getPath()); 
    }

    @Override
    public void stop() throws Exception {

        System.out.println("Apagando la aplicación... Cerrando hilos de JavaFX.");
        super.stop();
    }
}
