package com.railway.ui.window.common.entity;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Locomotive {
    private int locomotiveId;
    private String locomotiveName;
    private LocalDate entryDate;
    private Team serviceTeam;

    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Locomotive(int id, String name, LocalDate date) {
        this(id, name, date, null);
    }

    public Locomotive(int id, String name, LocalDate date, Team team) {
        this.locomotiveId = id;
        this.locomotiveName = name;
        this.entryDate = date;
        this.serviceTeam = team;
    }

    public int getLocomotiveId() {
        return locomotiveId;
    }

    public String getLocomotiveName() {
        return locomotiveName;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public Team getServiceTeam() {
        return serviceTeam;
    }

    @Override
    public String toString() {
        return locomotiveId + " " +locomotiveName;
    }
}
