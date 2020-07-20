package com.railway.ui.window.entrance.pages.delays.actions.add;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.fieldContollers.IntegerFieldController;
import com.railway.ui.window.common.formatters.DateFormatter;
import com.railway.ui.window.common.formatters.TimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddDelayController extends WindowController implements Initializable {
    private AddDelayModel model = new AddDelayModel();

    @FXML
    private TextField flightField;

    @FXML
    private TextField durationField;

    @FXML
    private TextField causeField;

    @FXML
    private Button addButton;

    @FXML
    private Label resultLabel;

    @FXML
    private JFXDatePicker operationDate;

    @FXML
    private JFXTimePicker operationTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        operationDate.setConverter(new DateFormatter(operationDate));
        operationDate.setValue(LocalDate.now());

        operationTime.setIs24HourView(true);
        operationTime.setConverter(new TimeFormatter(operationTime));
        operationTime.setValue(LocalTime.now());

        new IntegerFieldController(flightField);
        new IntegerFieldController(durationField);

        addButton.setOnMouseClicked(e -> {
            String idText = flightField.getText();
            if (idText.isEmpty()) {
                resultLabel.setText("Specify flight number");
                return;
            }
            int id = Integer.parseInt(idText);

            String durationText = durationField.getText();
            if (durationText.isEmpty()) {
                resultLabel.setText("Specify delay duration");
                return;
            }
            int duration = Integer.parseInt(durationText);

            String cause = causeField.getText();
            if (cause.isEmpty()) {
                resultLabel.setText("Specify delay cause");
                return;
            }

            int result = model.addDelay(id, duration, cause, LocalDateTime.of(
                    operationDate.getValue(), operationTime.getValue()));
            resultLabel.setText("Result: " + Errors.toString(result));
        });
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }
}
