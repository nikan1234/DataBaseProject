package com.railway.ui.window.common.entity;

public class Team {
    private Department department;
    private int teamId;
    private String teamType;

    public Team(int id, String type, Department department) {
        this.teamId = id;
        this.teamType = type;
        this.department = department;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getTeamType() {
        return teamType;
    }

    public Department getDepartment() {
        return department;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
