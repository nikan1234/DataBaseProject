package com.railway.ui.window.entrance.pages.routes.filters;

import com.railway.database.Matcher;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class AllRoutes extends FilterInterface implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public Collection<Matcher> getMatchers() {
        return new LinkedList<>();
    }
}
