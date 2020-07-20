package com.railway.ui.window.entrance.pages.locomotives.filters;

import com.jfoenix.controls.JFXDatePicker;
import com.railway.database.Matcher;
import com.railway.database.tables.locomotives.LocomotiveMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.common.formatters.DateFormatter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class HadInspection extends FilterInterface implements Initializable {

    @FXML
    private JFXDatePicker dateTo;

    @FXML
    private JFXDatePicker dateFrom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateFrom.setValue(LocalDate.now());
        dateFrom.setConverter(new DateFormatter(dateFrom));

        dateTo.setValue(LocalDate.now());
        dateTo.setConverter(new DateFormatter(dateTo));
    }

    @Override
    public Collection<Matcher> getMatchers() {
        LocalDate from = dateFrom.getValue();
        LocalDate to = dateTo.getValue();
        if (from == null || to == null) {
            return new LinkedList<>();
        }
        return Arrays.asList(
                new LocomotiveMatchers.HadInspection()
                .from(from).to(to)
        );
    }
}
