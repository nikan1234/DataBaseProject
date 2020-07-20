package com.railway.ui.window.entrance.pages.drivers.personalPage;

import com.railway.database.DatabaseController;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.database.tables.employee.EmployeeMatchers;
import com.railway.database.tables.examinations.ExaminationDomains;
import com.railway.database.tables.examinations.ExaminationMatchers;
import com.railway.database.tables.teams.TeamDomains;
import com.railway.ui.window.common.entity.Driver;
import com.railway.ui.window.common.entity.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;

public class PersonalPageModel {

    private static PreparedStatement selectDriverDataStatement;
    private static PreparedStatement examinationsSelectStatement;
    private static PreparedStatement teamSelectStatement;
    private static PreparedStatement addExaminationStatement;
    private static PreparedStatement deleteExaminationStatement;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();

            String selectDriverDataSql = select().from(EmployeeDomains.DETAILS_TABLE_NAME)
                    .where(new EmployeeMatchers.MatchById().getCondition())
                    .toString();
            selectDriverDataStatement = connection.prepareStatement(selectDriverDataSql);


            String examinationsSelectSql = select(field(ExaminationDomains.DATE))
                    .from(ExaminationDomains.TABLE_NAME)
                    .where(new ExaminationMatchers.MatchByPatientId().getCondition())
                    .orderBy(field(ExaminationDomains.DATE).desc())
                    .toString();
            examinationsSelectStatement = connection.prepareStatement(examinationsSelectSql);


            String teamSelectSql = select(field(TeamDomains.TEAM_ID), field(TeamDomains.TEAM_TYPE))
                    .from(EmployeeDomains.DETAILS_TABLE_NAME)
                    .innerJoin(TeamDomains.TABLE_NAME).using(field(TeamDomains.TEAM_ID))
                    .where(new EmployeeMatchers.MatchById().getCondition())
                    .toString()
                    .replace("\"", "");
            teamSelectStatement = connection.prepareStatement(teamSelectSql);


            addExaminationStatement = connection.prepareStatement(
                    "INSERT INTO MEDICAL_EXAMINATIONS VALUES(0, ?, TO_DATE(?, 'YYYY-MM-DD'))"
            );
            deleteExaminationStatement = connection.prepareStatement(
                    "DELETE FROM MEDICAL_EXAMINATIONS WHERE employee_id = ? " +
                            "AND TO_CHAR(med_date, 'YYYY-MM-DD') LIKE ?"
            );
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public PersonalPageModel() {
    }

    public Driver getEmployeeInfo(int employeeId) {
        final int EMPLOYEE_ID_POS = 1;
        try {
            selectDriverDataStatement.setInt(EMPLOYEE_ID_POS, employeeId);
            ResultSet result = selectDriverDataStatement.executeQuery();
            if (result.next()) {
                return new Driver(
                        result.getInt(EmployeeDomains.EMPLOYEE_ID),
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
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<LocalDate> getMedicalExaminations(int employeeId) {
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

    public Team getEmployeeTeam(int employeeId) {
        final int EMPLOYEE_ID_POS = 1;
        try {
            teamSelectStatement.setInt(EMPLOYEE_ID_POS, employeeId);
            ResultSet result = teamSelectStatement.executeQuery();
            if (result.next()) {
                return new Team(
                        result.getInt(TeamDomains.TEAM_ID),
                        result.getString(TeamDomains.TEAM_TYPE)
                );
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void addExamination(int employeeId, LocalDate date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            addExaminationStatement.setInt(1, employeeId);
            addExaminationStatement.setString(2, date.format(formatter));
            addExaminationStatement.executeUpdate();
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteExamination(int employeeId, LocalDate date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            deleteExaminationStatement.setInt(1, employeeId);
            deleteExaminationStatement.setString(2, date.format(formatter));
            deleteExaminationStatement.executeUpdate();
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
