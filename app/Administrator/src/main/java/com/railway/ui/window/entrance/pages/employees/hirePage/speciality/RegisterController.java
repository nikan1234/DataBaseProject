package com.railway.ui.window.entrance.pages.employees.hirePage.speciality;

import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Employee;
import javafx.fxml.Initializable;

public abstract class RegisterController extends WindowController implements Initializable {
    abstract public int registerEmployee(Employee employee, String password);
}
