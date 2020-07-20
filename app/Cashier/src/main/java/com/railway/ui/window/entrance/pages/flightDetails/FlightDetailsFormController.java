package com.railway.ui.window.entrance.pages.flightDetails;

import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.entrance.pages.flightDetails.actions.ActionInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

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
    private Button buyTicketButton;

    @FXML
    private Button reserveTicketButton;

    @FXML
    private Button purchaseTicketButton;

    @FXML
    private Button returnTicketButton;

    @FXML
    private Label durationField;

    @FXML
    private Label ticketCost;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        routeList = FXCollections.observableArrayList();
        routeTable.setItems(routeList);

        buyTicketButton.setOnMouseClicked(e -> {
            final String fxml = "/entrance/pages/flightDetails/actions/buy.fxml";
            doAction(fxml);
        });

        reserveTicketButton.setOnMouseClicked(e -> {
            final String fxml = "/entrance/pages/flightDetails/actions/reserve.fxml";
            doAction(fxml);
        });

        purchaseTicketButton.setOnMouseClicked(e -> {
            final String fxml = "/entrance/pages/flightDetails/actions/purchase.fxml";
            doAction(fxml);
        });

        returnTicketButton.setOnMouseClicked(e -> {
            final String fxml = "/entrance/pages/flightDetails/actions/refund.fxml";
            doAction(fxml);
        });
    }


    private <C extends WindowController> void doAction(String fxml) {
        C controller = getWindowManager().loadScene(fxml, this);
        ((ActionInterface)controller).setFlightNumber(model.getFlightNumber());
        getWindowManager().showScene(controller);
    }

    public void setFlightNumber(int number) {
        model = new FlightDetailsFormModel(number);
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
        durationField.setText(model.getDuration() + " hours");
        ticketCost.setText(Double.toString(model.getTicketCost()));
    }

    @FXML
    void onPrevButtonClicked(MouseEvent event) {
        getWindowManager().prevScene();
    }
}
