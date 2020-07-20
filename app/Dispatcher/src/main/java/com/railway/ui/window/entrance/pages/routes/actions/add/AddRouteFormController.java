package com.railway.ui.window.entrance.pages.routes.actions.add;

import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Station;
import com.railway.ui.window.common.fieldContollers.IntegerFieldController;
import com.railway.ui.window.common.listControllers.routes.RouteTypeListController;
import com.railway.ui.window.common.listControllers.stations.StationListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class AddRouteFormController extends WindowController implements Initializable {
    private AddRouteFormModel model = new AddRouteFormModel();

    static class Path {
        Station from;
        Station to;
        int duration;

        Path(Station from, Station to, int duration) {
            this.from = from;
            this.to = to;
            this.duration = duration;
        }
    }

    private List<Path> paths = new LinkedList<>();

    @FXML
    private ListView<Station> stationList;

    @FXML
    private ComboBox<Station> toStation;

    @FXML
    private ComboBox<Station> startStation;

    @FXML
    private TextField durationField;

    @FXML
    private Button addButton;

    @FXML
    private TextField fromStation;

    @FXML
    private Label resultLabel;

    @FXML
    private TextField routeId;

    @FXML
    private ComboBox<String> routeType;

    @FXML
    private Button createRoute;

    @FXML
    private Label finalLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new RouteTypeListController(routeType);
        new StationListController(startStation);
        new IntegerFieldController(durationField);

        startStation.getSelectionModel()
                .selectedItemProperty()
                .addListener((options, oldValue, newValue) -> reset());

        reset();

        createRoute.setOnMouseClicked(e -> {
            finalLabel.setText("");

            String text = routeId.getText();
            if (text.isEmpty()) {
                finalLabel.setText("Specify route id");
                return;
            }
            int id = Integer.parseInt(text);
            String type = routeType.getValue();

            if (paths.size() == 0) {
                finalLabel.setText("Specify route path");
                return;
            }
            int result = model.addRoute(id, type, paths);
            finalLabel.setText("RESULT: " + Errors.toString(result));
        });
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }

    private void reset() {
        new StationListController(toStation);

        Station start = startStation.getValue();
        stationList.getItems().clear();
        stationList.getItems().add(start);

        fromStation.setText(start.toString());
        removeStation(start);

        addButton.setOnMouseClicked(e -> {
            resultLabel.setText("");
            try {
                Path path = getPath();
                paths.add(path);

                Station to = path.to;

                fromStation.setText(to.toString());
                stationList.getItems().add(to);
                removeStation(to);
            }
            catch (final Exception exception) {
                resultLabel.setText(exception.getMessage());
            }
        });

        fromStation.setEditable(false);
    }

    private void removeStation(Station station) {
        toStation.getItems().remove(station);

        if (toStation.getItems().size() > 0) {
            toStation.setValue(toStation.getItems().get(0));
        }
    }

    private Path getPath() throws RuntimeException {
        Station from = stationList.getItems().get(
                stationList.getItems().size() - 1);
        Station to = toStation.getValue();

        if (from == null || to == null)
            throw new RuntimeException("There are no more stations");

        String duration = durationField.getText();
        if (duration.isEmpty()) {
            throw new RuntimeException("Specify duration");
        }

        return new Path(from , to, Integer.parseInt(duration));
    }
}
