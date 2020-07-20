package com.railway.ui.window.common.listControllers.departments;


import com.railway.ui.window.common.entity.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentListController {
    private ComboBox<String> box;
    private DepartmentListModel model = new DepartmentListModel();
    private List<Department> departments;


    public DepartmentListController(ComboBox<String> box) {
        this.box = box;
        this.departments = model.getDepartmentList();

        ObservableList<String> items = FXCollections.observableList(
                departments.stream().map(e -> {
                    final char sep = ' ';
                    return Integer.toString(e.getDepartmentId())
                            + sep + e.getDepartmentName();
                }).collect(Collectors.toList()));
        items.add(0, "Any department");
        box.setItems(items);
        box.setValue(items.get(0));
    }

    public Department getSelectedDepartment() {
        int idx = box.getSelectionModel().getSelectedIndex();
        if (idx == 0)
            return null;
        return departments.get(idx - 1);
    }
}
