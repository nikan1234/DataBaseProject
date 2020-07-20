package com.railway.ui.window.entrance.pages.employees.hirePage.speciality.manager;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.entrance.pages.employees.hirePage.speciality.RegisterController;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ManagerRegisterController extends RegisterController {
    private static final String sql = "{call ADD_MANAGER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    @Override
    public int registerEmployee(Employee employee, String password) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareCall(sql);
            statement.setInt(1, employee.getId());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getFirstName());
            statement.setString(4, employee.getSecondName());
            statement.setString(5, employee.getHireDate().format(formatter));
            statement.setString(6, employee.getBirthDate().format(formatter));
            statement.setString(7, employee.getGender());
            statement.setInt(8, employee.getChildCount());
            statement.setDouble(9, employee.getSalary());
            statement.setString(10, password);
            statement.executeUpdate();
            return Errors.QUERY_SUCCESS;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
