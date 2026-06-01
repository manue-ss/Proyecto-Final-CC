package co.edu.udistrital.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Manuel Salazar
 * @since 0.1
 */
public class Navigator {

    private final Stage stage;
    // Esta es la "máquina expendedora" de controladores que le dará el Main
    private Callback<Class<?>, Object> controllerFactory;

    public Navigator(Stage stage) {
        this.stage = stage;
    }

    public void setControllerFactory(Callback<Class<?>, Object> controllerFactory) {
        this.controllerFactory = controllerFactory;
    }

    /**
     * Carga un FXML y reemplaza la vista actual en la ventana.
     */
    public void navegar(Paths rutaDestino) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaDestino.getPath()));

            // Le decimos al loader que use nuestra fábrica de controladores
            if (controllerFactory != null) {
                loader.setControllerFactory(controllerFactory);
            }

            Parent root = loader.load();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

        }
        catch (Exception e) {
            System.err.println("Error al navegar a: " + rutaDestino.getPath());
            e.printStackTrace();
        }
    }
}
