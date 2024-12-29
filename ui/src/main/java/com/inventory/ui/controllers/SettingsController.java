package com.inventory.ui.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class SettingsController {

    @FXML
    private TextField apiUrlField;

    @FXML
    private CheckBox enableNotifications;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    // Called when the save button is clicked
    @FXML
    private void handleSave() {
        String apiUrl = apiUrlField.getText();
        boolean notificationsEnabled = enableNotifications.isSelected();

        // Save settings (to a file or configuration service)
        System.out.println("API URL: " + apiUrl);
        System.out.println("Notifications Enabled: " + notificationsEnabled);

        // Add actual saving logic here
    }

    // Called when the cancel button is clicked
    @FXML
    private void handleCancel() {
        // Reset fields or close the settings window
        System.out.println("Settings changes canceled.");
    }
}

