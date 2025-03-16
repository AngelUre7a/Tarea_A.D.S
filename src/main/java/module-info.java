module cr.ac.una.tarea_a.d.s {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens cr.ac.una.tarea_a.d.s to javafx.fxml;
    exports cr.ac.una.tarea_a.d.s.controller to javafx.fxml;
    exports cr.ac.una.tarea_a.d.s;
    exports cr.ac.una.tarea_a.d.s.controller to javafx.fxml;
    
}
