package com.railway.ui.window.entrance.pages.timetable;

import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManager;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.entrance.pages.flightDetails.FlightDetailsFormController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class FlightInfoController extends WindowController implements Initializable {
    FlightInfoModel model;

    @FXML
    private Button choiceButton;

    @FXML
    private Label flightNumberLabel;

    @FXML
    private Label destinationStationLabel;

    @FXML
    private Label startDateLabel;

    @FXML
    private Label arrivalDateLabel;

    @FXML
    private Label sourceStationLabel;

    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final String fxmlFile = "/entrance/pages/flightDetails/flightDetails.fxml";

        choiceButton.setOnMouseClicked(e -> {
            WindowManagerInterface manager = getWindowManager();

            FlightDetailsFormController controller = manager.loadScene(fxmlFile, this);
            controller.setFlightNumber(model.getNumber());
            controller.updateView();
            manager.showScene(controller);
        });
    }

    public void setModel(FlightInfoModel model) {
        this.model = model;
    }

    public void updateView() {
        if (model == null)
            return;

        flightNumberLabel.setText(String.valueOf(model.getNumber()));
        sourceStationLabel.setText(model.getSourceStation());
        destinationStationLabel.setText(model.getDestinationStation());
        startDateLabel.setText(model.getStartDate());
        arrivalDateLabel.setText(model.getArrivalDate());
        statusLabel.setText(model.getFlightStatus());
    }
}
