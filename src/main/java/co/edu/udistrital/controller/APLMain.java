package co.edu.udistrital.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;
// Aquí importarás tu GestorArchivosBinarios, Repositorios y Casos de Uso cuando existan

/**
 * Clase principal y Composition Root de la aplicación. Ensambla las
 * dependencias y arranca la interfaz gráfica.
 *
 * @author Manuel Salazar
 */
public class APLMain extends Application {

    public static void main(String[] args) {
        System.out.println("Arrancando sistema AutoRescate 24/7!");

        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        // --- 1. INSTANCIAR MOTOR DE PERSISTENCIA ---
        // GestorArchivosBinarios gestorBinario = new GestorArchivosBinarios();
        // --- 2. ENSAMBLAR REPOSITORIOS Y CASOS DE USO ---
        /* En lugar de una conexión SQL, los repositorios reciben el gestor binario 
           (o instancian el suyo propio si lo haces estático) para cargar los archivos .dat */
        // RepositorioCasos repoCasos = new RepositorioCasosImpl(gestorBinario);
        // AtenderEmergenciaUseCase emergenciaUC = new AtenderEmergenciaUseCase(repoCasos);
        // --- 3. CONFIGURAR NAVEGACIÓN ---
        Navigator navegador = new Navigator(primaryStage);

        // --- 4. FÁBRICA DE CONTROLADORES (INYECCIÓN A LA VISTA) ---
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
            return null; // Fallback mientras no haya controladores
        };

        navegador.setControllerFactory(factory);

        // ¡Usando tu nuevo Enum de rutas!
        // navegador.navegar(Rutas.TEMPLATE.getPath()); 
    }

    @Override
    public void stop() throws Exception {
        // Al usar archivos binarios, no hay conexiones de red que mantener abiertas ni cerrar.
        // Las escrituras se hacen instantáneamente en el disco durante la ejecución.
        System.out.println("Apagando la aplicación... Cerrando hilos de JavaFX.");
        super.stop();
    }
}
