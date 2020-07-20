package com.railway.ui.window.entrance.pages.passengers.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.tickets.TicketMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.*;

public class PassengerBaggageFilter extends FilterInterface implements Initializable {

    @FXML
    private ToggleGroup baggage;

    @FXML
    private RadioButton hasBaggage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        baggage.selectToggle(hasBaggage);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        return Arrays.asList(
                new TicketMatchers.MatchByPassengerBaggage().bind(hasBaggage.isSelected()));
    }
}
