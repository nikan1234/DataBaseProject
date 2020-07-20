package com.railway.ui.window.entrance.pages.employees.hirePage.speciality.dispatcher;

import com.railway.ui.window.entrance.pages.employees.hirePage.speciality.TeamEmployeeRegisterController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DispatcherRegisterController extends TeamEmployeeRegisterController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        super.setSqlProcedure("{call ADD_DISPATCHER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
    }
}
