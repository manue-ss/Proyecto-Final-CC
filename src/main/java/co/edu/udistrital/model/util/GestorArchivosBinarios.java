package co.edu.udistrital.model.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class GestorArchivosBinarios<T> {

    public void guardarDatos(String rutaArchivo, T datos) {
        try (FileOutputStream archivoSalida = new FileOutputStream(rutaArchivo); ObjectOutputStream canalSalida = new ObjectOutputStream(archivoSalida)) {

            canalSalida.writeObject(datos);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public T cargarDatos(String rutaArchivo) {

        T recuperado = null;

        try (FileInputStream archivoEntrada = new FileInputStream(rutaArchivo); ObjectInputStream canalEntrada = new ObjectInputStream(archivoEntrada)) {

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
