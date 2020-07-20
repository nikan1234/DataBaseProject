package com.railway.ui.window.entrance.pages.drivers.personalPage;

import com.jfoenix.controls.JFXDatePicker;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.entity.Team;
import com.railway.ui.window.common.formatters.DateFormatter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PersonalPageController extends WindowController implements Initializable {
    private int employeeId = 0;
    private PersonalPageModel model = new PersonalPageModel();

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

    @FXML
    private Label id;

    @FXML
    private Label lastName;

    @FXML
    private Label firstName;

    @FXML
    private Label secondName;

    @FXML
    private Label birthDate;

    @FXML
    private Label gender;

    @FXML
    private Label childCount;

    @FXML
    private Label speciality;

    @FXML
    private Label hireDate;

    @FXML
    private Label salary;

    @FXML
    private Label teamId;

    @FXML
    private Label teamType;

    @FXML
    private ListView<DateWrapper> medicalCard;

    @FXML
    private JFXDatePicker examinationDatePicker;

    @FXML
    private Button addExamination;

    @FXML
    private Button deleteExamination;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        examinationDatePicker.setConverter(new DateFormatter(examinationDatePicker));

        addExamination.setOnMouseClicked(e -> {
            LocalDate date = examinationDatePicker.getValue();
            model.addExamination(employeeId, date);

            updateView();
        });

        deleteExamination.setOnMouseClicked(e -> {
            LocalDate date = examinationDatePicker.getValue();
            model.deleteExamination(employeeId, date);

            updateView();
        });

        medicalCard.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                DateWrapper date = medicalCard.getSelectionModel()
                        .getSelectedItem();
                examinationDatePicker.setValue(date.getDate());
            }
        });
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }


    public void setEmployeeId(int id) {
        this.employeeId = id;
        updateView();
    }

    private void updateView() {
        examinationDatePicker.setValue(LocalDate.now());
        Employee employee = model.getEmployeeInfo(employeeId);
        if (employee == null) {
            return;
        }

        id.setText("Personal ID: " + employee.getId());
        lastName.setText(employee.getLastName());
        firstName.setText(employee.getFirstName());
        secondName.setText(employee.getSecondName());
        birthDate.setText(employee.getBirthDate().format(format));
        gender.setText(employee.getGender());
        childCount.setText(Integer.toString(employee.getChildCount()));
        speciality.setText(employee.getSpeciality());
        hireDate.setText(employee.getHireDate().format(format));
        salary.setText(Double.toString(employee.getSalary()));

        medicalCard.getItems().setAll(model.getMedicalExaminations(employeeId)
                .stream().map(DateWrapper::new).collect(Collectors.toList()));

        Team team = model.getEmployeeTeam(employeeId);
        teamId.setText(Integer.toString(team.getTeamId()));
        teamType.setText(team.getTeamType());
    }
}
