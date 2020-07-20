package com.railway.ui.window.entrance.pages.employees;

import com.railway.database.DatabaseController;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.ui.window.common.entity.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AdvancedEmployeeFormModel {
    static final String sqlSearchAverageSalaryInTeam =
            "SELECT *" +
                    "FROM EMPLOYEES INNER JOIN (" +
                    "    SELECT team_id, avg(salary) AVERAGE_SALARY " +
                    "    FROM EMPLOYEES " +
                    "    WHERE team_id IS NOT NULL " +
                    "    GROUP BY team_id " +
                    ") USING(team_id) " +
                    "WHERE AVERAGE_SALARY BETWEEN ? AND ? " +
                    "ORDER BY team_id, employee_id";

    static final String sqlSearchSummarySalaryInTeam =
            "SELECT *" +
                    "FROM EMPLOYEES INNER JOIN (" +
                    "    SELECT team_id, sum(salary) SUMMARY_SALARY " +
                    "    FROM EMPLOYEES " +
                    "    WHERE team_id IS NOT NULL " +
                    "    GROUP BY team_id " +
                    ") USING(team_id) " +
                    "WHERE SUMMARY_SALARY BETWEEN ? AND ? " +
                    "ORDER BY team_id, employee_id";

    static final String sqlSearchMinSalaryInEachTeam =
                    "SELECT * " +
                    "FROM EMPLOYEES INNER JOIN (" +
                    "     SELECT team_id, min(salary) MIN_SALARY " +
                    "     FROM EMPLOYEES " +
                    "     GROUP BY team_id) " +
                    "USING (team_id) " +
                    "WHERE salary = MIN_SALARY " +
                    "ORDER BY team_id, employee_id";
    static final String sqlSearchMaxSalaryInEachTeam =
                    "SELECT * " +
                    "FROM EMPLOYEES INNER JOIN (" +
                    "     SELECT team_id, max(salary) MAX_SALARY " +
                    "     FROM EMPLOYEES " +
                    "     GROUP BY team_id) " +
                    "USING (team_id) " +
                    "WHERE salary = MAX_SALARY " +
                    "ORDER BY team_id, employee_id";

    static PreparedStatement searchAverageSalaryInTeam;
    static PreparedStatement searchSummarySalaryInTeam;
    static PreparedStatement searchMinSalaryInEachTeam;
    static PreparedStatement searchMaxSalaryInEachTeam;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();
            searchAverageSalaryInTeam = connection.prepareStatement(sqlSearchAverageSalaryInTeam);
            searchSummarySalaryInTeam = connection.prepareStatement(sqlSearchSummarySalaryInTeam);
            searchMinSalaryInEachTeam = connection.prepareStatement(sqlSearchMinSalaryInEachTeam);
            searchMaxSalaryInEachTeam = connection.prepareStatement(sqlSearchMaxSalaryInEachTeam);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Employee> getAverageSalary(int min, int max) {
        try {
            searchAverageSalaryInTeam.setInt(1, min);
            searchAverageSalaryInTeam.setInt(2, max);
            ResultSet result = searchAverageSalaryInTeam.executeQuery();
            return getEmployees(result);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }

    public List<Employee> getSummarySalary(int min, int max) {
        try {
            searchSummarySalaryInTeam.setInt(1, min);
            searchSummarySalaryInTeam.setInt(2, max);
            ResultSet result = searchSummarySalaryInTeam.executeQuery();
            return getEmployees(result);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }

    public List<Employee> getMinSalary() {
        try {
            return getEmployees(searchMinSalaryInEachTeam.executeQuery());
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }

    public List<Employee> getMaxSalary() {
        try {
            return getEmployees(searchMaxSalaryInEachTeam.executeQuery());
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }


    private List<Employee> getEmployees(ResultSet result) throws SQLException {
        List<Employee> employees = new LinkedList<>();

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
}
