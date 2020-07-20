package com.railway.ui.window.entrance.pages.tickets;

import com.railway.database.Matcher;
import com.railway.database.tables.tickets.TicketMatchers;
import com.railway.ui.base.BaseController;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManager;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class TicketsFormController extends WindowController implements Initializable {
    private TicketsFormModel model = new TicketsFormModel();
    private List<FilterInterface> filterInterfaces = new LinkedList<>();
    private int currentIndex = 0;

    private static final int SEARCH_BY_FLIGHT_NUMBER = 0;
    private static final int SEARCH_BY_PURCHASE_DATE = 1;
    private static final int SEARCH_BY_PURCHASE_DATE_INTERVAL = 2;
    private static final int SEARCH_BY_FLIGHT_ROUTE = 3;
    private static final int SEARCH_BY_TICKET_COST = 4;
    private static final int SEARCH_BY_FLIGHT_DURATION = 5;

    private static final String filterFXML[] = {
            "/entrance/pages/tickets/filters/flightNumber.fxml",
            "/entrance/pages/tickets/filters/operationDate.fxml",
            "/entrance/pages/tickets/filters/dateInterval.fxml",
            "/entrance/pages/tickets/filters/route.fxml",
            "/entrance/pages/tickets/filters/ticketCost.fxml",
            "/entrance/pages/tickets/filters/flightDuration.fxml"
    };

    @FXML
    private StackPane filters;

    @FXML
    private Button searchButton;

    @FXML
    private Button caseFlightNumber;

    @FXML
    private Button casePurchaseDate;

    @FXML
    private Button caseDateInterval;

    @FXML
    private Button caseRoute;

    @FXML
    private Button caseTicketCost;

    @FXML
    private Button caseFlightDuration;

    @FXML
    private ToggleGroup paymentType;

    @FXML
    private RadioButton allPurchaseTypes;

    @FXML
    private RadioButton onlyPurchased;

    @FXML
    private RadioButton onlyReserved;

    @FXML
    private TableView<Ticket> ticketsTableView;

    @FXML
    private Label ticketsCount;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setup() {
        setupFilters();
        setupSearch();
        setupView();

        hideAll();
    }

    private void setupFilters() {
        WindowManagerInterface manager = getWindowManager();

        for (String fxml : filterFXML) {
            filterInterfaces.add(manager.loadScene(fxml));
        }
        filters.getChildren().addAll(filterInterfaces
                .stream()
                .map(BaseController::getView)
                .collect(Collectors.toList()));
    }

    private void setupView() {
        final double ID_COLUMN_RATIO = 0.05;
        final double PURCHASED_COLUMN_RATIO = 0.1;
        final double FLIGHT_DURATION = 0.1;
        final double COST_COLUMN_RATIO = 0.1;
        final double DATE_COLUMN_RATIO = 0.2;
        final double OWNER_COLUMN_RATIO = 0.45;

        TableColumn<Ticket, String> ticketId = createColumn(
                "ID", "id", ID_COLUMN_RATIO);
        TableColumn<Ticket, String> operationDate = createColumn(
                "Operation date", "operationDate", DATE_COLUMN_RATIO);
        TableColumn<Ticket, String> ticketCost = createColumn(
                "Cost", "cost", COST_COLUMN_RATIO);
        TableColumn<Ticket, String> duration = createColumn(
                "Duration", "duration", FLIGHT_DURATION);
        TableColumn<Ticket, String> purchased = createColumn(
                "Purchased", "purchased", PURCHASED_COLUMN_RATIO);

        TableColumn<Ticket, String> owner = new TableColumn<>("Owner");
        {
            final double COLS_COUNT = 3;
            double ratio = OWNER_COLUMN_RATIO / COLS_COUNT;
            TableColumn<Ticket, String> lastName = createColumn(
                    "Last name", "ownerLastName", ratio);
            TableColumn<Ticket, String> firstName = createColumn(
                    "First name", "ownerFirstName", ratio);
            TableColumn<Ticket, String> secondName = createColumn(
                    "Second name", "ownerSecondName", ratio);

            owner.getColumns().add(lastName);
            owner.getColumns().add(firstName);
            owner.getColumns().add(secondName);
        }

        ticketsTableView.getColumns().add(ticketId);
        ticketsTableView.getColumns().add(duration);
        ticketsTableView.getColumns().add(ticketCost);
        ticketsTableView.getColumns().add(purchased);
        ticketsTableView.getColumns().add(operationDate);
        ticketsTableView.getColumns().add(owner);
    }

    private TableColumn<Ticket, String> createColumn(String name, String valueName, double widthRatio) {
        TableColumn<Ticket, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(valueName));
        column.prefWidthProperty().bind(ticketsTableView
                .widthProperty()
                .multiply(widthRatio));
        return column;
    }

    private void setupSearch() {
        paymentType.selectToggle(allPurchaseTypes);

        caseFlightNumber.setOnMouseClicked(e   -> show(SEARCH_BY_FLIGHT_NUMBER));
        casePurchaseDate.setOnMouseClicked(e   -> show(SEARCH_BY_PURCHASE_DATE));
        caseDateInterval.setOnMouseClicked(e   -> show(SEARCH_BY_PURCHASE_DATE_INTERVAL));
        caseRoute.setOnMouseClicked(e          -> show(SEARCH_BY_FLIGHT_ROUTE));
        caseTicketCost.setOnMouseClicked(e     -> show(SEARCH_BY_TICKET_COST));
        caseFlightDuration.setOnMouseClicked(e -> show(SEARCH_BY_FLIGHT_DURATION));

        searchButton.setOnMouseClicked(e ->
                updateAll(filterInterfaces.get(currentIndex).getMatchers()));
    }


    private void hideAll() {
        allPurchaseTypes.setVisible(false);
        onlyPurchased.setVisible(false);
        onlyReserved.setVisible(false);

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

        allPurchaseTypes.setVisible(true);
        onlyPurchased.setVisible(true);
        onlyReserved.setVisible(true);
    }

    private void updateAll(Collection<Matcher> matchers) {
        Collection<Matcher> updated = addPurchaseFilters(matchers);
        ObservableList<Ticket> items = FXCollections.observableList(
                model.getTicketList(updated));

        ticketsTableView.setItems(items);
        ticketsCount.setText("Count: " + model.getTicketCount(updated));
    }

    private Collection<Matcher> addPurchaseFilters(Collection<Matcher> matchers) {
        if (onlyReserved.isSelected()) {
            matchers.add(new TicketMatchers.MatchByPurchase().bind(false));
        }
        else if (onlyPurchased.isSelected()) {
            matchers.add(new TicketMatchers.MatchByPurchase().bind(true));
        }
        return matchers;
    }
}
