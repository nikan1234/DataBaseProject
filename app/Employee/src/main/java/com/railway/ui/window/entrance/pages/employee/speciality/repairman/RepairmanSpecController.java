package com.railway.ui.window.entrance.pages.employee.speciality.repairman;

import com.railway.ui.window.entrance.pages.employees.personalPage.speciality.TeamInfoController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class RepairmanSpecController extends TeamInfoController {
    private RepairmanSpecModel model = new RepairmanSpecModel();

    private int employeeId;

    @FXML
    private Label rankLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }


    @Override
    public void setEmployeeId(int employeeId) {
        super.setEmployeeId(employeeId);
        this.employeeId = employeeId;

        updateView();
    }

    protected void updateView() {
        rankLabel.setText(Integer.toString(model.getRepairmanRank(employeeId)));
        super.updateView();
    }
}
