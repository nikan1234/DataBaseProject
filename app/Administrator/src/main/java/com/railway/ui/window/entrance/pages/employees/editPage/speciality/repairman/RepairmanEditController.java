package com.railway.ui.window.entrance.pages.employees.editPage.speciality.repairman;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.listControllers.teams.TeamListController;
import com.railway.ui.window.entrance.pages.employees.editPage.speciality.TeamEmployeeEditController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class RepairmanEditController extends TeamEmployeeEditController implements Initializable {
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

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof RepairmanRank) {
                return level == ((RepairmanRank) obj).level;
            }
            return false;
        }
    }

    private static final String sql = "UPDATE REPAIRMAN SET " +
            "last_name = ?, first_name = ?, second_name = ?, " +
            "hire_date =  TO_DATE(?, 'YYYY-MM-DD'), " +
            "birth_date = TO_DATE(?, 'YYYY-MM-DD'), " +
            "gender = ?, " +
            "child_count = ?, salary = ?, " +
            "team_id = ?, repairman_rank = ? " +
            "WHERE employee_id = ?";

    @FXML
    private ComboBox<RepairmanRank> repairmanRank;

    private List<RepairmanRank> ranks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);


        ranks = Arrays.asList(
                new RepairmanRank(1, "Intern"),
                new RepairmanRank(2, "Junior"),
                new RepairmanRank(3, "Middle"),
                new RepairmanRank(4, "Advanced"),
                new RepairmanRank(5, "Master"));

        repairmanRank.getItems().addAll(ranks);
        repairmanRank.setValue(ranks.get(0));
    }

    @Override
    public int editEmployee(Employee employee) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int teamId = getTeamId();
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
            statement.setInt(10, repairmanRank.getValue().level);
            statement.setInt(11, employee.getId());
            statement.executeUpdate();
            return Errors.QUERY_SUCCESS;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
    }

    @Override
    public void setEmployeeId(int employeeId) {
        super.setEmployeeId(employeeId);
        final String rankSql = "SELECT repairman_rank FROM EMPLOYEES WHERE employee_id = ?";

        try {
            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(rankSql);
            statement.setInt(1, employeeId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int level = result.getInt(1);
                if (!result.wasNull()) {
                    RepairmanRank rank = null;
                    for (RepairmanRank r : ranks) {
                        if (r.level == level) {
                            rank = r;
                        }
                    }
                    if (rank != null) {
                        repairmanRank.setValue(rank);
                    }
                }
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
