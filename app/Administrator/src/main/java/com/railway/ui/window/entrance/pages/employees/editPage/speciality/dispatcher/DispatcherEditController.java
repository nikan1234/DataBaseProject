package com.railway.ui.window.entrance.pages.employees.editPage.speciality.dispatcher;

import com.railway.ui.window.entrance.pages.employees.editPage.speciality.TeamEmployeeEditController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DispatcherEditController extends TeamEmployeeEditController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        super.setSqlProcedure("UPDATE DISPATCHER SET " +
                "last_name = ?, first_name = ?, second_name = ?, " +
                "hire_date =  TO_DATE(?, 'YYYY-MM-DD'), " +
                "birth_date = TO_DATE(?, 'YYYY-MM-DD'), " +
                "gender = ?, " +
                "child_count = ?, salary = ?, " +
                "team_id = ? " +
                "WHERE employee_id = ?");
    }
}
