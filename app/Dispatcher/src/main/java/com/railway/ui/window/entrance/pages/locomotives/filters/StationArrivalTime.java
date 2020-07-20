package com.railway.ui.window.entrance.pages.locomotives.filters;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.railway.database.Matcher;
import com.railway.database.tables.locomotives.LocomotiveMatchers;
import com.railway.ui.window.common.entity.Station;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.common.formatters.DateFormatter;
import com.railway.ui.window.common.formatters.TimeFormatter;
import com.railway.ui.window.common.listControllers.stations.StationListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class StationArrivalTime extends FilterInterface implements Initializable {

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXTimePicker timePicker;

    @FXML
    private ComboBox<Station> stationBox;

    private StationListController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new StationListController(stationBox);

        LocalTime now = LocalTime.now();
        timePicker.setIs24HourView(true);
        timePicker.setValue(LocalTime.of(now.getHour(), now.getMinute()));
        timePicker.setConverter(new TimeFormatter(timePicker));

        datePicker.setValue(LocalDate.now());
        datePicker.setConverter(new DateFormatter(datePicker));
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();

        Station station = controller.getSelectedStation();
        if (station == null) {
            return matchers;
        }
        String date = datePicker.getValue().format(DateFormatter.FORMATTER);
        String time = timePicker.getValue().format(TimeFormatter.FORMATTER);
        String timestamp = date + ' ' + time;

        matchers.add(new LocomotiveMatchers
                .MatchByStationArrivalDate()
                .setTime(timestamp)
                .setStation(station));
        return matchers;
    }
}
