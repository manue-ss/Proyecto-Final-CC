package co.edu.udistrital.controller;

/**
 * Diccionario centralizado de todas las rutas de las vistas FXML.
 * * @author Manuel Salazar
 */
public enum Paths {
    
    // Ruta absoluta desde la raíz de resources, incluyendo la extensión
    TEMPLATE("template.fxml");

    private final String RUTA;
    
    private static final String BASE_PATH = "/co/edu/udistrital/view/";

    Paths(String RUTA) {
        this.RUTA =  RUTA;
    }

    public String getPath() {
        return BASE_PATH + this.RUTA;
    }
}