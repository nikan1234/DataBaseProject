package com.railway.ui.window.entrance.pages.flightDetails;

import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Locomotive;
import com.railway.ui.window.common.formatters.DateFormatter;
import com.railway.ui.window.entrance.pages.timetable.TimetableController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class FlightDetailsFormController extends WindowController implements Initializable {
    private FlightDetailsFormModel model;
    private ObservableList<String> routeList;

    @FXML
    private Label flightNumberLabel;

    @FXML
    private ListView<String> routeTable;

    @FXML
    private Label flightStatus;

    @FXML
    private Label delayCauseLabel;

    @FXML
    private Label locomotiveId;

    @FXML
    private Label locomotiveName;

    @FXML
    private Label locomotiveEntryDate;

    @FXML
    private Label durationField;

    @FXML
    private Label ticketCost;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        routeList = FXCollections.observableArrayList();
        routeTable.setItems(routeList);
    }


    public void setFlightNumber(int number) {
        model = new FlightDetailsFormModel(number);
        updateView();
    }

    public void updateView() {
        flightNumberLabel.setText(String.valueOf(model.getFlightNumber()));

        routeList.clear();
        for (var pair : model.getRoute()) {
            routeList.add(pair.getValue() + "\t" + pair.getKey());
        }

        flightStatus.setText(model.getStatus());

        if (!model.good()) {
            delayCauseLabel.setText(model.getProblemDescribe());
        }

        Locomotive locomotive = model.getLocomotiveInfo();
        if (locomotive == null) {
            return;
        }
        locomotiveId.setText(Integer.toString(locomotive.getLocomotiveId()));
        locomotiveName.setText(locomotive.getLocomotiveName());
        locomotiveEntryDate.setText(locomotive.getEntryDate().format(DateFormatter.FORMATTER));

        durationField.setText(model.getDuration() + " hours");
        ticketCost.setText(Double.toString(model.getTicketCost()));
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }
}
