package com.railway.ui.window.entrance.pages.employees.personalPage.speciality.manager;

import com.railway.database.DatabaseController;
import com.railway.database.tables.departments.DepartmentDomains;
import com.railway.database.tables.employee.EmployeeMatchers;
import com.railway.database.tables.management.ManagementDomains;
import com.railway.ui.window.common.entity.Department;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class ManagerSpecModel {
    private static PreparedStatement statement;

    static {
        try {
            String sql = select(
                    field(DepartmentDomains.DEPARTMENT_ID),
                    field(DepartmentDomains.DEPARTMENT_NAME))
                    .from(DepartmentDomains.TABLE_NAME)
                    .innerJoin(ManagementDomains.TABLE_NAME).using(field(DepartmentDomains.DEPARTMENT_ID))
                    .where(new EmployeeMatchers.MatchById().getCondition())
                    .orderBy(field(DepartmentDomains.DEPARTMENT_ID))
                    .toString()
                    .replace("\"", "");
            statement = DatabaseController.getInstance().getConnection().prepareStatement(sql);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public ManagerSpecModel() {}

    public List<Department> getDepartmentList(int employeeId) {
        final int EMPLOYEE_ID_POS = 1;
        try {
            List<Department> departments = new LinkedList<>();
            statement.setInt(EMPLOYEE_ID_POS, employeeId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                departments.add(new Department(
                        result.getInt(DepartmentDomains.DEPARTMENT_ID),
                        result.getString(DepartmentDomains.DEPARTMENT_NAME),
                        null
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
