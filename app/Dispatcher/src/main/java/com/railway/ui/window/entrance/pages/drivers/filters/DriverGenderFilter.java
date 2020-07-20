package com.railway.ui.window.entrance.pages.drivers.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.employee.EmployeeMatchers;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

public class DriverGenderFilter extends FilterInterface implements Initializable {

    @FXML
    private ComboBox<String> genderBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderBox.setItems(FXCollections.observableArrayList(Employee.genders));
        genderBox.setValue(Employee.genders[0]);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        String gender = genderBox.getValue();
        return Arrays.asList(
                new EmployeeMatchers.MatchByGender().bind(gender));
    }
}
