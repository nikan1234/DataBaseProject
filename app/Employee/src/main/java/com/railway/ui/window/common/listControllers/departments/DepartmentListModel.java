package com.railway.ui.window.common.listControllers.departments;

import com.railway.database.DatabaseController;
import com.railway.database.tables.departments.DepartmentDomains;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.database.tables.management.ManagementDomains;
import com.railway.ui.window.common.entity.Department;
import com.railway.ui.window.common.entity.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class DepartmentListModel {
    private static PreparedStatement statement;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();

            String sqlRequest =
                    "SELECT employee_id, last_name, first_name, second_name, " +
                    "       hire_date, birth_date, gender, child_count, salary, " +
                    "       speciality, team_id, D.department_id, D.department_name " +
                    "FROM EMPLOYEES INNER JOIN MANAGEMENT M USING(employee_id) " +
                    "               RIGHT JOIN DEPARTMENTS D " +
                    "               ON D.department_id = M.department_id " +
                    "ORDER BY D.department_id";
            statement = connection.prepareStatement(sqlRequest);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    List<Department> getDepartmentList() {
        try {
            List<Department> departments = new LinkedList<>();
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Manager manager = null;

                int managerId = result.getInt(ManagementDomains.MANAGER_ID);
                if (!result.wasNull()) {
                    manager = new Manager(
                            managerId,
                            result.getString(EmployeeDomains.EMPLOYEE_LAST_NAME),
                            result.getString(EmployeeDomains.EMPLOYEE_FIRST_NAME),
                            result.getString(EmployeeDomains.EMPLOYEE_SECOND_NAME),
                            result.getDate(EmployeeDomains.EMPLOYEE_BIRTH_DATE).toLocalDate(),
                            result.getDate(EmployeeDomains.EMPLOYEE_HIRE_DATE).toLocalDate(),
                            result.getDouble(EmployeeDomains.EMPLOYEE_SALARY),
                            result.getString(EmployeeDomains.EMPLOYEE_GENDER),
                            result.getInt(EmployeeDomains.EMPLOYEE_CHILD_COUNT)
                    );
                }
                departments.add(new Department(
                        result.getInt(DepartmentDomains.DEPARTMENT_ID),
                        result.getString(DepartmentDomains.DEPARTMENT_NAME),
                        manager
                ));
            }
            return departments;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
