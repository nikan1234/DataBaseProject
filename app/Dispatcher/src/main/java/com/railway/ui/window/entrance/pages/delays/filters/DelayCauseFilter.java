package com.railway.ui.window.entrance.pages.delays.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.delays.DelayMatchers;
import com.railway.ui.window.common.listControllers.delays.DelayCauseListController;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class DelayCauseFilter extends FilterInterface implements Initializable {
    @FXML
    private ComboBox<String> delayCauseBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new DelayCauseListController(delayCauseBox);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        String delayCause = delayCauseBox.getValue();
        if (!delayCause.isEmpty()) {
            matchers.add(new DelayMatchers.MatchByDelayCause().bind(delayCauseBox.getValue()));
        }
        return matchers;
    }
}
