package com.railway.ui.window.entrance.pages.locomotives;

import com.railway.database.Matcher;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.common.entity.Locomotive;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.entrance.pages.locomotives.actions.add.AddLocomotiveFormController;
import com.railway.ui.window.entrance.pages.locomotives.actions.remove.RemoveLocomotiveFormController;
import com.railway.ui.window.entrance.pages.locomotives.actions.remove.RemoveLocomotiveFormModel;
import com.railway.ui.window.entrance.pages.locomotives.info.LocomotiveInfoFormController;
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

public class LocomotiveFormController extends WindowController implements Initializable {
    private LocomotiveFromModel model = new LocomotiveFromModel();
    private List<FilterInterface> filterInterfaces = new LinkedList<>();
    private int currentIndex = 0;

    @FXML
    private TableView<Locomotive> locomotivesTable;

    @FXML
    private StackPane filters;

    @FXML
    private Button caseAllLocomotives;

    @FXML
    private Button caseOnThisStation;

    @FXML
    private Button caseStationsArrival;

    @FXML
    private Button caseFlightsCount;

    @FXML
    private Button caseInspection;

    @FXML
    private Button caseRepair;

    @FXML
    private Button caseRepairsCount;

    @FXML
    private Button caseLocomotiveAge;

    @FXML
    private Button searchButton;

    @FXML
    private Label locomotivesCount;

    private static final int SEARCH_ALL_LOCOMOTIVES = 0;
    private static final int SEARCH_ON_THIS_STATION = 1;
    private static final int SEARCH_BY_FLIGHTS_COUNT = 2;
    private static final int SEARCH_BY_STATIONS_ARRIVAL = 3;
    private static final int SEARCH_BY_INSPECTION = 4;
    private static final int SEARCH_BY_REPAIR = 5;
    private static final int SEARCH_BY_REPAIR_COUNT = 6;
    private static final int SEARCH_BY_LOCOMOTIVE_AGE = 7;

    private static final String[] filterFXML = {
            "/entrance/pages/locomotives/filters/allLocomotives.fxml",
            "/entrance/pages/locomotives/filters/onThisStation.fxml",
            "/entrance/pages/locomotives/filters/flightsCount.fxml",
            "/entrance/pages/locomotives/filters/stationArrivalTime.fxml",
            "/entrance/pages/locomotives/filters/hadInspection.fxml",
            "/entrance/pages/locomotives/filters/hadRepair.fxml",
            "/entrance/pages/locomotives/filters/repairCount.fxml",
            "/entrance/pages/locomotives/filters/locomotiveAge.fxml",
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void addLocomotive() {
        final String fxml = "/entrance/pages/locomotives/actions/addLocomotive.fxml";

        WindowManagerInterface manager = getWindowManager();
        AddLocomotiveFormController controller = manager.loadScene(fxml, this);
        manager.showScene(controller);
    }


    @FXML
    void removeLocomotive() {
        final String fxml = "/entrance/pages/locomotives/actions/removeLocomotive.fxml";

        WindowManagerInterface manager = getWindowManager();
        RemoveLocomotiveFormController controller = manager.loadScene(fxml, this);
        manager.showScene(controller);
    }



    public void setup() {
        setupFilters();
        setupSearch();
        setupTableView();

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
        caseStationsArrival.setOnMouseClicked(e -> show(SEARCH_BY_STATIONS_ARRIVAL));
        caseAllLocomotives.setOnMouseClicked(e -> show(SEARCH_ALL_LOCOMOTIVES));
        caseOnThisStation.setOnMouseClicked(e -> show(SEARCH_ON_THIS_STATION));
        caseFlightsCount.setOnMouseClicked(e -> show(SEARCH_BY_FLIGHTS_COUNT));
        caseInspection.setOnMouseClicked(e -> show(SEARCH_BY_INSPECTION));
        caseRepair.setOnMouseClicked(e -> show(SEARCH_BY_REPAIR));
        caseRepairsCount.setOnMouseClicked(e -> show(SEARCH_BY_REPAIR_COUNT));
        caseLocomotiveAge.setOnMouseClicked(e -> show(SEARCH_BY_LOCOMOTIVE_AGE));

        searchButton.setOnMouseClicked(e -> {
            int idx = currentIndex;
            updateTable(filterInterfaces.get(idx).getMatchers());
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

    private void setupTableView() {
        final double LOCOMOTIVE_ID_COL_RATIO = 0.2;
        final double LOCOMOTIVE_NAME_COL_RATIO = 0.4;
        final double LOCOMOTIVE_YEAR_COL_RATIO = 0.4;

        locomotivesTable.getColumns().add(getColumn("ID", "locomotiveId", LOCOMOTIVE_ID_COL_RATIO));
        locomotivesTable.getColumns().add(getColumn("NAME", "locomotiveName", LOCOMOTIVE_NAME_COL_RATIO));
        locomotivesTable.getColumns().add(getColumn("ENTRY DATE", "entryDate", LOCOMOTIVE_YEAR_COL_RATIO));

        locomotivesTable.setRowFactory(tv -> {
            TableRow<Locomotive> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Locomotive rowData = row.getItem();

                    /* Locomotive page */
                    showLocomotivePage(rowData);
                }
            });
            return row;
        });
    }

    private TableColumn<Locomotive, String> getColumn(String name, String arg, double ratio) {
        TableColumn<Locomotive, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(arg));
        column.prefWidthProperty().bind(locomotivesTable.widthProperty().multiply(ratio));
        return column;
    }

    private void showLocomotivePage(Locomotive locomotive) {
        final String fxml = "/entrance/pages/locomotives/info/locomotiveInfo.fxml";

        WindowManagerInterface manager = getWindowManager();
        LocomotiveInfoFormController controller = manager.loadScene(fxml, this);
        controller.setLocomotiveId(locomotive.getLocomotiveId());
        manager.showScene(controller);
    }

    private void updateTable(Collection<Matcher> matchers) {
        locomotivesTable.getItems().setAll(model.getLocomotives(matchers));
        locomotivesCount.setText("Count: " + model.getLocomotivesCount(matchers));
    }
}
