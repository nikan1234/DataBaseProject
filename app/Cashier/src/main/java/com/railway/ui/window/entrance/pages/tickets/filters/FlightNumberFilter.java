package com.railway.ui.window.entrance.pages.tickets.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.flights.FlightMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class FlightNumberFilter extends FilterInterface implements Initializable {
    public static String PATH = "/entrance/pages/tickets/filters/flightNumber.fxml";

    @FXML
    private TextField flightNumberField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        flightNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                flightNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @Override
    public Collection<Matcher> getMatchers() {
        LinkedList<Matcher> matchers = new LinkedList<>();

        String text = flightNumberField.getText();
        if (!text.isEmpty()) {
            int number = Integer.parseInt(text);
            matchers.add(new FlightMatchers.MatchByFlightNumber().bind(number));
        }
        return matchers;
    }
}
