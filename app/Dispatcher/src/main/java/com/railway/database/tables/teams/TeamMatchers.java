package com.railway.database.tables.teams;

import com.railway.database.Matcher;

import java.util.Objects;

public class TeamMatchers {

    static public class MatchByTeamId extends Matcher {
        @Override
        public String getCondition() {
            return TeamDomains.TEAM_ID +
                    comparator() +
                    Objects.requireNonNullElse(getValue(), "?");
        }
    }
}
