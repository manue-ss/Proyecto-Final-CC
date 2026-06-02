package co.edu.udistrital.controller;

public class EventoGlobal {

    public static Runnable cambiarDatos;

    public static void notificarCambio() {
        if (cambiarDatos != null) {
            cambiarDatos.run();
        }
    }
}
