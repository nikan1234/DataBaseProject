package com.railway.ui.window.entrance.pages.employees.editPage.speciality;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.listControllers.teams.TeamListController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public abstract class TeamEmployeeEditController extends EditController {
    private String sql;

    @FXML
    private ComboBox<String> teamSelector;

    private TeamListController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new TeamListController(teamSelector);
    }

    @Override
    public int editEmployee(Employee employee) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int teamId = controller.getSelectedTeamId();
        if (teamId == TeamListController.NO_TEAM) {
            return Errors.NOT_ENOUGH_DATA;
        }

        try {
            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, employee.getLastName());
            statement.setString(2, employee.getFirstName());
            statement.setString(3, employee.getSecondName());
            statement.setString(4, employee.getHireDate().format(formatter));
            statement.setString(5, employee.getBirthDate().format(formatter));
            statement.setString(6, employee.getGender());
            statement.setInt(7, employee.getChildCount());
            statement.setDouble(8, employee.getSalary());
            statement.setInt(9, teamId);
            statement.setInt(10, employee.getId());
            statement.executeUpdate();
            return Errors.QUERY_SUCCESS;
        }
        catch (final SQLException e) {
            return e.getErrorCode();
        }
    }

    @Override
    public void setEmployeeId(int employeeId) {
        String teamSql = "SELECT team_id FROM EMPLOYEES WHERE employee_id = ?";

        try {
            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(teamSql);
            statement.setInt(1, employeeId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int teamId = result.getInt(1);
                if (!result.wasNull()) {
                    controller.setTeam(teamId);
                }
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getTeamId() {
        return controller.getSelectedTeamId();
    }

    public void setSqlProcedure(String sql) {
        this.sql = sql;
    }
}
