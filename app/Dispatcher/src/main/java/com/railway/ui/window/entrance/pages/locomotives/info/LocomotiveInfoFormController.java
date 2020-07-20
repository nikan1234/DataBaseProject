package com.railway.ui.window.entrance.pages.locomotives.info;

import com.jfoenix.controls.JFXDatePicker;
import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.entity.Locomotive;
import com.railway.ui.window.common.entity.Team;
import com.railway.ui.window.common.formatters.DateFormatter;
import com.railway.ui.window.common.listControllers.teams.TeamListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LocomotiveInfoFormController extends WindowController implements Initializable {
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-YYYY");

    private static class DateWrapper {
        private LocalDate date;

        public DateWrapper(LocalDate date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return date.format(format);
        }

        public LocalDate getDate() {
            return date;
        }
    }

    private LocomotiveInfoFormModel model = new LocomotiveInfoFormModel();
    private int locomotiveId = 0;
    private TeamListController teamListController;

    @FXML
    private Label locomotiveIdLabel;

    @FXML
    private Label locomotiveNameLabel;

    @FXML
    private Label entryDateLabel;

    @FXML
    private ComboBox<String> teamBox;

    @FXML
    private Button changeTeam;

    @FXML
    private ListView<Employee> employeeList;

    @FXML
    private Button addRepair;

    @FXML
    private Button removeRepair;

    @FXML
    private ListView<DateWrapper> repairsList;

    @FXML
    private JFXDatePicker repairDate;

    @FXML
    private Label resultLabel;

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teamListController = new TeamListController(teamBox);
        repairDate.setValue(LocalDate.now());
        repairDate.setConverter(new DateFormatter(repairDate));

        changeTeam.setOnMouseClicked(u -> {
            resultLabel.setText("");

            Team team = teamListController.getSelectedTeam();
            if (team == null) {
                resultLabel.setText("Specify team");
                return;
            }
            int result = model.changeTeam(locomotiveId, team.getTeamId());
            resultLabel.setText("RESULT: " + Errors.toString(result));
        });

        teamBox.getSelectionModel().selectedItemProperty().addListener(
                (options, oldValue, newValue) -> updateEmployeeList());

        addRepair.setOnMouseClicked(e -> {
            LocalDate date = repairDate.getValue();
            int result = model.addRepair(locomotiveId, date);
            resultLabel.setText("RESULT: " + Errors.toString(result));
            updateView();
        });

        removeRepair.setOnMouseClicked(e -> {
            LocalDate date = repairDate.getValue();

            int result = model.removeRepair(locomotiveId, date);
            resultLabel.setText("RESULT: " + Errors.toString(result));

            updateView();
        });

        repairsList.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                DateWrapper date = repairsList.getSelectionModel()
                        .getSelectedItem();
                repairDate.setValue(date.getDate());
            }
        });
    }

    public void setLocomotiveId(int locomotiveId) {
        this.locomotiveId = locomotiveId;
        updateView();
    }

    private void updateView() {

        Locomotive locomotive = model.getLocomotive(locomotiveId);

        locomotiveIdLabel.setText(Integer.toString(locomotive.getLocomotiveId()));
        locomotiveNameLabel.setText(locomotive.getLocomotiveName());
        entryDateLabel.setText(locomotive.getEntryDate().format(DateFormatter.FORMATTER));
        teamListController.selectTeam(locomotive.getServiceTeam());

        repairsList.getItems().setAll(
                model.getRepairs(locomotiveId).stream()
                        .map(DateWrapper::new)
                        .collect(Collectors.toList())
        );
        updateEmployeeList();
    }

    private void updateEmployeeList() {
        Team team = teamListController.getSelectedTeam();
        if (team == null) {
            return;
        }
        List<Employee> employee = model.getByTeam(team.getTeamId());
        employeeList.getItems().setAll(employee);
    }
}
