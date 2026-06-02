module co.edu.udistrital.auto.rescate {
    requires javafx.controls;
    requires javafx.fxml;

    exports co.edu.udistrital.controller;
   
    opens co.edu.udistrital.controller to javafx.fxml;
}
