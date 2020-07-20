package com.railway.ui.window.entrance.pages.employees.dismissPage;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.database.tables.employee.EmployeeMatchers;
import com.railway.ui.window.common.entity.Employee;
import org.jooq.Field;
import org.jooq.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class DismissFormModel {
    private Table<?> sourceTable;
    private List<Field<?>> fields;

    public DismissFormModel() {
        sourceTable = table(EmployeeDomains.TABLE_NAME);

        fields = Arrays.asList(
                field(EmployeeDomains.EMPLOYEE_ID),
                field(EmployeeDomains.EMPLOYEE_LAST_NAME),
                field(EmployeeDomains.EMPLOYEE_FIRST_NAME),
                field(EmployeeDomains.EMPLOYEE_SECOND_NAME),
                field(EmployeeDomains.EMPLOYEE_BIRTH_DATE),
                field(EmployeeDomains.EMPLOYEE_HIRE_DATE),
                field(EmployeeDomains.EMPLOYEE_SPECIALITY),
                field(EmployeeDomains.EMPLOYEE_SALARY),
                field(EmployeeDomains.EMPLOYEE_GENDER),
                field(EmployeeDomains.EMPLOYEE_CHILD_COUNT));
    }

    List<Employee> selectEmployees(String idBeginning) {
        try {
            List<Employee> employees = new LinkedList<>();
            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(
                    select(fields).from(sourceTable)
                            .where(new EmployeeMatchers.MatchByIdBeginning().bind(idBeginning).getCondition())
                            .orderBy(fields.get(0))
                            .toString()
            );
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

    public int dismissEmployee(int employeeId) {
        try {
            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(
                    deleteFrom(sourceTable)
                            .where(new EmployeeMatchers
                                    .MatchById()
                                    .bind(employeeId)
                                    .getCondition())
                            .toString());
            int rows = statement.executeUpdate();
            if (rows == 0)
                return Errors.NO_DATA_FOUND;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }
}
