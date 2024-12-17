package com.inventory.ui.utils;

import com.inventory.ui.animations.Animations;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class AlertUtils {
    public static void showError(Label label, String message) {
        showAlert(label, message, Color.RED);
    }

    public static void showSuccess(Label label, String message) {
        showAlert(label, message, Color.GREEN);
    }

    public static void showInfo(Label label, String message) {
        showAlert(label, message, Color.BLUE);
    }

    private static void showAlert(Label label, String message, Color color) {
        label.setText(message);
        label.setTextFill(color);
        label.setVisible(true);
        label.setManaged(true);
        Animations.fadeIn(label, 200);
    }
}