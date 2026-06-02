package co.edu.udistrital.model.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GestorArchivosBinarios<T> {

    private static final String RUTA_ABSOLUTA = "datos_app/";

    private void asegurarDirectorio() {
        try {
            Files.createDirectories(Paths.get(RUTA_ABSOLUTA));
        }
        catch (IOException e) {
            System.err.println("Error creando directorio de datos: " + e.getMessage());
        }
    }

    public void guardarDatos(String rutaArchivo, T datos) {
        asegurarDirectorio();
        try (FileOutputStream archivoSalida = new FileOutputStream(RUTA_ABSOLUTA + rutaArchivo); ObjectOutputStream canalSalida = new ObjectOutputStream(archivoSalida)) {
            canalSalida.writeObject(datos);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public T cargarDatos(String rutaArchivo) {
        asegurarDirectorio();
        T recuperado = null;
        try (FileInputStream archivoEntrada = new FileInputStream(RUTA_ABSOLUTA + rutaArchivo); ObjectInputStream canalEntrada = new ObjectInputStream(archivoEntrada)) {
            recuperado = (T) canalEntrada.readObject();
        }
        catch (java.io.FileNotFoundException e) {
            System.out.println("Nota: Archivo " + rutaArchivo + " no existe aún. Se creará al guardar.");
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return recuperado;
    }
}
