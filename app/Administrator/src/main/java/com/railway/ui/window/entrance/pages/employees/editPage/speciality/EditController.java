package com.railway.ui.window.entrance.pages.employees.editPage.speciality;

import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Employee;
import javafx.fxml.Initializable;

public abstract class EditController extends WindowController implements Initializable {
    abstract public int editEmployee(Employee employee);
    abstract public void setEmployeeId(int employeeId);
}
