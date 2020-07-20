package com.railway.ui.window.entrance.pages.tickets.filters;

import com.jfoenix.controls.JFXDatePicker;
import com.railway.database.Matcher;
import com.railway.database.tables.tickets.TicketMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.common.formatters.DateFormatter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class DateIntervalFilter extends FilterInterface implements Initializable {

    @FXML
    private JFXDatePicker fromDate;

    @FXML
    private JFXDatePicker toDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fromDate.setValue(LocalDate.now());
        fromDate.setConverter(new DateFormatter(fromDate));

        toDate.setValue(LocalDate.now());
        toDate.setConverter(new DateFormatter(toDate));
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new TicketMatchers.MatchByPurchaseInterval()
                .bind(new TicketMatchers.DateInterval(
                        fromDate.getValue(), toDate.getValue())));
        return matchers;
    }
}
