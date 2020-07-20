package com.railway.ui.window.entrance.pages.delays.actions.remove;

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

public class RemoveDelayController extends WindowController implements Initializable {
    private RemoveDelayModel model = new RemoveDelayModel();

    @FXML
    private TextField flightField;

    @FXML
    private Button removeButton;

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

        removeButton.setOnMouseClicked(e -> {
            String text = flightField.getText();
            if (text.isEmpty()) {
                resultLabel.setText("Specify flight number");
                return;
            }
            int result = model.removeDelay(Integer.parseInt(text), LocalDateTime.of(
                    operationDate.getValue(), operationTime.getValue()));
            resultLabel.setText("RESULT: " + Errors.toString(result));
        });
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }
}
