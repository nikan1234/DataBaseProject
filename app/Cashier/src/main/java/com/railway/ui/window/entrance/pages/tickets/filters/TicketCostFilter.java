package com.railway.ui.window.entrance.pages.tickets.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.flights.FlightMatchers;
import com.railway.ui.window.common.fieldContollers.DoubleFieldController;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class TicketCostFilter extends FilterInterface implements Initializable {
    @FXML
    private TextField fromCost;

    @FXML
    private TextField toCost;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new DoubleFieldController(fromCost);
        new DoubleFieldController(toCost);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        LinkedList<Matcher> matchers = new LinkedList<>();

        String from = fromCost.getText();
        if (!from.isEmpty()) {
            matchers.add(new FlightMatchers.MatchByTicketCost()
                    .bind(Double.parseDouble(from))
                    .comparator(Matcher.NOT_LESS));
        }
        String to = toCost.getText();
        if (!to.isEmpty()) {
            matchers.add(new FlightMatchers.MatchByTicketCost()
                    .bind(Double.parseDouble(to))
                    .comparator(Matcher.NOT_GREATER));
        }
        return matchers;
    }
}
