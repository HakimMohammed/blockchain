module com.inventory.ui {
    requires org.json;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires javafx.swing;
    requires atlantafx.base;
    requires javafx.fxml;
    requires static lombok;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;

    opens com.inventory.ui.models to javafx.base, com.fasterxml.jackson.databind;
    exports com.inventory.ui.enums to com.fasterxml.jackson.databind;
    exports com.inventory.ui.models to com.fasterxml.jackson.databind;
    opens com.inventory.ui to javafx.base;
    opens com.inventory.ui.controllers to javafx.fxml;
    exports com.inventory.ui;
    exports com.inventory.ui.dtos to com.fasterxml.jackson.databind;
    exports com.inventory.ui.auth to com.fasterxml.jackson.databind;
}