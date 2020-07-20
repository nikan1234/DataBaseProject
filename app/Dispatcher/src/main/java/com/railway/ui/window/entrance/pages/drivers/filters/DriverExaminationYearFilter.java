package com.railway.ui.window.entrance.pages.drivers.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.examinations.ExaminationMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class DriverExaminationYearFilter extends FilterInterface implements Initializable {

    @FXML
    private TextField yearField;

    @FXML
    private CheckBox hadExamination;


    private static final int YEAR_LENGTH = 4;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hadExamination.setSelected(true);

        yearField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                yearField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (yearField.getText().length() > YEAR_LENGTH) {
                yearField.setText(yearField.getText().substring(0, YEAR_LENGTH));
            }
        });
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();

        String year = yearField.getText();
        if (year.length() == YEAR_LENGTH) {
            matchers.add(new ExaminationMatchers
                    .MatchByExaminationYear()
                    .comparator((hadExamination.isSelected()) ? Matcher.EQUAL : Matcher.NOT_EQUAL)
                    .bind(year));
        }
        return matchers;
    }
}
