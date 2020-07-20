package com.railway.ui.window.entrance.pages.passengers;

import com.railway.database.Matcher;
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

public class PassengersFormController extends WindowController implements Initializable {
    private PassengersFormModel model = new PassengersFormModel();
    private List<FilterInterface> filterInterfaces = new LinkedList<>();
    private int selectedIndex = 0;

    private static final int SEARCH_BY_FLIGHT_NUMBER = 0;
    private static final int SEARCH_BY_FLIGHT_DAY = 1;
    private static final int SEARCH_BY_PASSENGER_AGE = 2;
    private static final int SEARCH_BY_PASSENGER_GENDER = 3;
    private static final int SEARCH_BY_PASSENGER_BAGGAGE = 4;

    private static final String filterFXML[] = {
            "/entrance/pages/passengers/filters/flightNumber.fxml",
            "/entrance/pages/passengers/filters/flightDay.fxml",
            "/entrance/pages/passengers/filters/passengerAge.fxml",
            "/entrance/pages/passengers/filters/passengerGender.fxml",
            "/entrance/pages/passengers/filters/passengerBaggage.fxml"
    };

    @FXML
    private StackPane filters;

    @FXML
    private Button caseFlightNumber;

    @FXML
    private Button caseFlightDay;

    @FXML
    private Button casePassengerAge;

    @FXML
    private Button casePassengerGender;

    @FXML
    private Button casePassengerBaggage;

    @FXML
    private Button searchButton;


    @FXML
    private TableView<Passenger> passengersTableView;

    @FXML
    private Label passengerCount;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setup() {
        setupFilters();
        setupTableView();
        setupSearch();

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

    private void setupTableView() {
        final double LAST_NAME_COLUMN_RATIO = 0.25;
        final double FIRST_NAME_COLUMN_RATIO = 0.25;
        final double SECOND_NAME_COLUMN_RATIO = 0.25;
        final double AGE_COLUMN_RATIO = 0.1;
        final double GENDER_COLUMN_RATIO = 0.15;

        TableColumn<Passenger, String> lastName = new TableColumn<>("Last name");
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastName.prefWidthProperty().bind(passengersTableView
                .widthProperty()
                .multiply(LAST_NAME_COLUMN_RATIO));

        TableColumn<Passenger, String> firstName = new TableColumn<>("First name");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstName.prefWidthProperty().bind(passengersTableView
                .widthProperty()
                .multiply(FIRST_NAME_COLUMN_RATIO));

        TableColumn<Passenger, String> secondName = new TableColumn<>("Second name");
        secondName.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        secondName.prefWidthProperty().bind(passengersTableView
                .widthProperty()
                .multiply(SECOND_NAME_COLUMN_RATIO));

        TableColumn<Passenger, Integer> age = new TableColumn<>("Age");
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        age.prefWidthProperty().bind(passengersTableView
                .widthProperty()
                .multiply(AGE_COLUMN_RATIO));

        TableColumn<Passenger, String> gender = new TableColumn<>("Gender");
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        gender.prefWidthProperty().bind(passengersTableView
                .widthProperty()
                .multiply(GENDER_COLUMN_RATIO));

        passengersTableView.getColumns().clear();
        passengersTableView.getColumns().add(lastName);
        passengersTableView.getColumns().add(firstName);
        passengersTableView.getColumns().add(secondName);
        passengersTableView.getColumns().add(age);
        passengersTableView.getColumns().add(gender);
    }

    private void setupSearch() {
        caseFlightNumber.setOnMouseClicked(e -> {
            show(SEARCH_BY_FLIGHT_NUMBER);
        });

        caseFlightDay.setOnMouseClicked(e -> {
            show(SEARCH_BY_FLIGHT_DAY);
        });

        casePassengerAge.setOnMouseClicked(e -> {
            show(SEARCH_BY_PASSENGER_AGE);
        });

        casePassengerGender.setOnMouseClicked(e -> {
            show(SEARCH_BY_PASSENGER_GENDER);
        });

        casePassengerBaggage.setOnMouseClicked(e -> {
            show(SEARCH_BY_PASSENGER_BAGGAGE);
        });

        searchButton.setOnMouseClicked(e -> {
            updateAll(filterInterfaces.get(selectedIndex).getMatchers());
        });
    }

    private void hideAll() {
        searchButton.setVisible(false);
        for (FilterInterface child : filterInterfaces) {
            child.getView().setVisible(false);
        }
    }

    private void show(int idx) {
        selectedIndex = idx;

        hideAll();
        Node node = filterInterfaces.get(idx).getView();
        node.toFront();
        node.setVisible(true);

        searchButton.setVisible(true);
    }

    private void updateAll(Collection<Matcher> matchers) {
        updateList(model.getPassengerList(matchers));
        updateCount(model.getPassengerCount(matchers));
    }

    private void updateList(List<Passenger> passengers) {
        ObservableList<Passenger> items = FXCollections.observableList(passengers);
        passengersTableView.setItems(items);
    }

    private void updateCount(int count) {
        passengerCount.setText("Count: " + count);
    }
}
