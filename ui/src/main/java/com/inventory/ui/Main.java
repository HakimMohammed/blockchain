package com.inventory.ui;

import com.inventory.ui.services.LoaderService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/app-icon-32.png"))));
        LoaderService.loadLoginPage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}