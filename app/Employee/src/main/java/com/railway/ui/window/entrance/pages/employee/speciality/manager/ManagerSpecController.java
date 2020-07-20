package com.railway.ui.window.entrance.pages.employee.speciality.manager;

import com.railway.ui.window.common.entity.Department;
import com.railway.ui.window.entrance.pages.employees.personalPage.speciality.SpecialityController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ManagerSpecController extends SpecialityController implements Initializable {
    private ManagerSpecModel model = new ManagerSpecModel();
    private int employeeId;

    @FXML
    private ListView<String> departmentList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final String fontFamily = "Courier New";
        departmentList.setCellFactory(cell -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setFont(Font.font(fontFamily, 16));
                }
            }
        });
    }

    @Override
    public void setEmployeeId(int id) {
        employeeId = id;
        updateView();
    }

    private void updateView() {
        List<Department> departments = model.getDepartmentList(employeeId);
        departmentList.setItems(FXCollections.observableList(
                departments.stream().map(d -> {
                    final String sep = " ";
                    return d.getDepartmentId() + sep + d.getDepartmentName();
                }).collect(Collectors.toList())
        ));
    }
}
