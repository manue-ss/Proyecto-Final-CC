package co.edu.udistrital.model.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class GestorArchivosBinarios<T> {

    public void guardarDatos(String rutaArchivo, T datos) {
        try {
            FileOutputStream archivoSalida = new FileOutputStream(rutaArchivo);
            ObjectOutputStream canalSalida = new ObjectOutputStream(archivoSalida);

            canalSalida.writeObject(datos);

            canalSalida.close();
            archivoSalida.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public T cargarDatos(String rutaArchivo) {

        T recuperado = null;

        try {
            FileInputStream archivoEntrada = new FileInputStream(rutaArchivo);
            ObjectInputStream canalEntrada = new ObjectInputStream(archivoEntrada);

            recuperado = (T) canalEntrada.readObject();

            canalEntrada.close();
            archivoEntrada.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return recuperado;
    }
}
