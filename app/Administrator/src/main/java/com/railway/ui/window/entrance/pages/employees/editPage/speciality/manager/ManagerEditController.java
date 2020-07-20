package com.railway.ui.window.entrance.pages.employees.editPage.speciality.manager;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.entrance.pages.employees.editPage.speciality.EditController;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ManagerEditController extends EditController {
    private static final String sql = "UPDATE MANAGER SET " +
            "last_name = ?, first_name = ?, second_name = ?, " +
            "hire_date =  TO_DATE(?, 'YYYY-MM-DD'), " +
            "birth_date = TO_DATE(?, 'YYYY-MM-DD'), " +
            "gender = ?, " +
            "child_count = ?, salary = ? " +
            "WHERE employee_id = ?";

    @Override
    public int editEmployee(Employee employee) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, employee.getLastName());
            statement.setString(2, employee.getFirstName());
            statement.setString(3, employee.getSecondName());
            statement.setString(4, employee.getHireDate().format(formatter));
            statement.setString(5, employee.getBirthDate().format(formatter));
            statement.setString(6, employee.getGender());
            statement.setInt(7, employee.getChildCount());
            statement.setDouble(8, employee.getSalary());
            statement.setInt(9, employee.getId());
            statement.executeUpdate();
            return Errors.QUERY_SUCCESS;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
    }

    @Override
    public void setEmployeeId(int employeeId) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
