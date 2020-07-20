package com.railway.ui.window.entrance.pages.locomotives.actions.add;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.database.tables.teams.TeamMatchers;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.entity.Locomotive;
import com.railway.ui.window.common.formatters.DateFormatter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class AddLocomotivesFormModel {
    private static final String insertSql =
            "INSERT INTO LOCOMOTIVES(locomotive_id, locomotive_name, team_id, entry_date) " +
                    "VALUES(?, ?, ?, TO_DATE(?, 'DD-MM-YYYY'))";

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

    public int addLocomotive(Locomotive locomotive) {
        final int LOCOMOTIVE_ID_POS = 1;
        final int LOCOMOTIVE_NAME_POS = 2;
        final int SERVICE_TEAM_POS = 3;
        final int ENTRY_DATE_POS = 4;
        try {
            PreparedStatement statement = DatabaseController.getInstance()
                    .getConnection().prepareStatement(insertSql);
            statement.setInt(LOCOMOTIVE_ID_POS, locomotive.getLocomotiveId());
            statement.setInt(SERVICE_TEAM_POS, locomotive.getServiceTeam().getTeamId());
            statement.setString(LOCOMOTIVE_NAME_POS, locomotive.getLocomotiveName());
            statement.setString(ENTRY_DATE_POS, locomotive.getEntryDate().format(DateFormatter.FORMATTER));
            int rows = statement.executeUpdate();
            if (rows != 1) {
                return Errors.SAME_DATA_ALREADY_EXISTS;
            }
        }
        catch (final SQLException e) {
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }
}
