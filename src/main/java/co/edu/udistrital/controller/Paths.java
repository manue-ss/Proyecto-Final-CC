package co.edu.udistrital.controller;

public enum Paths {
    
    DASHBOARD("Dashboard.fxml"),
    MODULO_SOLICITUDES("SolicitudesView.fxml"),
    MODULO_TECNICOS("TecnicosView.fxml"),
    MODULO_UNIDADES("UnidadesView.fxml"),
    MODULO_KITS("KitsView.fxml"),
    MODULO_CLIENTES("ClientesView.fxml"),
    MODULO_ADMINISTRACION("AdministracionView.fxml");

    private final String RUTA;
    // CORRECCIÓN: Apuntamos directamente a la carpeta "fxml" que tienes en "resources"
    private static final String BASE_PATH = "/fxml/"; 

    Paths(String RUTA) {
        this.RUTA = RUTA;
    }

    public String getPath() {
        return BASE_PATH + this.RUTA;
    }
}