package com.railway.ui.window.entrance.pages.tickets.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.routes.RouteMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.common.stations.StationListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class RouteFilter extends FilterInterface implements Initializable {

    private StationListController from;
    private StationListController to;

    @FXML
    private ComboBox<String> stationFrom;

    @FXML
    private ComboBox<String> stationTo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        from = new StationListController(stationFrom);
        to = new StationListController(stationTo);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        if (from.getSelectedIndex() > 0) {
            matchers.add(new RouteMatchers.MatchByStartStation().bind(from.getStationId()));
        }
        if (to.getSelectedIndex() > 0) {
            matchers.add(new RouteMatchers.MatchByFinishStation().bind(to.getStationId()));
        }
        return matchers;
    }
}
