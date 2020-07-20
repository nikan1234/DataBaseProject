package com.railway.ui.window.entrance.pages.departments;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.database.tables.management.ManagementDomains;
import com.railway.database.tables.departments.DepartmentDomains;
import com.railway.ui.window.common.entity.Department;
import com.railway.ui.window.common.entity.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DepartmentsFormModel {
    private  static final String selectSql =
                    "SELECT D.department_id, D.department_name, " +
                    "       employee_id, last_name, first_name, second_name, " +
                    "       birth_date, gender, child_count, hire_date, salary " +
                    "FROM " +
                    "EMPLOYEES INNER JOIN MANAGEMENT M USING(employee_id) " +
                    "          RIGHT JOIN DEPARTMENTS D " +
                    "          ON D.department_id = M.department_id " +
                    "ORDER BY D.department_id";
    private static final String selectManagementSql =
                    "SELECT * FROM MANAGEMENT " +
                    "WHERE department_id = ?";

    private static final String insertDepartmentSql =
                    "INSERT INTO DEPARTMENTS " +
                    "VALUES(?, ?)";
    private static final String insertManagementSql =
                    "INSERT INTO MANAGEMENT " +
                    "VALUES(?, ?)";

    private static final String deleteDepartmentSql =
                    "DELETE FROM DEPARTMENTS " +
                    "WHERE department_id = ?";
    private static final String deleteManagementSql =
                    "DELETE FROM MANAGEMENT " +
                    "WHERE department_id = ?";

    private static final String updateDepartmentSql =
                    "UPDATE DEPARTMENTS " +
                    "SET department_name = ? " +
                    "WHERE department_id = ?";
    private static final String updateManagementSql =
                    "UPDATE MANAGEMENT " +
                    "SET employee_id = ? " +
                    "WHERE department_id = ?";


    private static PreparedStatement selectStatement;
    private static PreparedStatement selectManagementStatement;

    private static PreparedStatement insertDepartmentStatement;
    private static PreparedStatement insertManagementStatement;

    private static PreparedStatement updateDepartmentStatement;
    private static PreparedStatement updateManagementStatement;

    private static PreparedStatement deleteDepartmentStatement;
    private static PreparedStatement deleteManagementStatement;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();
            selectStatement = connection.prepareStatement(selectSql);
            selectManagementStatement = connection.prepareStatement(selectManagementSql);

            insertDepartmentStatement = connection.prepareStatement(insertDepartmentSql);
            insertManagementStatement = connection.prepareStatement(insertManagementSql);

            updateDepartmentStatement = connection.prepareStatement(updateDepartmentSql);
            updateManagementStatement = connection.prepareStatement(updateManagementSql);

            deleteDepartmentStatement = connection.prepareStatement(deleteDepartmentSql);
            deleteManagementStatement = connection.prepareStatement(deleteManagementSql);

        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public List<Department> getDepartments() {
        try {
            List<Department> departments = new LinkedList<>();
            ResultSet result = selectStatement.executeQuery();
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
            System.out.println(e.getMessage());
        }
        return new LinkedList<>();
    }

    public int addDepartment(Department department) {
        final int DEPARTMENT_ID_POSITION = 1;
        final int DEPARTMENT_NAME_POSITION = 2;
        final int MANAGER_ID_POSITION = 2;

        try {
            insertDepartmentStatement.setInt(DEPARTMENT_ID_POSITION, department.getDepartmentId());
            insertDepartmentStatement.setString(DEPARTMENT_NAME_POSITION, department.getDepartmentName());
            insertDepartmentStatement.executeQuery();

            Manager manager = department.getManager();
            if (manager == null)
                return Errors.QUERY_SUCCESS;

            insertManagementStatement.setInt(DEPARTMENT_ID_POSITION, department.getDepartmentId());
            insertManagementStatement.setInt(MANAGER_ID_POSITION, manager.getId());
            insertManagementStatement.executeQuery();
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }

    public int editDepartment(Department department) {
        int departmentId = department.getDepartmentId();

        try {
            selectManagementStatement.setInt(1, departmentId);
            int rows = selectManagementStatement.executeUpdate();

            Manager manager = department.getManager();
            if (manager == null) {
                /* Remove manager */
                if (rows > 0) {
                    deleteManagementStatement.setInt(1, departmentId);
                    deleteManagementStatement.execute();
                }
            }
            else {
                int managerId = manager.getId();
                /* Add new manager */
                if (rows == 0) {
                    insertManagementStatement.setInt(1, departmentId);
                    insertManagementStatement.setInt(2, managerId);
                    insertManagementStatement.executeQuery();
                }
                /* Edit existing manager */
                else {
                    updateManagementStatement.setInt(1, managerId);
                    updateManagementStatement.setInt(2, departmentId);
                    updateManagementStatement.executeQuery();
                }
            }
            updateDepartmentStatement.setString(1, department.getDepartmentName());
            updateDepartmentStatement.setInt(2, departmentId);
            return (updateDepartmentStatement.executeUpdate() == 0) ?
                    Errors.NO_DATA_FOUND : Errors.QUERY_SUCCESS;
        }
        catch (final SQLException e) {
            System.out.println(e.getMessage());
            return e.getErrorCode();
        }
    }

    public int deleteDepartment(int departmentId) {
        final int ID_POSITION = 1;

        try {
            deleteDepartmentStatement.setInt(ID_POSITION, departmentId);
            int rows = deleteDepartmentStatement.executeUpdate();
            if (rows == 0)
                return Errors.NO_DATA_FOUND;
            return Errors.QUERY_SUCCESS;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
    }
}
