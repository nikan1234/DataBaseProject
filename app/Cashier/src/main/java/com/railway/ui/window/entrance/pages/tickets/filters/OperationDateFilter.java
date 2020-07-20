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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class OperationDateFilter extends FilterInterface implements Initializable {
    public static final String PATH = "/entrance/pages/tickets/filters/operationDate.fxml";

    @FXML
    private JFXDatePicker calendar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        calendar.setValue(LocalDate.now());
        calendar.setConverter(new DateFormatter(calendar));
    }

    @Override
    public Collection<Matcher> getMatchers() {
        LocalDate date = calendar.getValue();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new TicketMatchers.MatchByPurchaseDay().bind(date));
        return matchers;
    }
}
