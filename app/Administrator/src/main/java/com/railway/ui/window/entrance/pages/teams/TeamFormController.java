package com.railway.ui.window.entrance.pages.teams;

import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.entity.Manager;
import com.railway.ui.window.common.entity.Team;
import com.railway.ui.window.common.entity.Department;
import com.railway.ui.window.common.fieldContollers.NumberFieldController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TeamFormController extends WindowController implements Initializable {
    public static class EmployeeView {
        private String name;
        private String speciality;

        public EmployeeView(Employee employee) {
            name = String.format("%s %s %s", employee.getLastName(),
                    employee.getFirstName(), employee.getSecondName());
            speciality = employee.getSpeciality();
        }

        public String getName() {
            return name;
        }

        public String getSpeciality() {
            return speciality;
        }
    }

    private TeamFormModel model;
    private Department department;

    @FXML
    private Label logoLabel;

    @FXML
    private TableView<Team> teamsTable;

    @FXML
    private TableView<EmployeeView> teamEmployees;

    @FXML
    private Label managerId;

    @FXML
    private Label managerLastName;

    @FXML
    private Label managerFirstName;

    @FXML
    private Label managerSecondName;

    @FXML
    private Label managerAge;

    @FXML
    private Label managerChildCount;

    @FXML
    private Label managerHireDate;

    @FXML
    private Label managerSalary;

    @FXML
    private TextField teamIdField;

    @FXML
    private TextField teamTypeField;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label resultLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        initControllers();
    }

    public void setDepartment(Department department) {
        this.model = new TeamFormModel(department);
        this.department = department;
        updateView();
    }

    private void updateView() {
        final String datePattern = "dd-MM-yyyy";
        DateTimeFormatter format = DateTimeFormatter.ofPattern(datePattern);

        if (department == null)
            return;

        logoLabel.setText("Department "
                + department.getDepartmentId() + " "
                + department.getDepartmentName());

        Manager manager = department.getManager();
        if (manager == null) {
            managerId.setText("Manager not found");
            return;
        }

        managerId.setText("ID: " + manager.getId());
        managerLastName.setText(manager.getLastName());
        managerFirstName.setText(manager.getFirstName());
        managerSecondName.setText(manager.getSecondName());

        LocalDate now = LocalDate.now();
        long age = manager.getBirthDate().until(now, ChronoUnit.YEARS);

        managerAge.setText("Age: " + age);
        managerHireDate.setText("Hire Date: " + manager.getHireDate().format(format));
        managerChildCount.setText("Child count: " + manager.getChildCount());
        managerSalary.setText("Salary: " + manager.getSalary());


        teamsTable.setItems(FXCollections.observableList(model.getTeamList()));

        teamIdField.setText("");
        teamTypeField.setText("");

        new NumberFieldController(teamIdField);
    }

    private void initControllers() {
        addButton.setOnMouseClicked(e -> {
            String type = teamTypeField.getText();
            if (type.isEmpty()) {
                resultLabel.setText("SPECIFY TEAM TYPE");
                return;
            }
            int result = model.addTeam(new Team(0, type, department));
            if (result == Errors.QUERY_SUCCESS) {
                resultLabel.setText("TEAM ADDED SUCCESSFULLY");
            } else {
                resultLabel.setText("UNKNOWN ERROR " + result);
            }
            updateView();
        });


        deleteButton.setOnMouseClicked(e -> {
            String text = teamIdField.getText();
            if (text.isEmpty()) {
                resultLabel.setText("SPECIFY TEAM ID");
                return;
            }
            int id = Integer.parseInt(text);
            int result = model.deleteTeam(id);
            switch (result) {
                case Errors.QUERY_SUCCESS:
                    resultLabel.setText("TEAM DELETED SUCCESSFULLY");
                    break;
                case Errors.NO_DATA_FOUND:
                    resultLabel.setText("TEAM NOT FOUND");
                    break;
                default:
                    resultLabel.setText("UNKNOWN ERROR " + result);
            }
            updateView();
        });
    }

    private void initTable() {
        final double TEAM_ID_COLUMN_RATIO     = 0.2;
        final double TEAM_TYPE_COLUMN_RATIO   = 0.8;

        TableColumn<Team, Integer> teamId = new TableColumn<>("TEAM ID");
        teamId.setCellValueFactory(new PropertyValueFactory<>("teamId"));
        teamId.prefWidthProperty().bind(teamsTable.widthProperty().multiply(TEAM_ID_COLUMN_RATIO));

        TableColumn<Team, String> teamType = new TableColumn<>("TYPE");
        teamType.setCellValueFactory(new PropertyValueFactory<>("teamType"));
        teamType.prefWidthProperty().bind(teamsTable.widthProperty().multiply(TEAM_TYPE_COLUMN_RATIO));

        teamsTable.getColumns().add(teamId);
        teamsTable.getColumns().add(teamType);



        final double EMPLOYEE_NAME_RATIO = 0.7;
        final double EMPLOYEE_SPEC_RATIO = 0.3;

        TableColumn<EmployeeView, String> name = new TableColumn<>("NAME");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.prefWidthProperty().bind(teamEmployees.widthProperty().multiply(EMPLOYEE_NAME_RATIO));

        TableColumn<EmployeeView, String> spec = new TableColumn<>("SPECIALITY");
        spec.setCellValueFactory(new PropertyValueFactory<>("speciality"));
        spec.prefWidthProperty().bind(teamEmployees.widthProperty().multiply(EMPLOYEE_SPEC_RATIO));

        teamEmployees.getColumns().add(name);
        teamEmployees.getColumns().add(spec);


        teamsTable.setOnMousePressed(e -> {
            Team team = teamsTable.getSelectionModel().getSelectedItem();
            if (team == null) {
                return;
            }
            teamIdField.setText(Integer.toString(team.getTeamId()));
            teamTypeField.setText(team.getTeamType());

            teamEmployees.getItems().setAll(model.getEmployees(team.getTeamId())
                    .stream().map(EmployeeView::new).collect(Collectors.toList()));
        });
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }
}
