module com.inventory.ui {

    requires org.json;
    requires java.sql;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires javafx.swing;
    requires atlantafx.base;
    requires javafx.fxml;
    requires static lombok;

//    opens com.inventory.ui.models to javafx.base;
    opens com.inventory.ui.controllers to javafx.fxml;
    exports com.inventory.ui;
}