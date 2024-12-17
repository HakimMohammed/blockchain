package com.inventory.ui.utils;

import atlantafx.base.controls.Card;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class StyleUtils {
    public static void applyCardStyle(Card card) {
        card.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.2)));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
    }
}
