package com.railway.ui.window.entrance.pages.locomotives.info;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.database.tables.locomotives.LocomotiveDomains;
import com.railway.database.tables.repairs.RepairDomains;
import com.railway.database.tables.teams.TeamDomains;
import com.railway.database.tables.teams.TeamMatchers;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.entity.Locomotive;
import com.railway.ui.window.common.entity.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;

public class LocomotiveInfoFormModel {
    private static final String selectLocomotiveSql =
            "SELECT  L.locomotive_id, L.entry_date, L.locomotive_name, " +
                    "T.team_id, T.team_type " +
                    "FROM LOCOMOTIVES L LEFT JOIN TEAMS T " +
                    "ON L.team_id = T.team_id " +
                    "WHERE locomotive_id = ?";

    private static final String selectLocomotiveRepairsSql =
            "SELECT repair_date FROM REPAIR WHERE locomotive_id = ? " +
            "ORDER BY repair_date";

    private static final String changeTeamSql =
            "UPDATE LOCOMOTIVES SET team_id = ? WHERE locomotive_id = ?";

    private static final String addRepairSql =
            "INSERT INTO REPAIR VALUES(0, ?, TO_DATE(?, 'YYYY-MM-DD'))";

    private static final String deleteRepairSql =
            "DELETE FROM REPAIR WHERE locomotive_id = ? " +
                    "AND TO_CHAR(repair_date, 'YYYY-MM-DD') LIKE ?";

    private static PreparedStatement selectLocomotiveStatement;
    private static PreparedStatement selectLocomotiveRepairsStatement;
    private static PreparedStatement changeTeamStatement;
    private static PreparedStatement addRepairStatement;
    private static PreparedStatement deleteRepairStatement;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();

            selectLocomotiveStatement = connection.prepareStatement(selectLocomotiveSql);
            selectLocomotiveRepairsStatement = connection.prepareStatement(selectLocomotiveRepairsSql);
            changeTeamStatement = connection.prepareStatement(changeTeamSql);
            addRepairStatement = connection.prepareStatement(addRepairSql);
            deleteRepairStatement = connection.prepareStatement(deleteRepairSql);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public LocomotiveInfoFormModel() {}

    public Locomotive getLocomotive(int id) {
        try {
            selectLocomotiveStatement.setInt(1, id);
            ResultSet result = selectLocomotiveStatement.executeQuery();
            if (result.next()) {
                Team team = null;
                int teamId = result.getInt(TeamDomains.TEAM_ID);
                if (!result.wasNull()) {
                    team = new Team(teamId, result.getString(TeamDomains.TEAM_TYPE));
                }

                return new Locomotive(
                        result.getInt(LocomotiveDomains.ID),
                        result.getString(LocomotiveDomains.NAME),
                        result.getDate(LocomotiveDomains.ENTRY_DATE).toLocalDate(),
                        team
                );
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public int changeTeam(int locomotiveId, int teamId) {
        try {
            changeTeamStatement.setInt(1, teamId);
            changeTeamStatement.setInt(2, locomotiveId);
            int rows = changeTeamStatement.executeUpdate();
            if (rows != 1) {
                return Errors.NO_DATA_FOUND;
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }

    public List<LocalDate> getRepairs(int locomotiveId) {
        try {
            List<LocalDate> repairs = new LinkedList<>();

            selectLocomotiveRepairsStatement.setInt(1, locomotiveId);
            ResultSet result = selectLocomotiveRepairsStatement.executeQuery();
            while (result.next()) {
                repairs.add(result.getDate(RepairDomains.DATE).toLocalDate());
            }
            return repairs;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }

    public int addRepair(int locomotiveId, LocalDate date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            addRepairStatement.setInt(1, locomotiveId);
            addRepairStatement.setString(2, date.format(formatter));
            addRepairStatement.executeUpdate();
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }

    public int removeRepair(int locomotiveId, LocalDate date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            deleteRepairStatement.setInt(1, locomotiveId);
            deleteRepairStatement.setString(2, date.format(formatter));
            deleteRepairStatement.executeUpdate();
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }

    public List<Employee> getByTeam(int teamId) {
        try {
            List<Employee> employees = new LinkedList<>();

            final String sql = select().from(EmployeeDomains.TABLE_NAME)
                    .where(new TeamMatchers.MatchByTeamId().bind(teamId).getCondition())
                    .orderBy(field(EmployeeDomains.EMPLOYEE_ID))
                    .toString();
            PreparedStatement statement = DatabaseController.getInstance()
                    .getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                employees.add(new Employee(
                        result.getInt(EmployeeDomains.EMPLOYEE_ID),
                        result.getString(EmployeeDomains.EMPLOYEE_LAST_NAME),
                        result.getString(EmployeeDomains.EMPLOYEE_FIRST_NAME),
                        result.getString(EmployeeDomains.EMPLOYEE_SECOND_NAME),
                        result.getDate(EmployeeDomains.EMPLOYEE_BIRTH_DATE).toLocalDate(),
                        result.getDate(EmployeeDomains.EMPLOYEE_HIRE_DATE).toLocalDate(),
                        result.getString(EmployeeDomains.EMPLOYEE_SPECIALITY),
                        result.getDouble(EmployeeDomains.EMPLOYEE_SALARY),
                        result.getString(EmployeeDomains.EMPLOYEE_GENDER),
                        result.getInt(EmployeeDomains.EMPLOYEE_CHILD_COUNT)
                ));
            }
            return employees;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
