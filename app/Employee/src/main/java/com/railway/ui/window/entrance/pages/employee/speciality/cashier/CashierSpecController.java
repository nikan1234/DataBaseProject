package com.railway.ui.window.entrance.pages.employee.speciality.cashier;


import com.railway.ui.window.entrance.pages.employees.personalPage.speciality.TeamInfoController;

public class CashierSpecController extends TeamInfoController {

    @Override
    public void setEmployeeId(int employeeId) {
        super.setEmployeeId(employeeId);
        super.updateView();
    }
}
