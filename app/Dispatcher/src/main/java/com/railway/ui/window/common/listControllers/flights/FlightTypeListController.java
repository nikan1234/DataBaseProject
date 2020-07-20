package com.railway.ui.window.common.listControllers.flights;


import javafx.scene.control.ComboBox;

import java.util.List;

public class FlightTypeListController {

    public FlightTypeListController(ComboBox<String> typesBox) {
        List<String> types = new FlightTypeListModel().getFlightTypes();

        typesBox.getItems().setAll(types);
        if (types.size() > 0) {
            typesBox.setValue(types.get(0));
        }
    }
}
