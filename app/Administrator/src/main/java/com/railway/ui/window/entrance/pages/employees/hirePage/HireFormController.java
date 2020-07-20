package com.railway.ui.window.entrance.pages.employees.hirePage;

import com.jfoenix.controls.JFXDatePicker;
import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.entity.Specialty;
import com.railway.ui.window.common.fieldContollers.DoubleFieldController;
import com.railway.ui.window.common.fieldContollers.NumberFieldController;
import com.railway.ui.window.common.formatters.DateFormatter;
import com.railway.ui.window.entrance.pages.employees.hirePage.speciality.RegisterController;
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
import java.util.stream.Collectors;

public class HireFormController extends WindowController implements Initializable {
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
    private TextField password;

    @FXML
    private TextField childCount;

    @FXML
    private Button hireEmployeeButton;

    @FXML
    private ComboBox<String> speciality;

    @FXML
    private Label resultLabel;

    private List<Pair<String, String>> availableSpecialities;
    private RegisterController currentController;
    private String currentSpeciality = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new DoubleFieldController(salary);

        availableSpecialities = Arrays.asList(
                new Pair<>(Specialty.MANAGER,    "/entrance/pages/employees/hirePage/speciality/manager.fxml"),
                new Pair<>(Specialty.DISPATCHER, "/entrance/pages/employees/hirePage/speciality/dispatcher.fxml"),
                new Pair<>(Specialty.CASHIER,    "/entrance/pages/employees/hirePage/speciality/cashier.fxml"),
                new Pair<>(Specialty.DRIVER,     "/entrance/pages/employees/hirePage/speciality/driver.fxml"),
                new Pair<>(Specialty.REPAIRMAN,  "/entrance/pages/employees/hirePage/speciality/repairman.fxml"));

        setupControllers();
        setupSpecialities();
        setupCalendars();
        setupHireProcedure();
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }

    @FXML
    void onSpecialitySelected() {
        int idx = speciality.getSelectionModel().getSelectedIndex();
        if (idx <= 0) {
            currentController = null;
            currentSpeciality = "";
            return;
        }
        currentController = getWindowManager().loadScene(availableSpecialities.get(idx - 1).getValue());
        currentSpeciality = availableSpecialities.get(idx - 1).getKey();

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

    private void setupControllers() {
        new NumberFieldController(idField);
        new NumberFieldController(childCount);

        gender.getItems().add("M");
        gender.getItems().add("F");
        gender.setValue("M");
    }

    private void setupSpecialities() {
        final String header = "Select speciality";

        speciality.getItems().add(header);
        speciality.getItems().addAll(availableSpecialities
                .stream()
                .map(Pair::getKey)
                .collect(Collectors.toList()));
        speciality.setValue(header);
    }

    private void setupCalendars() {
        hireDate.setValue(LocalDate.now());
        hireDate.setConverter(new DateFormatter(hireDate));

        birthDate.setValue(LocalDate.now());
        birthDate.setConverter(new DateFormatter(hireDate));
    }

    private void setupHireProcedure() {
        hireEmployeeButton.setOnMouseClicked(e -> {
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

            String employeePassword = password.getText();
            if (employeePassword.isEmpty()) {
                resultLabel.setText("Specify password");
                return;
            }
            if (currentController != null) {
                int result = currentController.registerEmployee(
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
                                childrenCount),
                        employeePassword
                );
                resultLabel.setText("RESULT: " + Errors.toString(result));
            }
            else {
                resultLabel.setText("Choose speciality");
            }
        });
    }
}
