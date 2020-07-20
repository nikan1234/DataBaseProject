package com.railway.ui.window.common.listControllers.stations;

import com.railway.ui.window.common.entity.Station;
import javafx.scene.control.ComboBox;

import java.util.List;

public class StationListController {
    private ComboBox<Station> stationComboBox;
    private List<Station> stations;

    public StationListController(ComboBox<Station> stationComboBox) {
        this.stationComboBox = stationComboBox;
        this.stations = new StationListModel().getStationList();

        stationComboBox.getItems().setAll(stations);
        if (stations.size() > 0) {
            stationComboBox.setValue(stations.get(0));
        }
    }

    public Station getSelectedStation() {
        int idx = stationComboBox.getSelectionModel().getSelectedIndex();
        if (idx >= 0) {
            return stations.get(idx);
        }
        return null;
    }

    static public List<Station> getStationList() {
        return new StationListModel().getStationList();
    }
}
