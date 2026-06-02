module co.edu.udistrital.auto.rescate {
    // 1. Dependencias externas (Lo que tu proyecto necesita)
    requires javafx.controls;
    requires javafx.fxml;

    // 2. Permisos para el Controlador
    // Exportamos el paquete controller para que JavaFX pueda arrancar APLMain
    exports co.edu.udistrital.controller;
    exports co.edu.udistrital.model.entities;
    // Abrimos el paquete controller a javafx.fxml para que @FXML funcione en tus controladores
    opens co.edu.udistrital.controller to javafx.fxml;

    // 3. Permisos para el Modelo (Gson)
    // Exportamos el modelo por si otras partes de la app lo necesitan

    // Abrimos las entidades a Gson para que pueda convertirlas a JSON mediante Reflexión
    opens co.edu.udistrital.model.entities to co.edu.udistrital.model.usecases;
}
