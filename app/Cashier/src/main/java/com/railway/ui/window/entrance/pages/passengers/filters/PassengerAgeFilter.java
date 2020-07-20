package com.railway.ui.window.entrance.pages.passengers.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.tickets.TicketMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class PassengerAgeFilter extends FilterInterface implements Initializable {

    @FXML
    private TextField passengerAgeField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passengerAgeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                passengerAgeField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @Override
    public Collection<Matcher> getMatchers() {
        LinkedList<Matcher> matchers = new LinkedList<>();

        String text = passengerAgeField.getText();
        if (!text.isEmpty()) {
            int age = Integer.parseInt(text);
            matchers.add(
                    new TicketMatchers.MatchByPassengerAge().bind(age));
        }
        return matchers;
    }
}
