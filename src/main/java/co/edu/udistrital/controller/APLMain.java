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

        // 1. Instanciar DAOs (Acceso a Datos)
        KitDAO kitDAO = new KitDAO();
        OperacionDAO operacionDAO = new OperacionDAO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        TecnicoDAO tecnicoDAO = new TecnicoDAO();
        UnidadServicioDAO unidadServicioDAO = new UnidadServicioDAO();
        ClienteDAO clienteDao = new ClienteDAO();

        // 2. Instanciar Casos de Uso (Lógica de Negocio)
        AdministracionUseCase administracionUseCase = new AdministracionUseCase(solicitudDAO, tecnicoDAO, unidadServicioDAO, kitDAO, operacionDAO);
        ClienteUseCase clienteUseCase = new ClienteUseCase(clienteDao);
        KitUseCase kitUseCase = new KitUseCase(kitDAO);
        SolicitudUseCase solicitudUseCase = new SolicitudUseCase(solicitudDAO, tecnicoDAO, unidadServicioDAO, kitDAO, operacionDAO);
        TecnicoUseCase tecnicoUseCase = new TecnicoUseCase(tecnicoDAO, operacionDAO);
        UnidadUseCase unidadUseCase = new UnidadUseCase(unidadServicioDAO, operacionDAO);

        // 3. Instanciar el Navegador
        Navigator navegador = new Navigator(primaryStage);

        // 4. Configurar la Inyección de Dependencias (Controller Factory)
        Callback<Class<?>, Object> factory = clase -> {
            
            // Inyección para el Dashboard Principal
            if (clase == DashboardController.class) {
                return new DashboardController(unidadServicioDAO, navegador);
            }
            
            // --- AQUÍ REGISTRARÁS TUS CONTROLADORES A MEDIDA QUE LOS CREES ---
            /* Ejemplo: Descomenta esto cuando crees el SolicitudesController.java
            if (clase == SolicitudesController.class) {
                return new SolicitudesController(solicitudUseCase);
            }
            */

            // Retorno por defecto si el controlador no requiere dependencias especiales
            try {
                return clase.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                System.err.println("No se pudo instanciar el controlador: " + clase.getName());
                return null;
            }
        };

        navegador.setControllerFactory(factory);

        // 5. Arrancar la aplicación cargando el Dashboard
        primaryStage.setTitle("Central Operativa - AutoRescate");
        navegador.navegar(Paths.DASHBOARD); 
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Apagando la aplicación... Cerrando hilos de JavaFX.");
        super.stop();
    }
}