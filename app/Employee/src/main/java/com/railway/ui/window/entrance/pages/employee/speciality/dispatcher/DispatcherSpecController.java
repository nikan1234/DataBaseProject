package com.railway.ui.window.entrance.pages.employee.speciality.dispatcher;

import com.railway.ui.window.entrance.pages.employees.personalPage.speciality.TeamInfoController;

public class DispatcherSpecController extends TeamInfoController {

    @Override
    public void setEmployeeId(int employeeId) {
        super.setEmployeeId(employeeId);
        super.updateView();
    }

    @Override
    protected void updateView() {
        super.updateView();
    }

}
