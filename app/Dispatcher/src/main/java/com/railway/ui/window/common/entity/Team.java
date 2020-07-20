package com.railway.ui.window.common.entity;

public class Team {
    private int teamId;
    private String teamType;

    public Team(int id, String type) {
        this.teamId = id;
        this.teamType = type;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getTeamType() {
        return teamType;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof Team) {
            Team other = (Team) obj;
            return teamId == other.teamId;
        }
        return false;
    }

    @Override
    public String toString() {
        String format = "%d - %s";
        return String.format(format, teamId, teamType);
    }
}
