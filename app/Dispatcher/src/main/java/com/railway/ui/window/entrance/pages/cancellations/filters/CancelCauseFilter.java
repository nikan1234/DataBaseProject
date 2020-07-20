package com.railway.ui.window.entrance.pages.cancellations.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.cancellations.CancellationMatchers;
import com.railway.database.tables.delays.DelayMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.common.listControllers.delays.DelayCauseListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class CancelCauseFilter extends FilterInterface implements Initializable {
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
        if (delayCause == null) {
            return matchers;
        }
        if (!delayCause.isEmpty()) {
            matchers.add(new CancellationMatchers
                    .MatchByCancelCause()
                    .bind(delayCauseBox.getValue()));
        }
        return matchers;
    }
}
