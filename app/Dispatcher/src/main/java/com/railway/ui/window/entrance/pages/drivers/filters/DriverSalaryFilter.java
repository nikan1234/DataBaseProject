package com.railway.ui.window.entrance.pages.drivers.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.employee.EmployeeMatchers;
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

public class DriverSalaryFilter extends FilterInterface implements Initializable {
    @FXML
    private TextField minSalary;

    @FXML
    private TextField maxSalary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new IntegerFieldController(minSalary);
        new IntegerFieldController(maxSalary);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();

        String min = minSalary.getText();
        String max = maxSalary.getText();
        if (!min.isEmpty()) {
            matchers.add(new EmployeeMatchers
                    .MatchBySalary()
                    .bind(Integer.parseInt(min))
                    .comparator(Matcher.NOT_LESS));
        }
        if (!max.isEmpty()) {
            matchers.add(new EmployeeMatchers
                    .MatchBySalary()
                    .bind(Integer.parseInt(max))
                    .comparator(Matcher.NOT_GREATER));
        }
        return matchers;
    }
}
