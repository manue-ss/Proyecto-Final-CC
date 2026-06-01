package co.edu.udistrital.controller;

import co.edu.udistrital.model.util.DatabaseConnection;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Manuel Salazar
 */
public class APLMain extends Application {

    public static void main(String[] args) {
        System.out.println("Arrancando sistema!");
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        DatabaseConnection conexion = DatabaseConnection.getInstance();

        /* Este es un ejemplo de la inyección de dependencias, donde al repositorio 
        se el inyecta la conexion y a el caso de uso se le inyecta un repositorio*/
        //RepositorioCasos repoCasos = new RepositorioCasosImpl(conexion);
        //AtenderEmergenciaUseCase emergenciaUC = new AtenderEmergenciaUseCase(repoCasos);
        // 2. Crear el Navegador (solo necesita el Stage)
        Navigator navegador = new Navigator(primaryStage);

        // 3. Crear el ControllerFactory. 
        // ¡Gracias a la magia de las lambdas de Java, esta función "recuerda" 
        // las variables que creamos arriba sin tener que meterlas en el Navegador!
        Callback<Class<?>, Object> factory = clase -> {
            /*
            Aca se presenta un ejemplo de como se usa en controller factory al 
            momento de ejecutar el sistema, para evitar que javaFX cree un 
            controlador sin casos de uso
            
            if (clase == ControladorMenu.class) {
                return new ControladorMenu(navegador);
            }
            if (clase == ControladorEmergencias.class) {
                // Le inyectamos el caso de uso y el navegador para que pueda cambiar de vista luego
                return new ControladorEmergencias(navegador, emergenciaUC);
            }
            // ... otros controladores ...
             */
            return null;

        };

        // 4. Conectar la fábrica al navegador y arrancar
        navegador.setControllerFactory(factory);
        //navegador.navegar("/vistas/MenuPrincipal.fxml"); // Vista inicial
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Apagando la aplicación... Liberando recursos.");

        DatabaseConnection.getInstance().closeConnection();

        super.stop();
    }
}
