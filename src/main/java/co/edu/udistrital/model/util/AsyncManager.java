/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.udistrital.model.util;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.application.Platform;

/**
 * Clase utilitaria para manejar operaciones pesadas sin congelar la UI.
 *
 * @author Manuel Salazar
 * @since 0.1
 */
public class AsyncManager {

    /**
     * Ejecuta una tarea pesada en segundo plano y luego actualiza la interfaz.
     *
     * * @param tareaPesada     Lo que se hará en el fondo (Ej: Consulta a base
     *                           de datos).
     * @param actualizacionUI Lo que se hará en la pantalla con el resultado.
     * @param <T>             El tipo de dato que retorna la tarea pesada.
     */
    public static <T> void ejecutarAsync(Supplier<T> tareaPesada, Consumer<T> actualizacionUI) {
        CompletableFuture.supplyAsync(tareaPesada)
                .thenAcceptAsync(actualizacionUI, Platform::runLater)
                .exceptionally(ex -> {
                    System.err.println("Error en la operación en segundo plano: " + ex.getMessage());
                    return null;
                });
    }
}
