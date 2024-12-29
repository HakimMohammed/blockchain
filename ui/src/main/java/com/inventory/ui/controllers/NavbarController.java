package com.inventory.ui.controllers;

import com.inventory.ui.models.User;
import com.inventory.ui.services.AuthenticationService;
import com.inventory.ui.services.UILoaderService;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class NavbarController {
    @FXML
    private TextField searchField;
    @FXML
    private Button notificationsButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button logoutButton;

    private final AuthenticationService authenticationService;

    public NavbarController() {
        this.authenticationService = new AuthenticationService();
    }

    @FXML
    private void initialize() {
        notificationsButton.setGraphic(new FontIcon(Feather.BELL));
        profileButton.setGraphic(new FontIcon(Feather.USER));
        profileButton.setOnAction(event -> showUserProfileDialog());
        logoutButton.setGraphic(new FontIcon(Feather.LOG_OUT));
        logoutButton.setOnAction(event -> logout());
    }

    private void showUserProfileDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("User Profile");
        dialog.setHeaderText(null);

        dialog.getDialogPane().getStyleClass().add("custom-dialog-pane");

        User user = authenticationService.me();

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dialog-content");

        Label titleLabel = new Label("User Information");
        titleLabel.getStyleClass().add("dialog-title");

        Label nameLabel = createStyledLabel("Name: ", user.getFirstName());
        Label emailLabel = createStyledLabel("Email: ", user.getEmail());
        Label roleLabel = createStyledLabel("Role: ", user.getRole().name());
        Label organizationLabel = createStyledLabel("Organization: ", user.getOrganization().getName());

        content.getChildren().addAll(titleLabel, nameLabel, emailLabel, roleLabel, organizationLabel);

        dialog.getDialogPane().setContent(content);

        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), content);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        dialog.showAndWait();
    }

    private Label createStyledLabel(String prefix, String value) {
        Label label = new Label(prefix + value);
        label.getStyleClass().add("dialog-label");
        return label;
    }

    private void logout() {
        authenticationService.logout();
        UILoaderService.loadLogin();
    }
}