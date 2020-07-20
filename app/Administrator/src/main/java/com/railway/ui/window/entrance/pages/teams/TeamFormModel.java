package com.railway.ui.window.entrance.pages.teams;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.database.tables.teams.TeamDomains;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.entity.Team;
import com.railway.ui.window.common.entity.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TeamFormModel {
    private Department department;
    private static final String selectSql = "SELECT * FROM TEAMS " +
                                            "WHERE department_id = ? " +
                                            "ORDER BY team_id";

    private static final String insertSql = "INSERT INTO TEAMS " +
                                            "VALUES(?, ?, ?)";

    private static final String deleteSql = "DELETE FROM TEAMS " +
                                            "WHERE team_id = ?";

    private static final String employeesSql =
            "SELECT * FROM EMPLOYEES WHERE team_id = ? " +
            "ORDER BY employee_id";

    private static PreparedStatement selectStatement;
    private static PreparedStatement insertStatement;
    private static PreparedStatement deleteStatement;
    private static PreparedStatement employeesStatement;

    static {
        Connection connection = DatabaseController.getInstance().getConnection();
        try {
            selectStatement = connection.prepareStatement(selectSql);
            insertStatement = connection.prepareStatement(insertSql);
            deleteStatement = connection.prepareStatement(deleteSql);
            employeesStatement = connection.prepareStatement(employeesSql);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public TeamFormModel(Department department) {
        this.department = department;
    }

    public List<Team> getTeamList() {
        final int DEPARTMENT_ID_POS = 1;
        try {
            List<Team> teams = new LinkedList<>();
            selectStatement.setInt(DEPARTMENT_ID_POS, department.getDepartmentId());
            ResultSet result = selectStatement.executeQuery();
            while (result.next()) {
                teams.add(new Team(
                        result.getInt(TeamDomains.TEAM_ID),
                        result.getString(TeamDomains.TEAM_TYPE),
                        department
                ));
            }
            return teams;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }

    public int addTeam(Team team) {
        final int TEAM_ID_POSITION = 1;
        final int TEAM_TYPE_POSITION = 2;
        final int DEPARTMENT_ID_POSITION = 3;

        try {
            insertStatement.setInt(TEAM_ID_POSITION, team.getTeamId());
            insertStatement.setString(TEAM_TYPE_POSITION, team.getTeamType());
            insertStatement.setInt(DEPARTMENT_ID_POSITION, team.getDepartment().getDepartmentId());
            insertStatement.executeQuery();
        }
        catch (final SQLException e) {
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }


    public int deleteTeam(int id) {
        final int TEAM_ID_POSITION = 1;

        try {
            deleteStatement.setInt(TEAM_ID_POSITION, id);
            int rows = deleteStatement.executeUpdate();
            if (rows == 0)
                return Errors.NO_DATA_FOUND;
            return Errors.QUERY_SUCCESS;
        }
        catch (final SQLException e) {
            return e.getErrorCode();
        }
    }

    public List<Employee> getEmployees(int teamId) {
        try {
            List<Employee> employees = new LinkedList<>();

            employeesStatement.setInt(1, teamId);
            ResultSet result = employeesStatement.executeQuery();
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
            return new LinkedList<>();
        }
    }
}
