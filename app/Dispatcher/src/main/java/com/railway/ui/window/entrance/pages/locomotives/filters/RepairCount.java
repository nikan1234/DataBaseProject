package com.railway.ui.window.entrance.pages.locomotives.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.locomotives.LocomotiveMatchers;
import com.railway.ui.window.common.fieldContollers.IntegerFieldController;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class RepairCount extends FilterInterface implements Initializable {

    @FXML
    private TextField repairCountField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new IntegerFieldController(repairCountField);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        String count = repairCountField.getText();
        if (!count.isEmpty()) {
            matchers.add(new LocomotiveMatchers
                    .MatchByRepairCount().bind(count));
        }
        return matchers;
    }
}
