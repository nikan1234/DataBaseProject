package com.railway.ui.window.common.listControllers.teams;

import com.railway.database.DatabaseController;
import com.railway.database.tables.teams.TeamDomains;
import com.railway.ui.window.common.entity.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.*;
public class TeamListModel {

    private static PreparedStatement statement;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();
            statement = connection.prepareStatement(select()
                    .from(TeamDomains.TABLE_NAME)
                    .orderBy(field(TeamDomains.TEAM_ID))
                    .toString());
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Team> getTeamList() {
        try {
            List<Team> teams = new LinkedList<>();
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                teams.add(new Team(
                        result.getInt(TeamDomains.TEAM_ID),
                        result.getString(TeamDomains.TEAM_TYPE)
                ));
            }
            return teams;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
