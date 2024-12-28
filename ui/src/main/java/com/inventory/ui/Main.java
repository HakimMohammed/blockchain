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
//        try {
//            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
//
//            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/login-view.fxml"));
//            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
//            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/auth.css")).toExternalForm());
//
//            stage.initStyle(StageStyle.UNDECORATED);
//            stage.setScene(scene);
//            stage.show();
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
        UILoaderService.setPrimaryStage(stage);
        UILoaderService.loadLogin();
    }


    public static void main(String[] args) {
        launch(args);
    }
}