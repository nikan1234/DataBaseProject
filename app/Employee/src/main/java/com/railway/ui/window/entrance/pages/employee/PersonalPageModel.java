package com.railway.ui.window.entrance.pages.employee;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.database.tables.employee.EmployeeMatchers;
import com.railway.ui.window.common.entity.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.jooq.impl.DSL.*;

public class PersonalPageModel {

    private static PreparedStatement selectStatement;
    private static PreparedStatement updateStatement;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();

            String sql = select().from(EmployeeDomains.DETAILS_TABLE_NAME)
                    .where(new EmployeeMatchers.MatchById().getCondition())
                    .toString();
            selectStatement = connection.prepareStatement(sql);

            updateStatement = connection.prepareStatement(
                    "UPDATE USERS_DATA SET user_password = ? WHERE user_id = ?"
            );
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public PersonalPageModel() {
    }

    public Employee getEmployeeInfo(int employeeId) {
        final int EMPLOYEE_ID_POS = 1;
        try {

            selectStatement.setInt(EMPLOYEE_ID_POS, employeeId);
            ResultSet result = selectStatement.executeQuery();
            if (result.next()) {
                return new Employee(
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
                );
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public int changePassword(int employeeId, String newPassword) {
        if (newPassword.isEmpty()) {
            return Errors.NOT_ENOUGH_DATA;
        }
        final int PASSWORD_POS = 1;
        final int EMPLOYEE_POS = 2;
        try {
            updateStatement.setInt(EMPLOYEE_POS, employeeId);
            updateStatement.setString(PASSWORD_POS, newPassword);
            int rows = updateStatement.executeUpdate();
            if (rows != 1) {
                return Errors.NO_DATA_FOUND;
            }
        }
        catch (final SQLException e) {
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }
}
