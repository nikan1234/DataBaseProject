package com.railway.ui.window.entrance.pages.employees.personalPage.speciality.repairman;

import com.railway.database.DatabaseController;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.database.tables.employee.EmployeeMatchers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.jooq.impl.DSL.*;

public class RepairmanSpecModel {
    public static final int BAD_RANK = -1;

    static PreparedStatement statement;

    static {
        try {
            statement = DatabaseController.getInstance().getConnection().prepareStatement(
                    select(field(EmployeeDomains.Repairman.REPAIRMAN_RANK))
                            .from(EmployeeDomains.Repairman.TABLE_NAME)
                            .where(new EmployeeMatchers.MatchById().getCondition())
                            .toString());
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getRepairmanRank(int employeeId) {
        try {
            statement.setInt(1, employeeId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(EmployeeDomains.Repairman.REPAIRMAN_RANK);
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return BAD_RANK;
    }
}
