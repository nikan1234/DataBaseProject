package com.railway.ui.window.entrance;

import com.railway.ui.base.windowManager.ScrollableWindowManager;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.entrance.pages.departments.DepartmentsFormController;
import com.railway.ui.window.entrance.pages.employees.EmployeeFormController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class EntranceFormController extends WindowController implements Initializable {

    @FXML
    private ScrollPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setWindowManager(new ScrollableWindowManager(rootPane));
        showDepartments();
    }

    @FXML
    void onHomeButtonClicked(MouseEvent event) {
        showDepartments();
    }

    private void showDepartments() {
        final String fxml = "/entrance/pages/departments/departments.fxml";
        DepartmentsFormController controller = show(fxml);
    }

    @FXML
    void onEmployeeButtonClicked(MouseEvent event) {
        final String fxml = "/entrance/pages/employees/employees.fxml";
        EmployeeFormController controller = show(fxml);
        controller.setup();
    }


    private <C extends WindowController> C show(String fxml) {
        WindowManagerInterface manager = getWindowManager();
        C controller = manager.loadScene(fxml, this);
        manager.resetScenes(controller);
        return controller;
    }
}
