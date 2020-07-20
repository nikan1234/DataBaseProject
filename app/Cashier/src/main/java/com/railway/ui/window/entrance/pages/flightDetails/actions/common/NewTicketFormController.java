package com.railway.ui.window.entrance.pages.flightDetails.actions.common;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.railway.database.DatabaseController;
import com.railway.database.tables.ErrorCodes;
import com.railway.database.utils.DatabaseUtils;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.formatters.DateFormatter;
import com.railway.ui.window.common.formatters.TimeFormatter;
import com.railway.ui.window.entrance.pages.flightDetails.actions.ActionInterface;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class NewTicketFormController extends WindowController implements Initializable, ActionInterface {
    private String sql;

    protected static class Positions {
        private static final int LAST_NAME_POS      = 1;
        private static final int FIRST_NAME_POS     = 2;
        private static final int SECOND_NAME_POS    = 3;
        private static final int BAGGAGE_POS        = 4;
        private static final int AGE_POS            = 5;
        private static final int GENDER_POS         = 6;
        private static final int FLIGHT_NUMBER_POS  = 7;
        private static final int DATE_POS           = 8;
        private static final int RESULT_POS         = 9;
    }


    private int flightNumber;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField secondNameField;

    @FXML
    private TextField ageField;

    @FXML
    private ComboBox<Character> genderBox;

    @FXML
    private TextField flightNumberField;

    @FXML
    private RadioButton hasBaggage;

    @FXML
    private JFXTimePicker clock;

    @FXML
    private JFXDatePicker calendar;

    @FXML
    private Label resultLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupAgeField();
        setupGenderField();
        setupTime();
    }

    public void setSqlCommand(String sql) {
        this.sql = sql;
    }


    private void setupGenderField() {
        List<Character> list = Arrays.asList('M', 'F');
        genderBox.setItems(FXCollections.observableList(list));
        genderBox.setValue(list.get(0));
    }

    private void setupAgeField() {
        ageField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                ageField.setText(newValue.replaceAll("[^\\d]", ""));
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

    @Override
    public void setFlightNumber(int number) {
        flightNumber = number;
        flightNumberField.setText(Integer.toString(number));
    }

    @FXML
    void onAcceptButtonClicked(MouseEvent event) {
        resultLabel.setText("");
        try {
            String last_name = lastNameField.getText();
            String first_name = firstNameField.getText();
            String second_name = secondNameField.getText();
            if (last_name.isEmpty() || first_name.isEmpty()) {
                resultLabel.setText("SOME FIELDS ARE EMPTY!");
                return;
            }

            String stringAge = ageField.getText();
            if (stringAge.isEmpty()) {
                resultLabel.setText("SOME FIELDS ARE EMPTY!");
                return;
            }

            int age = Integer.parseInt(stringAge);
            Character gender = genderBox.getValue();

            String day = calendar.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = clock.getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String timestamp = day + " " + time;

            Character baggage = DatabaseUtils.toChar(hasBaggage.isSelected());

            CallableStatement cStmt = DatabaseController
                    .getInstance()
                    .getConnection()
                    .prepareCall(sql);
            cStmt.setString(Positions.LAST_NAME_POS, last_name);
            cStmt.setString(Positions.FIRST_NAME_POS, first_name);
            cStmt.setString(Positions.SECOND_NAME_POS, second_name);
            cStmt.setString(Positions.BAGGAGE_POS, Character.toString(baggage));
            cStmt.setString(Positions.GENDER_POS, Character.toString(gender));
            cStmt.setInt(Positions.AGE_POS, age);
            cStmt.setInt(Positions.FLIGHT_NUMBER_POS, flightNumber);
            cStmt.setString(Positions.DATE_POS, timestamp);
            cStmt.registerOutParameter(Positions.RESULT_POS, Types.VARCHAR);

            cStmt.executeUpdate();

            String ID = cStmt.getString(Positions.RESULT_POS);
            resultLabel.setText("SUCCESS!\nTICKET ID: " + ID);
        }
        catch (SQLException e) {
            String builder = "YOU CAN'T BY TICKET FOR THIS FLIGHT!" +
                    System.lineSeparator() +
                    ErrorCodes.toString(e.getErrorCode());
            resultLabel.setText(builder);
        }
    }

    @FXML
    void onPrevButtonClicked(MouseEvent event) {
        getWindowManager().prevScene();
    }
}
