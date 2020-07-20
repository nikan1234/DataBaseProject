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

public class ExperienceFilter extends FilterInterface implements Initializable {

    @FXML
    private TextField minExperience;

    @FXML
    private TextField maxExperience;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new NumberFieldController(minExperience);
        new NumberFieldController(maxExperience);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();

        String min = minExperience.getText();
        String max = maxExperience.getText();
        if (!min.isEmpty()) {
            matchers.add(new EmployeeMatchers
                    .MatchByExperience()
                    .bind(Integer.parseInt(min))
                    .comparator(Matcher.NOT_LESS));
        }
        if (!max.isEmpty()) {
            matchers.add(new EmployeeMatchers
                    .MatchByExperience()
                    .bind(Integer.parseInt(max))
                    .comparator(Matcher.NOT_GREATER));
        }
        return matchers;
    }
}
