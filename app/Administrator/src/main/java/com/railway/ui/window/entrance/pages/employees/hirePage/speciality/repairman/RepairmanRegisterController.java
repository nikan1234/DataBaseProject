package com.railway.ui.window.entrance.pages.employees.hirePage.speciality.repairman;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.listControllers.teams.TeamListController;
import com.railway.ui.window.entrance.pages.employees.hirePage.speciality.TeamEmployeeRegisterController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class RepairmanRegisterController extends TeamEmployeeRegisterController implements Initializable {
    private static class RepairmanRank {
        private int level;
        private String description;

        public RepairmanRank(int level, String description) {
            this.level = level;
            this.description = description;
        }

        @Override
        public String toString() {
            final String sep = " - ";
            return level + sep + description;
        }
    }

    private static final String sql = "{call ADD_REPAIRMAN(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    @FXML
    private ComboBox<RepairmanRank> repairmanRank;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);


        List<RepairmanRank> ranks = Arrays.asList(
                new RepairmanRank(1, "Intern"),
                new RepairmanRank(2, "Junior"),
                new RepairmanRank(3, "Middle"),
                new RepairmanRank(4, "Advanced"),
                new RepairmanRank(5, "Master"));

        repairmanRank.getItems().addAll(ranks);
        repairmanRank.setValue(ranks.get(0));
    }

    @Override
    public int registerEmployee(Employee employee, String password) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int teamId = getTeamId();
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
            statement.setInt(11, repairmanRank.getValue().level);
            statement.setString(12, password);
            statement.executeUpdate();
            return Errors.QUERY_SUCCESS;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
    }
}
