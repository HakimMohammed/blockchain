module com.inventory.ui {

    requires org.json;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires javafx.swing;
    requires atlantafx.base;
    requires javafx.fxml;
    requires static lombok;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;
    requires org.kordamp.ikonli.feather;
    requires org.kordamp.ikonli.javafx;
    requires java.management;
    requires java.net.http;
    requires org.java_websocket;
    requires org.jetbrains.annotations;

    exports com.inventory.ui;
    exports com.inventory.ui.services;
    exports com.inventory.ui.models;
    exports com.inventory.ui.dtos.auth;
    exports com.inventory.ui.dtos.company_demand;
    exports com.inventory.ui.dtos.exchange;
    exports com.inventory.ui.enums to com.fasterxml.jackson.databind;

    opens com.inventory.ui.controllers to javafx.fxml;
    opens com.inventory.ui.dtos.auth to com.fasterxml.jackson.databind;
    opens com.inventory.ui.dtos.company_demand to com.fasterxml.jackson.databind;
    opens com.inventory.ui.auth to com.fasterxml.jackson.databind;
    opens com.inventory.ui to javafx.fxml;
    opens com.inventory.ui.services to javafx.fxml;
    opens com.inventory.ui.models to javafx.fxml , com.fasterxml.jackson.databind;
    exports com.inventory.ui.sockets to javafx.graphics;

}