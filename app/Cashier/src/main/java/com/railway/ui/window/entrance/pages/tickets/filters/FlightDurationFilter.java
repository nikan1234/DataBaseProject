package com.railway.ui.window.entrance.pages.tickets.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.flights.FlightMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class FlightDurationFilter extends FilterInterface implements Initializable {
    @FXML
    private TextField fromHours;

    @FXML
    private TextField toHours;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fromHours.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                fromHours.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        toHours.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                toHours.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @Override
    public Collection<Matcher> getMatchers() {
        LinkedList<Matcher> matchers = new LinkedList<>();

        String from = fromHours.getText();
        if (!from.isEmpty()) {
            matchers.add(new FlightMatchers.MatchByFlightDuration()
                    .bind(Integer.parseInt(from))
                    .comparator(Matcher.NOT_LESS));
        }
        String to = toHours.getText();
        if (!to.isEmpty()) {
            matchers.add(new FlightMatchers.MatchByFlightDuration()
                    .bind(Integer.parseInt(to))
                    .comparator(Matcher.NOT_GREATER));
        }
        return matchers;
    }
}
