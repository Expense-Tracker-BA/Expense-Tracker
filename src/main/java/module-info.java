module Frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens Frontend to javafx.fxml;
    exports Frontend;
}