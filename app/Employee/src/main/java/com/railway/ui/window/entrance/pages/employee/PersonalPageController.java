package com.railway.ui.window.entrance.pages.employee;

import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.entity.Specialty;
import com.railway.ui.window.entrance.pages.employees.personalPage.speciality.SpecialityController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PersonalPageController extends WindowController implements Initializable {
    private int employeeId = 0;
    private PersonalPageModel model = new PersonalPageModel();
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-YYYY");
    private HashMap<String, String> specialityToFxml = new HashMap<>();

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
    private TextField passwordField;

    @FXML
    private AnchorPane additionalInfo;


    @FXML
    private Label resultLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        specialityToFxml.put(Specialty.MANAGER,    "/entrance/pages/employees/speciality/manager.fxml");
        specialityToFxml.put(Specialty.DISPATCHER, "/entrance/pages/employees/speciality/dispatcher.fxml");
        specialityToFxml.put(Specialty.CASHIER,    "/entrance/pages/employees/speciality/cashier.fxml");
        specialityToFxml.put(Specialty.DRIVER,     "/entrance/pages/employees/speciality/driver.fxml");
        specialityToFxml.put(Specialty.REPAIRMAN,  "/entrance/pages/employees/speciality/repairman.fxml");
    }

    @FXML
    void changePassword() {
        String password = passwordField.getText();
        if (password.isEmpty()) {
            resultLabel.setText("Password can't be empty");
            return;
        }
        int result = model.changePassword(employeeId, password);
        resultLabel.setText("RESULT: " + Errors.toString(result));
    }


    public void setEmployeeId(int id) {
        this.employeeId = id;
        updateView();
    }

    private void updateView() {
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

        setAdditionalInfo(employee);
    }

    private void setAdditionalInfo(Employee employee) {
        String fxml = specialityToFxml.get(employee.getSpeciality());
        if (fxml == null) {
            return;
        }
        SpecialityController controller = getWindowManager().loadScene(fxml);
        controller.setEmployeeId(employeeId);

        AnchorPane pane = (AnchorPane) controller.getView();

        pane.minWidthProperty().bind(additionalInfo.widthProperty());
        pane.maxWidthProperty().bind(additionalInfo.widthProperty());
        pane.minHeightProperty().bind(additionalInfo.heightProperty());
        pane.maxHeightProperty().bind(additionalInfo.heightProperty());

        pane.setTranslateX(0);
        pane.setTranslateY(0);

        additionalInfo.getChildren().clear();
        additionalInfo.getChildren().add(pane);
    }
}
