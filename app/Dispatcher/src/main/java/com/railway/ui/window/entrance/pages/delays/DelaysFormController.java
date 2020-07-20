package com.railway.ui.window.entrance.pages.delays;

import com.railway.database.Matcher;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManager;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.common.entity.Flight;
import com.railway.ui.window.common.entity.Ticket;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.entrance.pages.delays.actions.add.AddDelayController;
import com.railway.ui.window.entrance.pages.delays.actions.remove.RemoveDelayController;
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

public class DelaysFormController extends WindowController implements Initializable {
    private DelaysFormModel model = new DelaysFormModel();
    private List<FilterInterface> filterInterfaces = new LinkedList<>();
    private int currentIndex = 0;

    @FXML
    private TableView<DelayedFlight> flightsTable;

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
    private TableView<Ticket> ticketsTable;

    @FXML
    private Label flightsCount;

    @FXML
    private Label ticketsCount;

    @FXML
    private StackPane filters;

    private static final int SEARCH_ALL_FLIGHTS = 0;
    private static final int SEARCH_BY_CAUSE = 1;
    private static final int SEARCH_BY_ROUTE = 2;
    private static final int SEARCH_BY_DIRECTION = 3;

    private static final String filterFXML[] = {
            "/entrance/pages/delays/filters/allFlights.fxml",
            "/entrance/pages/delays/filters/delayCause.fxml",
            "/entrance/pages/delays/filters/route.fxml",
            "/entrance/pages/delays/filters/direction.fxml"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    void addDelay() {
        final String fxml = "/entrance/pages/delays/actions/addDelay.fxml";
        AddDelayController controller = getWindowManager().loadScene(fxml, this);
        getWindowManager().showScene(controller);
    }

    @FXML
    void removeDelay() {
        final String fxml = "/entrance/pages/delays/actions/removeDelay.fxml";
        RemoveDelayController controller = getWindowManager().loadScene(fxml, this);
        getWindowManager().showScene(controller);
    }

    public void setup() {
        setupFilters();
        setupSearch();

        setupFlightsTable();
        setupTicketsTable();

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
            updateTables(filterInterfaces.get(idx).getMatchers());
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

    private void updateTables(Collection<Matcher> matchers) {
        flightsTable.getItems().setAll(model.getFlights(matchers));
        ticketsTable.getItems().setAll(model.getTickets(matchers));

        flightsCount.setText("Count: " + model.getFlightsCount(matchers));
        ticketsCount.setText("Count: " + model.getTicketsCount(matchers));
    }

    private void showFlightDetailsPage(Flight flight) {
        final String fxml = "/entrance/pages/flightDetails/flightDetails.fxml";
        WindowManagerInterface manager = getWindowManager();
        FlightDetailsFormController controller = manager.loadScene(fxml, this);
        controller.setFlightNumber(flight.getFlightNumber());

        manager.showScene(controller);
    }

    private void setupFlightsTable() {
        final double FLIGHT_NUMBER_COL_RATIO = 0.1;
        final double FLIGHT_DATE_COL_RATIO = 0.3;
        final double FLIGHT_DELAY_COL_RATIO = 0.3;
        final double FLIGHT_TYPE_COL_RATIO = 0.3;


        flightsTable.getColumns().add(flightColumn("NUMBER", "flightNumber", FLIGHT_NUMBER_COL_RATIO));
        flightsTable.getColumns().add(flightColumn("FLIGHT DATE", "flightDate", FLIGHT_DATE_COL_RATIO));
        flightsTable.getColumns().add(flightColumn("DELAYED UNTIL", "delayedUntil", FLIGHT_DELAY_COL_RATIO));
        flightsTable.getColumns().add(flightColumn("FLIGHT TYPE", "flightType", FLIGHT_TYPE_COL_RATIO));

        flightsTable.setRowFactory(tv -> {
            TableRow<DelayedFlight> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    DelayedFlight rowData = row.getItem();

                    /* Flight page */
                    showFlightDetailsPage(rowData);
                }
            });
            return row;
        });
    }

    private void setupTicketsTable() {
        final double ID_COLUMN_RATIO = 0.1;
        final double PURCHASED_COLUMN_RATIO = 0.1;
        final double COST_COLUMN_RATIO = 0.1;
        final double DATE_COLUMN_RATIO = 0.2;
        final double OWNER_COLUMN_RATIO = 0.5;

        ticketsTable.getColumns().add(ticketColumn("ID", "id", ID_COLUMN_RATIO));
        ticketsTable.getColumns().add(ticketColumn("Operation date", "operationDate", DATE_COLUMN_RATIO));
        ticketsTable.getColumns().add(ticketColumn("Cost", "cost", COST_COLUMN_RATIO));
        ticketsTable.getColumns().add(ticketColumn("Purchased", "purchased", PURCHASED_COLUMN_RATIO));

        TableColumn<Ticket, String> owner = new TableColumn<>("Owner");
        {
            final double COLS_COUNT = 3;
            double ratio = OWNER_COLUMN_RATIO / COLS_COUNT;
            TableColumn<Ticket, String> lastName = ticketColumn(
                    "Last name", "ownerLastName", ratio);
            TableColumn<Ticket, String> firstName = ticketColumn(
                    "First name", "ownerFirstName", ratio);
            TableColumn<Ticket, String> secondName = ticketColumn(
                    "Second name", "ownerSecondName", ratio);

            owner.getColumns().add(lastName);
            owner.getColumns().add(firstName);
            owner.getColumns().add(secondName);
        }
        ticketsTable.getColumns().add(owner);
    }

    private TableColumn<DelayedFlight, String> flightColumn(String name, String arg, double ratio) {
        TableColumn<DelayedFlight, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(arg));
        column.prefWidthProperty().bind(flightsTable.widthProperty().multiply(ratio));
        return column;
    }

    private TableColumn<Ticket, String> ticketColumn(String name, String arg, double ratio) {
        TableColumn<Ticket, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(arg));
        column.prefWidthProperty().bind(ticketsTable.widthProperty().multiply(ratio));
        return column;
    }
}
