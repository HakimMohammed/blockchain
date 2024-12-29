package com.inventory.ui.controllers;

import com.inventory.ui.animations.Animations;
import com.inventory.ui.services.AuthenticationService;
import com.inventory.ui.services.UILoaderService;
import com.inventory.ui.utils.AlertUtils;
import com.inventory.ui.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private HBox loginContainer;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label statusLabel;

    private final AuthenticationService authService;

    public LoginController() {
        this.authService = new AuthenticationService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
        setupEventHandlers();
        Animations.fadeIn(loginContainer);
    }

    private void setupUI() {
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
    }

    private void setupEventHandlers() {
        loginButton.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!ValidationUtils.validateLoginInput(username, password)) {
            handleLoginError("Please fill in all fields");
            return;
        }

        try {
            boolean success = authService.login(username, password);
            if (success) {
                handleLoginSuccess();
                UILoaderService.loadDashboard();
            } else {
                handleLoginError("Invalid credentials");
            }
        } catch (Exception e) {
            handleLoginError("An error occurred during login");
        }
    }


    private void handleLoginSuccess() {
        AlertUtils.showSuccess(statusLabel, "Login successful!");
        // Add navigation logic here
    }

    private void handleLoginError(String message) {
        AlertUtils.showError(statusLabel, message);
        Animations.shake(loginButton);
    }
}