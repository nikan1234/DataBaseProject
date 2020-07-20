package com.railway.ui.window.entrance.pages.employees.hirePage.speciality;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.listControllers.teams.TeamListController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public abstract class TeamEmployeeRegisterController extends RegisterController {
    private String sql;

    @FXML
    private ComboBox<String> teamSelector;

    private TeamListController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new TeamListController(teamSelector);
    }

    @Override
    public int registerEmployee(Employee employee, String password) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int teamId = controller.getSelectedTeamId();
        if (teamId == TeamListController.NO_TEAM) {
            return Errors.NOT_ENOUGH_DATA;
        }

        try {
            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareCall(sql);
            statement.setInt(1, employee.getId());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getFirstName());
            statement.setString(4, employee.getSecondName());
            statement.setString(5, employee.getHireDate().format(formatter));
            statement.setString(6, employee.getBirthDate().format(formatter));
            statement.setString(7, employee.getGender());
            statement.setInt(8, employee.getChildCount());
            statement.setDouble(9, employee.getSalary());
            statement.setInt(10, teamId);
            statement.setString(11, password);
            statement.executeUpdate();
            return Errors.QUERY_SUCCESS;
        }
        catch (final SQLException e) {
            return e.getErrorCode();
        }
    }

    public int getTeamId() {
        return controller.getSelectedTeamId();
    }

    public void setSqlProcedure(String sql) {
        this.sql = sql;
    }
}
