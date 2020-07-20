package com.railway.ui.window.entrance.pages.timetable.actions.remove;

import com.jfoenix.controls.JFXListView;
import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Flight;
import com.railway.ui.window.common.fieldContollers.IntegerFieldController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RemoveFlightFormController extends WindowController implements Initializable {
    private RemoveFlightFormModel model = new RemoveFlightFormModel();

    @FXML
    private TextField flightNumber;

    @FXML
    private Label resultLabel;

    @FXML
    private JFXListView<Flight> flightsTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new IntegerFieldController(flightNumber);

        flightNumber.setOnKeyTyped(e -> updateFlightsList());

        updateFlightsList();
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }

    @FXML
    void removeFlightButton() {
        String flight = flightNumber.getText();
        if (flight.isEmpty()) {
            return;
        }
        int result = model.removeFlight(Integer.parseInt(flight));
        resultLabel.setText("RESULT: " + Errors.toString(result));

        updateFlightsList();
    }

    private void updateFlightsList() {
        String idBeginning = flightNumber.getText();
        flightsTable.getItems().setAll(model.getFlights(idBeginning));
    }
}
