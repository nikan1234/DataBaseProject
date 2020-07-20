package com.railway.ui.window.entrance.pages.departments;

import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.buttonTableCell.ButtonCellGenerator;
import com.railway.ui.window.common.entity.Department;
import com.railway.ui.window.common.listControllers.managers.ManagerListController;
import com.railway.ui.window.entrance.pages.teams.TeamFormController;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DepartmentsFormController extends WindowController implements Initializable {
    private DepartmentsFormModel model = new DepartmentsFormModel();
    private ManagerListController managers;

    @FXML
    private TableView<DepartmentView> departmentsTable;

    @FXML
    private TextField departmentIdField;

    @FXML
    private TextField departmentNameField;

    @FXML
    private ComboBox<String> managersBox;

    @FXML
    private Button addDepartmentButton;

    @FXML
    private Button removeDepartmentButton;

    @FXML
    private Button editDepartmentButton;

    @FXML
    private Label resultLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        initControllers();
        updateDepartmentsTable();
    }


    void initControllers() {
        departmentIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            final int MAX_ID_LENGTH = 4;

            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            if (newValue.length() > MAX_ID_LENGTH) {
                newValue = newValue.substring(0, MAX_ID_LENGTH);
            }
            departmentIdField.setText(newValue);
        });

        managers = new ManagerListController(managersBox);

        initCreateController();
        initDeleteController();
        initUpdateController();
    }

    private void initTable() {
        final double BUTTON_COLUMN_RATIO = 0.05;
        final double ID_COLUMN_RATIO  = 0.15;
        final double NAME_COLUMN_RATIO = 0.3;
        final double MANAGER_COLUMN_RATIO = 0.5;

        TableColumn<DepartmentView, Void> button = new TableColumn<>();
        button.setCellFactory(getButtonGenerator());
        button.minWidthProperty().bind(
                departmentsTable.widthProperty()
                        .multiply(BUTTON_COLUMN_RATIO));

        TableColumn<DepartmentView, Integer> departmentId = new TableColumn<>("ID");
        departmentId.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        departmentId.minWidthProperty().bind(
                departmentsTable.widthProperty()
                        .multiply(ID_COLUMN_RATIO));

        TableColumn<DepartmentView, String> departmentName = new TableColumn<>("DEPARTMENT NAME");
        departmentName.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        departmentName.minWidthProperty().bind(
                departmentsTable.widthProperty()
                       .multiply(NAME_COLUMN_RATIO));

        TableColumn<DepartmentView, String> manager = new TableColumn<>("MANAGER");
        {
            final int columnsCount = 3;
            DoubleBinding property = departmentsTable.widthProperty()
                    .multiply(MANAGER_COLUMN_RATIO / columnsCount);

            TableColumn<DepartmentView, String> lastName = new TableColumn<>("LAST NAME");
            lastName.setCellValueFactory(new PropertyValueFactory<>("managerLastName"));
            lastName.minWidthProperty().bind(property);

            TableColumn<DepartmentView, String> firstName = new TableColumn<>("FIRST NAME");
            firstName.setCellValueFactory(new PropertyValueFactory<>("managerFirstName"));
            firstName.prefWidthProperty().bind(property);

            TableColumn<DepartmentView, String> secondName = new TableColumn<>("SECOND NAME");
            secondName.setCellValueFactory(new PropertyValueFactory<>("managerSecondName"));
            secondName.prefWidthProperty().bind(property);

            manager.getColumns().add(lastName);
            manager.getColumns().add(firstName);
            manager.getColumns().add(secondName);
        }
        departmentsTable.getColumns().add(button);
        departmentsTable.getColumns().add(departmentId);
        departmentsTable.getColumns().add(departmentName);
        departmentsTable.getColumns().add(manager);


        departmentsTable.setOnMousePressed(event -> {
            DepartmentView view = departmentsTable.getSelectionModel().getSelectedItem();

            if (view == null) {
                return;
            }
            departmentIdField.setText(Integer.toString(view.getDepartmentId()));
            departmentNameField.setText(view.getDepartmentName());
            managers.setItem(view.getDepartment().getManager());
        });
    }

    private Department getDepartment() {
        String textId = departmentIdField.getText();
        if (textId.isEmpty()) {
            resultLabel.setText("SPECIFY DEPARTMENT ID");
            return null;
        }
        int departmentId = Integer.parseInt(textId);

        String departmentName = departmentNameField.getText();
        if (departmentName.isEmpty()) {
            resultLabel.setText("SPECIFY DEPARTMENT NAME");
            return null;
        }
        return new Department(
                departmentId, departmentName, managers.getSelectedManager());
    }

    private void initCreateController() {
        addDepartmentButton.setOnMouseClicked(e -> {
            Department department = getDepartment();
            if (department == null)
                return;

            final int result = model.addDepartment(department);
            if (result == Errors.QUERY_SUCCESS) {
                resultLabel.setText("DEPARTMENT ADDED SUCCESSFULLY");
            } else {
                resultLabel.setText(Errors.toString(result));
            }
            updateDepartmentsTable();
        });
    }

    private void initDeleteController() {
        removeDepartmentButton.setOnMouseClicked(e -> {
            String textId = departmentIdField.getText();
            if (textId.isEmpty()) {
                resultLabel.setText("SPECIFY DEPARTMENT ID");
                return;
            }
            int departmentId = Integer.parseInt(textId);
            final int result = model.deleteDepartment(departmentId);
            if (result == Errors.QUERY_SUCCESS) {
                resultLabel.setText("DEPARTMENT ADDED SUCCESSFULLY");
            } else {
                resultLabel.setText(Errors.toString(result));
            }
            updateDepartmentsTable();
        });
    }

    private void initUpdateController() {
        editDepartmentButton.setOnMouseClicked(e -> {
            Department department = getDepartment();
            if (department == null)
                return;

            final int result = model.editDepartment(department);
            if (result == Errors.QUERY_SUCCESS) {
                resultLabel.setText("DEPARTMENT ADDED SUCCESSFULLY");
            } else {
                resultLabel.setText(Errors.toString(result));
            }
            updateDepartmentsTable();
        });
    }


    private void showDepartmentDetails(Department department) {
        final String fxml = "/entrance/pages/teams/teams.fxml";
        TeamFormController controller = getWindowManager().loadScene(fxml, this);
        controller.setDepartment(department);
        getWindowManager().showScene(controller);
    }

    private void updateDepartmentsTable() {
        departmentIdField.setText("");
        departmentNameField.setText("");
        managers.setItem(0);

        List<Department> departments = model.getDepartments();
        List<DepartmentView> views = departments
                .stream()
                .map(DepartmentView::new)
                .collect(Collectors.toList());

        departmentsTable.setItems(FXCollections.observableList(views));
    }

    private Callback<TableColumn<DepartmentView, Void>, TableCell<DepartmentView, Void>>
    getButtonGenerator() {
        return new ButtonCellGenerator<DepartmentView>("SELECT")
                .getButtonCallback(departmentView -> showDepartmentDetails(departmentView.getDepartment()));
    }
}
