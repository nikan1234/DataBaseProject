package com.railway.ui.window.entrance.pages.cancellations;

import com.railway.database.Matcher;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManager;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.common.entity.Flight;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.entrance.pages.cancellations.actions.add.AddCancellationController;
import com.railway.ui.window.entrance.pages.cancellations.actions.remove.RemoveCancellationController;
import com.railway.ui.window.entrance.pages.delays.actions.add.AddDelayController;
import com.railway.ui.window.entrance.pages.flightDetails.FlightDetailsFormController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CancelFormController extends WindowController implements Initializable {
    private CancelFormModel model = new CancelFormModel();
    private List<FilterInterface> filterInterfaces = new LinkedList<>();
    private int currentIndex = 0;

    @FXML
    private TableView<Flight> flightsTable;

    @FXML
    private Button caseAllFlights;

    @FXML
    private Button caseDirection;

    @FXML
    private Button caseDelayCause;

    @FXML
    private Button caseRoute;

    @FXML
    private Button searchButton;

    @FXML
    private Label flightsCount;

    @FXML
    private StackPane filters;

    private static final int SEARCH_ALL_FLIGHTS = 0;
    private static final int SEARCH_BY_CAUSE = 1;
    private static final int SEARCH_BY_ROUTE = 2;
    private static final int SEARCH_BY_DIRECTION = 3;

    private static final String filterFXML[] = {
            "/entrance/pages/cancellations/filters/allFlights.fxml",
            "/entrance/pages/cancellations/filters/cancelCause.fxml",
            "/entrance/pages/cancellations/filters/route.fxml",
            "/entrance/pages/cancellations/filters/direction.fxml"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void addCancellation() {
        final String fxml = "/entrance/pages/cancellations/actions/addCancellation.fxml";
        AddCancellationController controller = getWindowManager().loadScene(fxml, this);
        getWindowManager().showScene(controller);
    }

    @FXML
    void removeCancellation() {
        final String fxml = "/entrance/pages/cancellations/actions/removeCancellation.fxml";
        RemoveCancellationController controller = getWindowManager().loadScene(fxml, this);
        getWindowManager().showScene(controller);
    }

    private void showFlightDetailsPage(Flight flight) {
        final String fxml = "/entrance/pages/flightDetails/flightDetails.fxml";
        WindowManagerInterface manager = getWindowManager();
        FlightDetailsFormController controller = manager.loadScene(fxml, this);
        controller.setFlightNumber(flight.getFlightNumber());

        manager.showScene(controller);
    }

    public void setup() {
        setupFilters();
        setupSearch();
        setupFlightsTable();

        hideAll();
    }

    private void setupFilters() {
        WindowManagerInterface manager = getWindowManager();

        for (String fxml : filterFXML) {
            filterInterfaces.add(manager.loadScene(fxml));
        }
        filters.getChildren().addAll(filterInterfaces
                .stream()
                .map(e -> {
                    AnchorPane node = (AnchorPane) e.getView();
                    node.prefWidthProperty().bind(filters.widthProperty());
                    node.prefHeightProperty().bind(filters.heightProperty());
                    return e.getView();
                })
                .collect(Collectors.toList()));
    }

    private void setupSearch() {
        caseAllFlights.setOnMouseClicked(e -> show(SEARCH_ALL_FLIGHTS));
        caseDirection.setOnMouseClicked(e -> show(SEARCH_BY_DIRECTION));
        caseDelayCause.setOnMouseClicked(e -> show(SEARCH_BY_CAUSE));
        caseRoute.setOnMouseClicked(e -> show(SEARCH_BY_ROUTE));

        searchButton.setOnMouseClicked(e -> {
            int idx = currentIndex;
            updateTable(filterInterfaces.get(idx).getMatchers());
        });
    }

    private void setupFlightsTable() {
        final double FLIGHT_NUMBER_COL_RATIO = 0.1;
        final double FLIGHT_DATE_COL_RATIO = 0.3;
        final double FLIGHT_TYPE_COL_RATIO = 0.3;


        flightsTable.getColumns().add(flightColumn("NUMBER", "flightNumber", FLIGHT_NUMBER_COL_RATIO));
        flightsTable.getColumns().add(flightColumn("FLIGHT DATE", "flightDate", FLIGHT_DATE_COL_RATIO));
        flightsTable.getColumns().add(flightColumn("FLIGHT TYPE", "flightType", FLIGHT_TYPE_COL_RATIO));

        flightsTable.setRowFactory(tv -> {
            TableRow<Flight> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Flight rowData = row.getItem();

                    /* Flight page */
                    showFlightDetailsPage(rowData);
                }
            });
            return row;
        });
    }

    private void hideAll() {
        searchButton.setVisible(false);
        for (FilterInterface child : filterInterfaces) {
            child.getView().setVisible(false);
        }
    }

    private void show(int idx) {
        currentIndex = idx;

        hideAll();
        Node node = filterInterfaces.get(idx).getView();
        node.toFront();
        node.setVisible(true);

        searchButton.setVisible(true);
    }

    private void updateTable(Collection<Matcher> matchers) {
        flightsTable.getItems().setAll(model.getFlights(matchers));
        flightsCount.setText("Count: " + model.getFlightsCount(matchers));
    }

    private TableColumn<Flight, String> flightColumn(String name, String arg, double ratio) {
        TableColumn<Flight, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(arg));
        column.prefWidthProperty().bind(flightsTable.widthProperty().multiply(ratio));
        return column;
    }
}
