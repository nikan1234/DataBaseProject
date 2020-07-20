package com.railway.ui.window.common.listControllers.managers;

import com.railway.database.DatabaseController;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.ui.window.common.entity.Driver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class ManagerListModel {

    private static PreparedStatement statement;

    static {
        try {
            statement = DatabaseController.getInstance().getConnection().prepareStatement(
                            "SELECT * FROM MANAGER " +
                            "ORDER BY employee_id"
            );
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public LinkedList<Driver> getManagerList() {
        try {
            LinkedList<Driver> managers = new LinkedList<>();
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                managers.add(new Driver(
                            result.getInt(EmployeeDomains.EMPLOYEE_ID),
                            result.getString(EmployeeDomains.EMPLOYEE_LAST_NAME),
                            result.getString(EmployeeDomains.EMPLOYEE_FIRST_NAME),
                            result.getString(EmployeeDomains.EMPLOYEE_SECOND_NAME),
                            result.getDate(EmployeeDomains.EMPLOYEE_BIRTH_DATE).toLocalDate(),
                            result.getDate(EmployeeDomains.EMPLOYEE_HIRE_DATE).toLocalDate(),
                            result.getDouble(EmployeeDomains.EMPLOYEE_SALARY),
                            result.getString(EmployeeDomains.EMPLOYEE_GENDER),
                            result.getInt(EmployeeDomains.EMPLOYEE_CHILD_COUNT)
                ));
            }
            return managers;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
