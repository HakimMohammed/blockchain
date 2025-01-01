package com.inventory.ui;

import com.inventory.ui.services.UILoaderService;
import javafx.application.Application;
import javafx.stage.Stage;

class Runner {

    public static void main(String[] args) {
        SecondApp app = new SecondApp();
        new Thread(app).start();
    }
}

public class SecondApp extends Application implements Runnable {
    @Override
    public void run(){
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        UILoaderService.setPrimaryStage(stage);
        UILoaderService.loadLogin();
    }
}