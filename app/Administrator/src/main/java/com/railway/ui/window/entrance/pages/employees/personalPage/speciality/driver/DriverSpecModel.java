package com.railway.ui.window.entrance.pages.employees.personalPage.speciality.driver;

import com.railway.database.DatabaseController;
import com.railway.database.tables.examinations.ExaminationDomains;
import com.railway.database.tables.examinations.ExaminationMatchers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class DriverSpecModel {
    static final int WRONG_ID = -1;
    static PreparedStatement examinationsSelectStatement;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();

            String examinationsSelectSql = select(field(ExaminationDomains.DATE))
                    .from(ExaminationDomains.TABLE_NAME)
                    .where(new ExaminationMatchers.MatchByPatientId().getCondition())
                    .orderBy(field(ExaminationDomains.DATE).desc())
                    .toString();
            examinationsSelectStatement = connection.prepareStatement(examinationsSelectSql);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private int employeeId;


    public DriverSpecModel() {
        employeeId = WRONG_ID;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }


    public List<LocalDate> getMedicalExaminations() {
        final int EMPLOYEE_ID_POS = 1;
        try {
            List<LocalDate> examinations = new LinkedList<>();
            examinationsSelectStatement.setInt(EMPLOYEE_ID_POS, employeeId);
            ResultSet result = examinationsSelectStatement.executeQuery();
            while (result.next()) {
                examinations.add(result.getDate(ExaminationDomains.DATE).toLocalDate());
            }
            return examinations;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
