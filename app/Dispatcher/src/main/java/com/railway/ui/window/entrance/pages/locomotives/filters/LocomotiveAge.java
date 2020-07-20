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

public class LocomotiveAge extends FilterInterface implements Initializable {

    @FXML
    private TextField ageField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new IntegerFieldController(ageField);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        String age = ageField.getText();
        if (!age.isEmpty()) {
            matchers.add(new LocomotiveMatchers.MatchByAge().bind(age));
        }
        return matchers;
    }
}
