package com.railway.ui.window.entrance.pages.timetable.actions.add;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Flight;
import com.railway.ui.window.common.entity.Locomotive;
import com.railway.ui.window.common.entity.Route;
import com.railway.ui.window.common.fieldContollers.DoubleFieldController;
import com.railway.ui.window.common.fieldContollers.IntegerFieldController;
import com.railway.ui.window.common.formatters.DateFormatter;
import com.railway.ui.window.common.formatters.TimeFormatter;
import com.railway.ui.window.common.listControllers.flights.FlightTypeListController;
import com.railway.ui.window.common.listControllers.locomotives.LocomotiveListController;
import com.railway.ui.window.common.listControllers.routes.RouteListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddFlightFormController extends WindowController implements Initializable {
    private AddFlightFormModel model = new AddFlightFormModel();


    @FXML
    private TextField flightNumber;

    @FXML
    private JFXDatePicker flightDate;

    @FXML
    private JFXTimePicker flightTime;

    @FXML
    private ComboBox<String> flightType;

    @FXML
    private ComboBox<Locomotive> locomotive;

    @FXML
    private ComboBox<Route> route;

    @FXML
    private TextField ticketCost;

    @FXML
    private Label arrivalDate;

    @FXML
    private Label resultLabel;

    @FXML
    private JFXCheckBox addInspection;

    @FXML
    private JFXCheckBox addRepair;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new DoubleFieldController(ticketCost);

        addInspection.setSelected(true);
        addRepair.setSelected(false);

        new IntegerFieldController(flightNumber);
        new RouteListController(route);
        new LocomotiveListController(locomotive);

        new FlightTypeListController(flightType);

        LocalTime now = LocalTime.now();
        flightTime.setIs24HourView(true);
        flightTime.setValue(LocalTime.of(now.getHour(), now.getMinute()));
        flightTime.setConverter(new TimeFormatter(flightTime));

        flightDate.setValue(LocalDate.now());
        flightDate.setConverter(new DateFormatter(flightDate));

        route.getSelectionModel().selectedItemProperty()
                .addListener((options, oldValue, newValue) -> showArrivalDate());

        flightDate.valueProperty().addListener((options, oldValue, newValue) -> showArrivalDate());
        flightTime.valueProperty().addListener((options, oldValue, newValue) -> showArrivalDate());

        showArrivalDate();
    }

    @FXML
    void addFlightButton() {
        resultLabel.setText("");

        String number = flightNumber.getText();
        if (number.isEmpty()) {
            resultLabel.setText("Specify flight number");
            return;
        }
        String cost = ticketCost.getText();
        if (cost.isEmpty()) {
            resultLabel.setText("Wrong cost value");
            return;
        }
        LocalDateTime dateTime = LocalDateTime.of(
                flightDate.getValue(), flightTime.getValue());

        Flight flight = new Flight(
                Integer.parseInt(number),
                dateTime, flightType.getValue(),
                Double.parseDouble(cost));

        addFlight(flight, locomotive.getValue(), route.getValue(), dateTime);
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }

    private void showArrivalDate() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY hh:mm:ss");

        LocalDateTime dateTime = LocalDateTime.of(flightDate.getValue(), flightTime.getValue());
        dateTime = dateTime.plusHours(model.getRouteDuration(route.getValue()));
        arrivalDate.setText(dateTime.format(formatter));
    }

    private void addFlight(Flight flight, Locomotive locomotive, Route route, LocalDateTime date) {
        final int INSPECTION_DURATION = 1;

        int result = model.addFlight(flight, locomotive, route, date);
        if (result != Errors.QUERY_SUCCESS) {
            resultLabel.setText("FAILED TO ADD FLIGHT.\n" +
                    "ERROR: " + Errors.toString(result));
            return;
        }

        LocalDateTime inspectionTime = date.minusHours(INSPECTION_DURATION);
        if (addInspection.isSelected()) {
            result = model.addInspection(flight, inspectionTime);
            if (result != Errors.QUERY_SUCCESS) {
                resultLabel.setText("FAILED TO ADD INSPECTION.\n" +
                        "ERROR: " + Errors.toString(result));
            }
        }
        if (addRepair.isSelected()) {
            result = model.addRepair(locomotive, inspectionTime);
            if (result != Errors.QUERY_SUCCESS) {
                resultLabel.setText("FAILED TO ADD REPAIR.\n" +
                        "ERROR: " + Errors.toString(result));
            }
        }
        resultLabel.setText("SUCCESS!");
    }
}
