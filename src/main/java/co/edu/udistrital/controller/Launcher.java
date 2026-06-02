package co.edu.udistrital.controller;

public class Launcher {
    
    // Este método main NO está en una clase que extiende de Application, 
    // por lo que la JVM lo ejecutará sin intentar hacer validaciones de JavaFX.
    public static void main(String[] args) {
        // Desde aquí llamamos al main real de tu aplicación
        APLMain.main(args);
    }
}