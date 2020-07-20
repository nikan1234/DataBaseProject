package com.railway.ui.window.entrance.pages.employees.editPage;

import com.jfoenix.controls.JFXDatePicker;
import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.entity.Specialty;
import com.railway.ui.window.common.fieldContollers.DoubleFieldController;
import com.railway.ui.window.common.fieldContollers.NumberFieldController;
import com.railway.ui.window.common.formatters.DateFormatter;
import com.railway.ui.window.entrance.pages.employees.EmployeeFormController;
import com.railway.ui.window.entrance.pages.employees.editPage.speciality.EditController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class EditFormController extends WindowController implements Initializable {
    @FXML
    private AnchorPane additionalInfo;

    @FXML
    private TextField idField;

    @FXML
    private TextField lastName;

    @FXML
    private TextField firstName;

    @FXML
    private TextField secondName;

    @FXML
    private JFXDatePicker hireDate;

    @FXML
    private JFXDatePicker birthDate;

    @FXML
    private ComboBox<String> gender;

    @FXML
    private TextField salary;


    @FXML
    private TextField childCount;

    @FXML
    private Button editEmployeeButton;

    @FXML
    private TextField speciality;

    @FXML
    private Label resultLabel;

    private List<Pair<String, String>> availableSpecialities;
    private EditController currentController = null;
    private String currentSpeciality = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idField.setEditable(false);
        speciality.setEditable(false);

        new DoubleFieldController(salary);

        availableSpecialities = Arrays.asList(
                new Pair<>(Specialty.MANAGER,    "/entrance/pages/employees/editPage/speciality/manager.fxml"),
                new Pair<>(Specialty.DISPATCHER, "/entrance/pages/employees/editPage/speciality/dispatcher.fxml"),
                new Pair<>(Specialty.CASHIER,    "/entrance/pages/employees/editPage/speciality/cashier.fxml"),
                new Pair<>(Specialty.DRIVER,     "/entrance/pages/employees/editPage/speciality/driver.fxml"),
                new Pair<>(Specialty.REPAIRMAN,  "/entrance/pages/employees/editPage/speciality/repairman.fxml"));

        setupControllers();
        setupCalendars();
        setupEditProcedure();
    }

    public void setEmployee(Employee employee) {
        if (employee == null) {
            return;
        }
        idField.setText(Integer.toString(employee.getId()));
        lastName.setText(employee.getLastName());
        firstName.setText(employee.getFirstName());
        secondName.setText(employee.getSecondName());
        hireDate.setValue(employee.getHireDate());
        birthDate.setValue(employee.getBirthDate());
        speciality.setText(employee.getSpeciality());
        salary.setText(Double.toString(employee.getSalary()));
        childCount.setText(Integer.toString(employee.getChildCount()));
        gender.setValue(employee.getGender());

        setAdditionalInfo(employee);
    }

    private void setAdditionalInfo(Employee employee) {
        String fxml = null;
        for (Pair<String, String> p : availableSpecialities) {
            if (p.getKey().equals(employee.getSpeciality())) {
                fxml = p.getValue();
            }
        }
        if (fxml == null) {
            return;
        }

        currentController = getWindowManager().loadScene(fxml);
        currentController.setEmployeeId(employee.getId());

        AnchorPane pane = (AnchorPane) currentController.getView();

        pane.minWidthProperty().bind(additionalInfo.widthProperty());
        pane.maxWidthProperty().bind(additionalInfo.widthProperty());
        pane.minHeightProperty().bind(additionalInfo.heightProperty());
        pane.maxHeightProperty().bind(additionalInfo.heightProperty());

        pane.setTranslateX(0);
        pane.setTranslateY(0);

        additionalInfo.getChildren().clear();
        additionalInfo.getChildren().add(pane);
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
        getWindowManager().prevScene();
    }


    private void setupControllers() {
        new NumberFieldController(idField);
        new NumberFieldController(childCount);

        gender.getItems().add("M");
        gender.getItems().add("F");
        gender.setValue("M");
    }

    private void setupCalendars() {
        hireDate.setValue(LocalDate.now());
        hireDate.setConverter(new DateFormatter(hireDate));

        birthDate.setValue(LocalDate.now());
        birthDate.setConverter(new DateFormatter(hireDate));
    }

    private void setupEditProcedure() {
        editEmployeeButton.setOnMouseClicked(e -> {
            resultLabel.setText("");

            String idText = idField.getText();
            if (idText.isEmpty()) {
                resultLabel.setText("Specify employee id");
                return;
            }
            int employeeId = Integer.parseInt(idText);
            String employeeLastName = lastName.getText();
            if (employeeLastName.isEmpty()) {
                resultLabel.setText("Specify last name");
                return;
            }
            String employeeFirstName = firstName.getText();
            if (employeeFirstName.isEmpty()) {
                resultLabel.setText("Specify first name");
                return;
            }
            String employeeSecondName = secondName.getText();
            if (employeeSecondName.isEmpty()) {
                resultLabel.setText("Specify second name");
                return;
            }
            String numberText = childCount.getText();
            if (numberText.isEmpty()) {
                resultLabel.setText("Specify children count");
                return;
            }
            int childrenCount = Integer.parseInt(numberText);

            if (salary.getText().isEmpty()) {
                resultLabel.setText("Specify salary");
                return;
            }
            double employeeSalary = Double.parseDouble(salary.getText());


            if (currentController != null) {
                int result = currentController.editEmployee(
                        new Employee(
                                employeeId,
                                employeeLastName,
                                employeeFirstName,
                                employeeSecondName,
                                birthDate.getValue(),
                                hireDate.getValue(),
                                currentSpeciality,
                                employeeSalary,
                                gender.getValue(),
                                childrenCount)
                );
                resultLabel.setText("RESULT: " + Errors.toString(result));
            }
            else {
                resultLabel.setText("Choose speciality");
            }
        });
    }
}
