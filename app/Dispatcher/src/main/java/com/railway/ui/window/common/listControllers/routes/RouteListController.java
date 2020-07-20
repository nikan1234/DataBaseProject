package com.railway.ui.window.common.listControllers.routes;

import com.railway.ui.window.common.entity.Route;
import javafx.scene.control.ComboBox;

import java.util.List;

public class RouteListController {
    private List<Route> routes;
    private ComboBox<Route> routeBox;

    public RouteListController(ComboBox<Route> routeBox) {
        this.routeBox = routeBox;
        this.routes = new RouteListModel().getRoutes();

        routeBox.getItems().addAll(routes);
        if (routes.size() > 0) {
            routeBox.setValue(routes.get(0));
        }
    }

    public Route getSelectedRoute() {
        int idx = routeBox.getSelectionModel().getSelectedIndex();
        if (idx >= 0) {
            return routes.get(idx);
        }
        return null;
    }
}
