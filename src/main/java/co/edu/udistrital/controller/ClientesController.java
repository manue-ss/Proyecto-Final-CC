package co.edu.udistrital.controller;

import co.edu.udistrital.model.usecases.ClienteUseCase;
import javafx.fxml.FXML;

public class ClientesController {
    private final ClienteUseCase useCase;

    public ClientesController(ClienteUseCase useCase) {
        this.useCase = useCase;
    }

    @FXML public void initialize() { }
}