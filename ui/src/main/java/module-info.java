module com.inventory.ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.json;
    requires java.sql;

//    opens com.inventory.ui.models to javafx.base;
//    opens com.inventory.ui.controllers to javafx.fxml;
    exports com.inventory.ui;
}