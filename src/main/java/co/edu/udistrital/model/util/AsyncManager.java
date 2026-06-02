/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.udistrital.model.util;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.application.Platform;


public class AsyncManager {


    public static <T> void ejecutarAsync(Supplier<T> tareaPesada, Consumer<T> actualizacionUI) {
        CompletableFuture.supplyAsync(tareaPesada)
                .thenAcceptAsync(actualizacionUI, Platform::runLater)
                .exceptionally(ex -> {
                    System.err.println("Error en la operación en segundo plano: " + ex.getMessage());
                    return null;
                });
    }
}
