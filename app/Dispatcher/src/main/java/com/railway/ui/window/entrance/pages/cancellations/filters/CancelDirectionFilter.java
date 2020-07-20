package com.railway.ui.window.entrance.pages.cancellations.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.flights.FlightMatchers;
import com.railway.ui.window.common.entity.Station;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.common.listControllers.stations.StationListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class CancelDirectionFilter extends FilterInterface implements Initializable {
    private StationListController controller;

    @FXML
    private ComboBox<Station> stationBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new StationListController(stationBox);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        Station station = controller.getSelectedStation();
        if (station != null) {
            matchers.add(new FlightMatchers
                    .MatchByFlightDirection()
                    .bind(station.getId()));
        }
        return matchers;
    }
}
