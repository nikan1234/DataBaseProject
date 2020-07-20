package com.railway.ui.window.entrance.pages.timetable;

import com.jfoenix.controls.JFXCheckBox;
import com.railway.database.Matcher;
import com.railway.database.tables.flights.FlightDomains;
import com.railway.database.tables.flights.FlightMatchers;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.common.fieldContollers.DoubleFieldController;
import com.railway.ui.window.common.fieldContollers.IntegerFieldController;
import com.railway.ui.window.common.stations.StationListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.jooq.Condition;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static org.jooq.impl.DSL.*;

public class TimetableController extends WindowController implements Initializable {
    TimetableModel model = new TimetableModel();

    @FXML
    private ScrollPane board;

    @FXML
    private Button updateButton;

    @FXML
    private TextField flightDuration;

    @FXML
    private TextField flightNumberField;

    @FXML
    private ComboBox<String> stationTo;

    @FXML
    private ComboBox<String> stationFrom;

    @FXML
    private TextField minCost;

    @FXML
    private TextField maxCost;

    @FXML
    private JFXCheckBox actualFlights;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new IntegerFieldController(flightNumberField);

        new StationListController(stationFrom);
        new StationListController(stationTo);


        board.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        stationFrom.getSelectionModel().selectedItemProperty()
                .addListener((options, oldValue, newValue) -> onFiltersSelected());
        stationTo.getSelectionModel().selectedItemProperty()
                .addListener((options, oldValue, newValue) -> onFiltersSelected());

        flightNumberField.setOnKeyTyped(u -> onFiltersSelected());
        flightDuration.setOnKeyTyped(u    -> onFiltersSelected());
        minCost.setOnKeyTyped(u           -> onFiltersSelected());
        maxCost.setOnKeyTyped(u           -> onFiltersSelected());
        updateButton.setOnMouseClicked(u  -> onFiltersSelected());
        actualFlights.setOnMouseClicked(u -> onFiltersSelected());

        new IntegerFieldController(flightDuration);
        new DoubleFieldController(minCost);
        new DoubleFieldController(maxCost);
    }


    public void updateTimetable() {
        final String fxmlFile = "/entrance/pages/timetable/flightInfo.fxml";
        final WindowManagerInterface manager = getWindowManager();
        final double SPACING = 10;

        VBox layout = new VBox();
        layout.setSpacing(SPACING);

        board.setContent(layout);
        for (var info : model.getTimetable()) {
            FlightInfoController controller = manager.loadScene(fxmlFile ,this);
            AnchorPane node = (AnchorPane) controller.getView();
            node.prefWidthProperty().bind(board.widthProperty());

            layout.getChildren().add(node);
            controller.setModel(info);
            controller.updateView();
        }
    }


    private void onFiltersSelected() {
        LinkedList<Condition> filters = new LinkedList<>();

        String flightNumberText = flightNumberField.getText();
        if (!flightNumberText.isEmpty()) {
            filters.add(condition(new FlightMatchers
                    .MatchByFlightIdBeginning()
                    .bind(flightNumberText)
                    .getCondition()));
        }
        if (!minCost.getText().isEmpty()) {
            filters.add(condition(new FlightMatchers
                    .MatchByTicketCost()
                    .bind(minCost.getText())
                    .comparator(Matcher.NOT_LESS)
                    .getCondition()
            ));
        }
        if (!maxCost.getText().isEmpty()) {
            filters.add(condition(new FlightMatchers
                    .MatchByTicketCost()
                    .bind(maxCost.getText())
                    .comparator(Matcher.NOT_GREATER)
                    .getCondition()
            ));
        }
        if (!flightDuration.getText().isEmpty()) {
            filters.add(condition(new FlightMatchers
                    .MatchByFlightDuration()
                    .bind(flightDuration.getText())
                    .getCondition()
            ));
        }
        if (actualFlights.isSelected()) {
            filters.add(condition(new FlightMatchers.ActualFlight().getCondition()));
        }
        if (stationFrom.getSelectionModel().getSelectedIndex() > 0) {
            filters.add(field(FlightDomains.Timetable.START_STATION_NAME).eq(stationFrom.getValue()));
        }
        if (stationTo.getSelectionModel().getSelectedIndex() > 0) {
            filters.add(field(FlightDomains.Timetable.FINISH_STATION_NAME).eq(stationTo.getValue()));
        }

        model.setFilters(filters);
        updateTimetable();
    }
}
