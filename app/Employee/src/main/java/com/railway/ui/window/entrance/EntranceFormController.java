package com.railway.ui.window.entrance;

import com.railway.ui.base.windowManager.ScrollableWindowManager;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.entrance.pages.employee.PersonalPageController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EntranceFormController extends WindowController implements Initializable {
    private int employeeId = 0;

    @FXML
    private ScrollPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setWindowManager(new ScrollableWindowManager(rootPane));
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
        showEmployeePage();
    }


    private void showEmployeePage() {
        final String fxml = "/entrance/pages/employees/employee.fxml";
        WindowManagerInterface manager = getWindowManager();
        PersonalPageController controller = manager.loadScene(fxml, this);
        controller.setEmployeeId(employeeId);
        manager.showScene(controller);
    }
}
