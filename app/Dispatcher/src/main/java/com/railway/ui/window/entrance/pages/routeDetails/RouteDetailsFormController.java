package com.railway.ui.window.entrance.pages.routeDetails;

import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Route;
import com.railway.ui.window.common.entity.Station;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class RouteDetailsFormController extends WindowController implements Initializable {
    private RouteDetailsFormModel model = new RouteDetailsFormModel();

    @FXML
    private Label routeIdLabel;

    @FXML
    private ListView<Station> routeTable;

    @FXML
    private Label routeTypeLabel;

    @FXML
    private Label routeDuration;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }

    public void setRoute(Route route) {
        String format = "Duration: %d hours";

        routeIdLabel.setText(Integer.toString(route.getRouteId()));
        routeTypeLabel.setText(route.getRouteType());
        routeTable.getItems().addAll(model.getRouteDetails(route));
        routeDuration.setText(String.format(format, model.getDuration()));
    }
}
