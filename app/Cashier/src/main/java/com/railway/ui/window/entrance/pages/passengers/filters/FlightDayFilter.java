package com.railway.ui.window.entrance.pages.passengers.filters;

import com.jfoenix.controls.JFXDatePicker;
import com.railway.database.Matcher;
import com.railway.database.tables.flights.FlightMatchers;
import com.railway.database.tables.routes.RouteMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.common.formatters.DateFormatter;
import com.railway.ui.window.common.routes.RouteTypeListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

public class FlightDayFilter extends FilterInterface implements Initializable {

    @FXML
    private JFXDatePicker calendar;

    @FXML
    private ComboBox<String> routeTypeBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new RouteTypeListController(routeTypeBox);

        calendar.setValue(LocalDate.now());
        calendar.setConverter(new DateFormatter(calendar));
    }

    @Override
    public Collection<Matcher> getMatchers() {
        LocalDate date = calendar.getValue();

        return Arrays.asList(
                new FlightMatchers.MatchByFlightDay().bind(date),
                new RouteMatchers.MatchByRouteType().bind(routeTypeBox.getValue()));
    }
}
