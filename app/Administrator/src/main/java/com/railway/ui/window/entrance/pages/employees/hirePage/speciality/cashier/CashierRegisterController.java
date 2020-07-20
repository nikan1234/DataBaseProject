package com.railway.ui.window.entrance.pages.employees.hirePage.speciality.cashier;

import com.railway.ui.window.entrance.pages.employees.hirePage.speciality.TeamEmployeeRegisterController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class CashierRegisterController extends TeamEmployeeRegisterController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        super.setSqlProcedure("{call ADD_CASHIER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
    }
}
