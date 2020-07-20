package com.railway.ui.window.entrance.pages.routes.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.routes.RouteMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.common.listControllers.routes.RouteTypeListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class RouteTypeFilter extends FilterInterface implements Initializable {

    @FXML
    private ComboBox<String> routeTypeBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new RouteTypeListController(routeTypeBox);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new RouteMatchers.MatchByRouteType().bind(routeTypeBox.getValue()));
        return matchers;
    }
}
