package com.inventory.ui.animations;


import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animations {
    public static void shake(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), node);
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.playFromStart();
    }

    public static void fadeIn(Node node) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public static void fadeIn(Node node, double duration) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(duration), node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}