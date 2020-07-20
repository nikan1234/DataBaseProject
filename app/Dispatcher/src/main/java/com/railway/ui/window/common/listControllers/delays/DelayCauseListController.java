package com.railway.ui.window.common.listControllers.delays;

import javafx.scene.control.ComboBox;

import java.util.List;

public class DelayCauseListController {

    public DelayCauseListController(ComboBox<String> comboBox) {
        List<String> causes = new DelayCauseListModel().getDelayCauses();
        comboBox.getItems().addAll(causes);

        if (causes.size() > 0) {
            comboBox.setValue(causes.get(0));
        }
    }
}
