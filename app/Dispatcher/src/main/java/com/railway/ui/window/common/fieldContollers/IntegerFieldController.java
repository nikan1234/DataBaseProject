package com.railway.ui.window.common.fieldContollers;


import javafx.scene.control.TextField;

public class IntegerFieldController {

    public IntegerFieldController(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                field.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
