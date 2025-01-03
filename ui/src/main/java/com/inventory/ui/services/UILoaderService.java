package com.inventory.ui.services;

import atlantafx.base.theme.PrimerLight;
import com.inventory.ui.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;

public class UILoaderService {

    @Setter
    private static Stage primaryStage;


    public static void loadLogin() {
        loadView("views/login-view.fxml", "styles/auth.css");
    }

    public static void loadDashboard() {
        loadView("views/dashboard-view.fxml", "styles/main.css");
    }

    private static void loadView(String fxmlFilePath, String cssFilePath) {
        try {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFilePath));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1000, 600);
            if (cssFilePath != null && !cssFilePath.isEmpty()) {
                scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource(cssFilePath)).toExternalForm());
            }
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Error loading view: " + e.getMessage());
        }
    }
}
