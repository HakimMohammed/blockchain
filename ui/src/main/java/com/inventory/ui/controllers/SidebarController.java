package com.inventory.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class SidebarController {

    @Setter
    private DashboardController dashboardController;

    @FXML
    private Button exchangesButton;
    @FXML
    private Button productsButton;

    @FXML
    private Button reportsButton;

    private Button selectedButton;

    @FXML
    private void initialize() {
        setupButton(exchangesButton, Feather.REFRESH_CCW, "exchange-table-view.fxml");
        setupButton(productsButton, Feather.PACKAGE, "product-table-view.fxml");
        setupButton(reportsButton, Feather.FILE_TEXT, "report-view.fxml");
        markSelectedButton(productsButton);
    }

    private void setupButton(Button button, Feather icon, String page) {
        button.setGraphic(new FontIcon(icon));
        button.setOnAction(event -> {
            if (dashboardController != null) {
                dashboardController.loadPage(page);
            }
            markSelectedButton(button);
        });
    }

    private void markSelectedButton(Button button) {
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove("selected-button");
        }
        button.getStyleClass().add("selected-button");
        selectedButton = button;
    }
}