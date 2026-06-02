package co.edu.udistrital.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Navigator {

    private final Stage stage;
    private Callback<Class<?>, Object> controllerFactory;

    public Navigator(Stage stage) {
        this.stage = stage;
    }

    public void setControllerFactory(Callback<Class<?>, Object> controllerFactory) {
        this.controllerFactory = controllerFactory;
    }

    /**
     * Carga un FXML y reemplaza TODA la ventana (Ideal para el Login o Dashboard principal).
     */
    public void navegar(Paths rutaDestino) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaDestino.getPath()));

            if (controllerFactory != null) {
                loader.setControllerFactory(controllerFactory);
            }

            Parent root = loader.load();
            // Tamaño ajustado para una pantalla de gestión operativa
            stage.setScene(new Scene(root, 1200, 700)); 
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            System.err.println("Error al navegar a la ventana principal: " + rutaDestino.getPath());
            e.printStackTrace();
        }
    }

    /**
     * Carga un FXML, le inyecta su controlador y devuelve el nodo (Parent) 
     * para incrustarlo dentro de otra vista dinámica.
     */
    public Parent cargarVistaDinamica(Paths rutaDestino) {
        try {
            // Validar que el archivo exista antes de cargarlo para evitar fallos si aún no lo has creado
            if (getClass().getResource(rutaDestino.getPath()) == null) {
                System.out.println("Aviso: El archivo FXML " + rutaDestino.getPath() + " aún no ha sido creado.");
                return null;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaDestino.getPath()));
            
            if (controllerFactory != null) {
                loader.setControllerFactory(controllerFactory);
            }
            
            return loader.load();
            
        } catch (Exception e) {
            System.err.println("Error interno al cargar la sub-vista: " + rutaDestino.getPath());
            e.printStackTrace();
            return null;
        }
    }
}