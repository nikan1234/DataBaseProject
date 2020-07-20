package com.railway.ui.window.entrance.pages.locomotives.filters;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.railway.database.Matcher;
import com.railway.database.tables.locomotives.LocomotiveMatchers;
import com.railway.ui.window.common.fieldContollers.IntegerFieldController;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.common.formatters.DateFormatter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class LocomotiveFlightsCount extends FilterInterface implements Initializable {

    @FXML
    private TextField flightsCount;

    @FXML
    private JFXCheckBox beforeRepair;

    @FXML
    private Label repairDateLabel;

    @FXML
    private JFXDatePicker repairDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repairDate.setValue(LocalDate.now());
        repairDate.setConverter(new DateFormatter(repairDate));

        new IntegerFieldController(flightsCount);

        beforeRepair.setSelected(false);
        beforeRepair.selectedProperty().addListener((observable, oldValue, newValue) -> {
            repairDateLabel.setVisible(newValue);
            repairDate.setVisible(newValue);
        });

        repairDateLabel.setVisible(false);
        repairDate.setVisible(false);
    }

        @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();

        String count = flightsCount.getText();
        if (!count.isEmpty()) {
            if (beforeRepair.isSelected()) {
                LocalDate date = repairDate.getValue();
                if (date != null) {
                    matchers.add(new LocomotiveMatchers
                            .MatchByFlightsBeforeRepair()
                            .setCount(Integer.parseInt(count))
                            .setDate(date));
                }
            }
            else {
                matchers.add(new LocomotiveMatchers
                        .NumberOfFlights()
                        .bind(count));
            }
        }
        return matchers;
    }
}
