package com.railway.ui.window.entrance.pages.employees.personalPage.speciality;

import com.railway.database.DatabaseController;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.database.tables.employee.EmployeeMatchers;
import com.railway.database.tables.teams.TeamDomains;
import com.railway.ui.window.common.entity.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;

public class TeamInfoModel {
    static final int WRONG_ID = -1;
    static PreparedStatement teamSelectStatement;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();

            String teamSelectSql = select(field(TeamDomains.TEAM_ID), field(TeamDomains.TEAM_TYPE))
                    .from(EmployeeDomains.DETAILS_TABLE_NAME)
                    .innerJoin(TeamDomains.TABLE_NAME).using(field(TeamDomains.TEAM_ID))
                    .where(new EmployeeMatchers.MatchById().getCondition())
                    .toString()
                    .replace("\"", "");
            teamSelectStatement = connection.prepareStatement(teamSelectSql);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private int employeeId;


    public TeamInfoModel() {
        employeeId = WRONG_ID;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Team getEmployeeTeam() {
        final int EMPLOYEE_ID_POS = 1;
        try {
            teamSelectStatement.setInt(EMPLOYEE_ID_POS, employeeId);
            ResultSet result = teamSelectStatement.executeQuery();
            if (result.next()) {
                return new Team(
                        result.getInt(TeamDomains.TEAM_ID),
                        result.getString(TeamDomains.TEAM_TYPE),
                        null
                );
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
