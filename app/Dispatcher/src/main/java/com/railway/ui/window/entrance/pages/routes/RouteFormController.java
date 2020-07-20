package com.railway.ui.window.entrance.pages.routes;

import com.railway.database.Matcher;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManager;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.common.entity.Route;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.entrance.pages.routeDetails.RouteDetailsFormController;
import com.railway.ui.window.entrance.pages.routes.actions.add.AddRouteFormController;
import com.railway.ui.window.entrance.pages.routes.actions.remove.RemoveRouteFormController;
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

public class RouteFormController extends WindowController implements Initializable {
    private RouteFormModel model = new RouteFormModel();
    private List<FilterInterface> filterInterfaces = new LinkedList<>();
    private int currentIndex = 0;

    @FXML
    private TableView<Route> routesTable;

    @FXML
    private Button caseAllRoutes;

    @FXML
    private Button caseDirection;

    @FXML
    private Button caseRouteType;

    @FXML
    private Button searchButton;

    @FXML
    private Label routesCount;

    @FXML
    private StackPane filters;

    private static final int SEARCH_ALL_ROUTES = 0;
    private static final int SEARCH_BY_DIRECTION = 1;
    private static final int SEARCH_BY_TYPE = 2;

    private static final String filterFXML[] = {
            "/entrance/pages/routes/filters/allRoutes.fxml",
            "/entrance/pages/routes/filters/routeDirection.fxml",
            "/entrance/pages/routes/filters/routeType.fxml"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void addRoute() {
        final String fxml = "/entrance/pages/routes/actions/addRoute.fxml";

        WindowManagerInterface manager = getWindowManager();
        AddRouteFormController controller = manager.loadScene(fxml, this);
        manager.showScene(controller);
    }

    @FXML
    private void removeRoute() {
        final String fxml = "/entrance/pages/routes/actions/removeRoute.fxml";

        WindowManagerInterface manager = getWindowManager();
        RemoveRouteFormController controller = manager.loadScene(fxml, this);
        manager.showScene(controller);
    }

    public void setup() {
        setupFilters();
        setupSearch();

        setupRoutesTable();

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
        caseAllRoutes.setOnMouseClicked(e -> show(SEARCH_ALL_ROUTES));
        caseDirection.setOnMouseClicked(e -> show(SEARCH_BY_DIRECTION));
        caseRouteType.setOnMouseClicked(e -> show(SEARCH_BY_TYPE));

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
        routesTable.getItems().setAll(model.getRoutes(matchers));
        routesCount.setText("Count: " + model.getRouteCount(matchers));
    }

    private void setupRoutesTable() {
        final double ROUTE_ID_COL_RATIO = 0.1;
        final double ROUTE_TYPE_COL_RATIO = 0.3;
        final double ROUTE_START_COL_RATIO = 0.3;
        final double ROUTE_FINISH_COL_RATIO = 0.3;

        routesTable.getColumns().add(routeColumn("#", "routeId", ROUTE_ID_COL_RATIO));
        routesTable.getColumns().add(routeColumn("FROM", "startStation", ROUTE_START_COL_RATIO));
        routesTable.getColumns().add(routeColumn("TO", "finishStation", ROUTE_FINISH_COL_RATIO));
        routesTable.getColumns().add(routeColumn("TYPE", "routeType", ROUTE_TYPE_COL_RATIO));

        routesTable.setRowFactory(tv -> {
            TableRow<Route> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Route rowData = row.getItem();

                    /* Route page */
                    showRouteDetails(rowData);
                }
            });
            return row;
        });
    }

    private void showRouteDetails(Route route) {
        final String fxml = "/entrance/pages/routeDetails/routeDetails.fxml";

        WindowManagerInterface manager = getWindowManager();
        RouteDetailsFormController controller = manager.loadScene(fxml, this);
        controller.setRoute(route);

        manager.showScene(controller);
    }

    private TableColumn<Route, String> routeColumn(String name, String arg, double ratio) {
        TableColumn<Route, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(arg));
        column.prefWidthProperty().bind(routesTable.widthProperty().multiply(ratio));
        return column;
    }
}
