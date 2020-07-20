package com.railway.ui.window.entrance.pages.employees.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.employee.EmployeeMatchers;
import com.railway.ui.window.common.fieldContollers.NumberFieldController;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class AgeFilter extends FilterInterface implements Initializable {

    @FXML
    private TextField ageField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new NumberFieldController(ageField);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        String text = ageField.getText();
        if (!text.isEmpty()) {
            int age = Integer.parseInt(text);
            matchers.add(new EmployeeMatchers.MatchByAge().bind(age));
        }
        return matchers;
    }
}
