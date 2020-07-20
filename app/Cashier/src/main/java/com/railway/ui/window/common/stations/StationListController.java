package com.railway.ui.window.common.stations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.LinkedList;


public class StationListController {
    StationListModel model = new StationListModel();
    ComboBox<String> stationList;

    public StationListController(ComboBox<String> stationList) {
        this.stationList = stationList;

        LinkedList<String> stationNames = new LinkedList<>();
        for (var station : model.getStations()) {
            stationNames.add(station.getValue());
        }
        final String title = "Any station";
        ObservableList<String> items = FXCollections.observableArrayList(title);
        items.addAll(stationNames);
        stationList.setItems(items);
        stationList.setValue(title);
    }

    public int getSelectedIndex() {
        return stationList.getSelectionModel().getSelectedIndex();
    }

    public int getStationId() {
        int idx = getSelectedIndex();
        if (idx == 0)
            return -1;
        return model.getStations().get(idx - 1).getKey();
    }
}
