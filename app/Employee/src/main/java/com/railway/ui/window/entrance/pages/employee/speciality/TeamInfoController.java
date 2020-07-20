package com.railway.ui.window.entrance.pages.employees.personalPage.speciality;

import com.railway.ui.window.common.entity.Team;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

abstract public class TeamInfoController extends SpecialityController implements Initializable {
    private TeamInfoModel model = new TeamInfoModel();
    private int employeeId;

    @FXML
    private Label teamId;

    @FXML
    private Label teamType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    protected void updateView() {
        model.setEmployeeId(employeeId);
        Team team = model.getEmployeeTeam();
        if (team != null) {
            teamId.setText(Integer.toString(team.getTeamId()));
            teamType.setText(team.getTeamType());
        }
    }

    @Override
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
