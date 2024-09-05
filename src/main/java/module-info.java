module Frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens Backend.Business_Layer to javafx.fxml, javafx.base;
    opens Backend.Service_Layer to javafx.fxml, javafx.base;
    opens Frontend to javafx.fxml;
    exports Frontend;
    exports Backend.Business_Layer;
    exports Backend.Service_Layer;
}