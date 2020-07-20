package com.railway.ui.window.entrance.pages.employees.dismissPage;

import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.fieldContollers.NumberFieldController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DismissFormController extends WindowController implements Initializable {
    DismissFormModel model = new DismissFormModel();

    @FXML
    private TextField employeeIdField;

    @FXML
    private ListView<String> employeesList;

    @FXML
    private Label resultLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new NumberFieldController(employeeIdField);

        employeeIdField.setOnKeyTyped(u -> updateEmployeeList());
    }

    private void updateEmployeeList() {
        String text = employeeIdField.getText();
        if (text.isEmpty()) {
            employeesList.getItems().clear();
            return;
        }
        employeesList.getItems().setAll(model.selectEmployees(text).stream()
                .map(e -> {
                    final String sep = "\t";
                    return e.getId()
                            + sep + e.getLastName()
                            + sep + e.getFirstName()
                            + sep + e.getSecondName();
                }).collect(Collectors.toList())
        );
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }

    @FXML
    void dismissEmployee() {
        String text = employeeIdField.getText();
        if (text.isEmpty()) {
            return;
        }
        int employeeId = Integer.parseInt(text);
        int result = model.dismissEmployee(employeeId);
        resultLabel.setText("RESULT: " + Errors.toString(result));

        updateEmployeeList();
    }
}
