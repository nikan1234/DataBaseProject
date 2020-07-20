package com.railway.ui.window.common.routes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.List;

public class RouteTypeListController {
    private RouteTypeListModel model = new RouteTypeListModel();

    public RouteTypeListController(ComboBox<String> routeTypes) {

        List<String> list = model.getRouteTypes();
        ObservableList<String> items = FXCollections.observableArrayList(list);
        routeTypes.setValue(list.get((0)));
        routeTypes.setItems(items);
    }
}
