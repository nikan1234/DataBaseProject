package com.railway.ui.window.entrance.pages.employees;

import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManager;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.entrance.pages.employees.dismissPage.DismissFormController;
import com.railway.ui.window.entrance.pages.employees.hirePage.HireFormController;
import com.railway.ui.window.entrance.pages.employees.personalPage.PersonalPageController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeFormCommon extends WindowController implements Initializable {
    @FXML
    private TableView<Employee> employeesTable;

    @FXML
    private Label employeeCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableView();
    }

    @FXML
    void onHireEmployeeButtonClicked() {
        final String fxml = "/entrance/pages/employees/hirePage/hire.fxml";
        HireFormController controller = getWindowManager().loadScene(fxml, this);
        getWindowManager().showScene(controller);
    }

    @FXML
    void onDismissEmployeeButtonClicked() {
        final String fxml = "/entrance/pages/employees/dismissPage/dismiss.fxml";
        DismissFormController controller = getWindowManager().loadScene(fxml, this);
        getWindowManager().showScene(controller);
    }

    private void setupTableView() {
        final double ID_COLUMN_RATIO = 0.07;
        final double LAST_NAME_COLUMN_RATIO = 0.25;
        final double FIRST_NAME_COLUMN_RATIO = 0.25;
        final double SECOND_NAME_COLUMN_RATIO = 0.25;
        final double SPECIALITY_COLUMN_RATIO = 0.18;

        employeesTable.getColumns().add(getColumn("ID", "id", ID_COLUMN_RATIO));
        employeesTable.getColumns().add(getColumn("LAST NAME", "lastName", LAST_NAME_COLUMN_RATIO));
        employeesTable.getColumns().add(getColumn("FIRST NAME", "firstName", FIRST_NAME_COLUMN_RATIO));
        employeesTable.getColumns().add(getColumn("SECOND NAME", "secondName", SECOND_NAME_COLUMN_RATIO));
        employeesTable.getColumns().add(getColumn("SPECIALITY", "speciality", SPECIALITY_COLUMN_RATIO));

        employeesTable.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Employee rowData = row.getItem();

                    /* Employee personal page */
                    showEmployeePersonalPage(rowData);
                }
            });
            return row;
        });
    }

    private TableColumn<Employee, String> getColumn(String name, String arg, double ratio) {
        TableColumn<Employee, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(arg));
        column.prefWidthProperty().bind(employeesTable.widthProperty().multiply(ratio));
        return column;
    }

    protected void updateTable(List<Employee> employees, int count) {
        employeesTable.setItems(FXCollections.observableList(employees));
        employeeCount.setText("Count: " + count);
    }

    private void showEmployeePersonalPage(Employee employee) {
        final String fxml = "/entrance/pages/employees/personalPage/personalPage.fxml";

        WindowManagerInterface manager = getWindowManager();
        PersonalPageController controller = manager.loadScene(fxml ,this);
        controller.setEmployeeId(employee.getId());

        manager.showScene(controller);
    }
}
