package com.railway.ui.window.entrance.pages.employees.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.departments.DepartmentMatchers;
import com.railway.ui.window.common.listControllers.departments.DepartmentListController;
import com.railway.ui.window.common.entity.Department;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.*;

public class DepartmentFilter extends FilterInterface implements Initializable {
    private DepartmentListController controller;


    @FXML
    private ComboBox<String> departmentId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new DepartmentListController(departmentId);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        Department department = controller.getSelectedDepartment();
        if (department == null)
             return matchers;

        matchers.add(new DepartmentMatchers.MatchByDepartmentId()
                .bind(department.getDepartmentId()));
        return matchers;
    }
}
