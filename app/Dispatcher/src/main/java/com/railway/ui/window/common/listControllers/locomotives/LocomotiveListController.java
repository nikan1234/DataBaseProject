package com.railway.ui.window.common.listControllers.locomotives;

import com.railway.ui.window.common.entity.Locomotive;
import javafx.scene.control.ComboBox;

import java.util.List;

public class LocomotiveListController {
    private List<Locomotive> locomotives;
    private ComboBox<Locomotive> locomotiveComboBox;

    public LocomotiveListController(ComboBox<Locomotive> box) {
        this.locomotiveComboBox = box;

        locomotives = new LocomotiveListModel().getLocomotives();
        locomotiveComboBox.getItems().setAll(locomotives);
        if (locomotives.size() > 0) {
            locomotiveComboBox.setValue(locomotives.get(0));
        }
    }

    public Locomotive getSelectedLocomotive() {
        int idx = locomotiveComboBox.getSelectionModel().getSelectedIndex();
        if (idx < 0)
            return null;
        if (idx >= locomotives.size()) {
            return null;
        }
        return locomotives.get(idx);
    }
}
