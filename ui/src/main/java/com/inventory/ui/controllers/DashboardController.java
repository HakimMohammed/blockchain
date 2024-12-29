package com.inventory.ui.controllers;

import com.inventory.ui.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DashboardController {
    @FXML
    private BorderPane mainContainer;

    public void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/" + fxmlFile));
            Parent page = loader.load();
            mainContainer.setCenter(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/sidebar-view.fxml"));
            Parent sidebar = loader.load();
            SidebarController sidebarController = loader.getController();
            sidebarController.setDashboardController(this);
            mainContainer.setRight(sidebar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}