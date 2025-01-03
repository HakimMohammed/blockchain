package com.inventory.ui.controllers;

import com.inventory.ui.enums.DemandStatus;
import com.inventory.ui.enums.OrganizationType;
import com.inventory.ui.models.CompanyDemand;
import com.inventory.ui.models.User;
import com.inventory.ui.services.AuthenticationService;
import com.inventory.ui.services.CompanyDemandService;
import com.inventory.ui.services.UILoaderService;
import com.inventory.ui.sockets.WebSocketNotificationClient;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavbarController {

    @FXML
    private Button notificationsButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button logoutButton;

    private Label notificationBadge;
    private int notificationCount = 0;
    private final List<String> notifications = new ArrayList<>();

    private final AuthenticationService authenticationService = AuthenticationService.getInstance();
    private final CompanyDemandService companyDemandService = CompanyDemandService.getInstance();

    @FXML
    private void initialize() {
        User user = authenticationService.me();
        if (OrganizationType.COMPANY.equals(user.getOrganization().getType())) {
            notificationsButton.setVisible(false);
        } else {
            notificationsButton.setGraphic(new FontIcon(Feather.BELL));
            setupNotificationButton();
            loadInitialNotifications();
        }
        profileButton.setGraphic(new FontIcon(Feather.USER));
        profileButton.setOnAction(event -> showUserProfileDialog());
        logoutButton.setGraphic(new FontIcon(Feather.LOG_OUT));
        logoutButton.setOnAction(event -> logout());
        try {
            WebSocketNotificationClient webSocketClient = new WebSocketNotificationClient(new URI("ws://localhost:8080/notifications"));
            webSocketClient.setOnMessageReceived(this::incrementNotificationCount);
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void loadInitialNotifications() {
        List<CompanyDemand> companyDemands = companyDemandService.findAll();
        for (CompanyDemand demand : companyDemands) {
            if (demand.getStatus() != DemandStatus.ACCEPTED) {
                String notification = "New demand from " + demand.getCompanyName() + " with quantity " + demand.getQuantity() + " for product " + demand.getProductName()
                        + " with id " + demand.getId();
                notifications.add(notification);
                notificationCount++;
            }
        }
        updateNotificationBadge();
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

    private void setupNotificationButton() {
        notificationBadge = new Label();
        notificationBadge.getStyleClass().add("notification-badge");
        notificationBadge.setVisible(false);

        FontIcon notificationIcon = new FontIcon(Feather.BELL);
        StackPane stackPane = new StackPane(notificationIcon, notificationBadge);
        notificationsButton.setGraphic(stackPane);
        notificationsButton.setOnMouseClicked(event -> {
            notificationCount = 0;
            updateNotificationBadge();
            showNotificationsDialog();
        });
    }

    private void incrementNotificationCount(String message) {
        Platform.runLater(() -> {
            notifications.add(message);
            notificationCount++;
            updateNotificationBadge();
        });
    }

    private void updateNotificationBadge() {
        if (notificationCount > 0) {
            notificationBadge.setText(String.valueOf(notificationCount));
            notificationBadge.setVisible(true);
        } else {
            notificationBadge.setVisible(false);
        }
    }

    private void showNotificationsDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Notifications");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        for (String notification : notifications) {
            HBox notificationItem = new HBox(10);
            Label notificationLabel = new Label(notification);
            String regex = "with id ([\\w-]+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(notification);

            if (matcher.find()) {
                String demandId = matcher.group(1);
                notificationItem.setId(demandId);
            }

            Button doneButton = getButton(notification, content, notificationItem);
            notificationItem.getChildren().addAll(notificationLabel, doneButton);
            content.getChildren().add(notificationItem);
        }

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private @NotNull Button getButton(String notification, VBox content, HBox notificationItem) {
        Button doneButton = new Button();
        FontIcon doneIcon = new FontIcon(Feather.CHECK);
        doneButton.setStyle("-fx-background-color: #2B55D5");
        doneButton.setGraphic(doneIcon);
        doneButton.setOnAction(event -> {
            notifications.remove(notification);
            content.getChildren().remove(notificationItem);
            companyDemandService.update(UUID.fromString(notificationItem.getId()));
            updateNotificationBadge();
        });
        return doneButton;
    }
}