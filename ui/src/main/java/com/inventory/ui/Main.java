package com.inventory.ui;

import atlantafx.base.theme.PrimerLight;
import com.inventory.ui.services.UILoaderService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        UILoaderService.setPrimaryStage(stage);
        UILoaderService.loadLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}