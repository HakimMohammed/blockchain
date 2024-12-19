package com.inventory.ui;

import com.inventory.ui.services.UILoaderService;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        UILoaderService uiLoaderService = new UILoaderService();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/app-icon-32.png"))));
        uiLoaderService.loadLoginPage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}