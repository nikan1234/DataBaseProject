package com.railway.ui.window.entrance.pages.flightDetails.actions.common;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.railway.database.DatabaseController;
import com.railway.database.tables.ErrorCodes;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.formatters.DateFormatter;
import com.railway.ui.window.common.formatters.TimeFormatter;
import com.railway.ui.window.entrance.pages.flightDetails.actions.ActionInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ExistingTicketFormController extends WindowController implements Initializable, ActionInterface {
    private String sql;
    private int flightNumber;


    private static class Positions {
        static final int TICKET_ID_POS = 1;
        static final int PURCHASE_TIME_POS  = 2;
    }

    @FXML
    private TextField ticketIdField;

    @FXML
    private JFXTimePicker clock;

    @FXML
    private JFXDatePicker calendar;

    @FXML
    private Label resultLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTicketField();
        setupTime();
    }

    private void setupTicketField() {
        final char sep = '-';

        ticketIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                final String pattern = Integer.toString(flightNumber) + sep;
                int idx = newValue.indexOf(sep);
                if (idx < 0) {
                    newValue = oldValue;
                } else {
                    newValue = newValue.substring(idx + 1);
                    if (!newValue.matches("\\d*")) {
                        newValue = newValue.replaceAll("[^\\d]", "");
                    }
                    newValue = pattern + newValue;
                }
                ticketIdField.setText(newValue);
            }
            catch (final Exception e) {
                ticketIdField.setText(oldValue);
            }
        });
    }

    private void setupTime() {
        clock.setIs24HourView(true);
        clock.setValue(LocalTime.now());
        clock.setConverter(new TimeFormatter(clock));

        calendar.setValue(LocalDate.now());
        calendar.setConverter(new DateFormatter(calendar));
    }

    @FXML
    void onAcceptButtonClicked(MouseEvent event) {
        resultLabel.setText("");
        try {
            String ID = ticketIdField.getText();
            if (!checkId(ID)) {
                resultLabel.setText("WRONG ID!");
                return;
            }

            String day = calendar.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = clock.getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String timestamp = day + " " + time;

            CallableStatement statement = DatabaseController.getInstance().getConnection().prepareCall(sql);
            statement.setString(Positions.TICKET_ID_POS, ID);
            statement.setString(Positions.PURCHASE_TIME_POS, timestamp);
            statement.executeUpdate();
            resultLabel.setText("SUCCESS!\nTICKET " + ID);
        }
        catch (SQLException e) {
            String builder = "YOU CAN'T OPERATE WITH TICKET FOR THIS FLIGHT!" +
                    System.lineSeparator() +
                    ErrorCodes.toString(e.getErrorCode());
            resultLabel.setText(builder);
        }
    }

    @FXML
    void onPrevButtonClicked(MouseEvent event) {
        getWindowManager().prevScene();
    }

    @Override
    public void setFlightNumber(int number) {
        final char sep = '-';

        this.flightNumber = number;
        this.ticketIdField.setText(Integer.toString(flightNumber) + sep);
        this.ticketIdField.positionCaret(ticketIdField.getText().length());
    }

    public void setSqlCommand(String sql) {
        this.sql = sql;
    }

    private boolean checkId(String id) {
        final char sep = '-';

        int idx = id.indexOf(sep);
        if (idx == -1)
            return false;
        return id.length() - idx > 1;
    }
}
