package com.railway.ui.window.entrance.pages.delays.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.routes.RouteMatchers;
import com.railway.ui.window.common.entity.Route;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.common.listControllers.routes.RouteListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class DelayRouteFilter extends FilterInterface implements Initializable {
    private RouteListController controller;

    @FXML
    private ComboBox<Route> routeBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new RouteListController(routeBox);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        Route route = controller.getSelectedRoute();
        if (route != null) {
            matchers.add(new RouteMatchers.MatchByRouteId().bind(route.getRouteId()));
        }
        return matchers;
    }
}
