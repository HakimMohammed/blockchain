package com.inventory.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    public Label texting;

    @FXML
    public void sayHello() {
        texting.setText("Hello, JavaFX 11!");
    }
}
