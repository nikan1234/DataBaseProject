package com.railway.ui.window.entrance.pages.timetable;

public class FlightInfoModel {
    private int number;
    private String from;
    private String to;
    private String startDate;
    private String finishDate;
    private String flightStatus;

    public FlightInfoModel(int number,
                           String from,
                           String to,
                           String startDate,
                           String finishDate,
                           String flightStatus) {
        this.number = number;
        this.from = from;
        this.to = to;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.flightStatus = flightStatus;
    }

    public int getNumber() {
        return number;
    }

    public String getSourceStation() {
        return from;
    }

    public String getDestinationStation() {
        return to;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getArrivalDate() {
        return finishDate;
    }

    public String getFlightStatus() {
        return flightStatus;
    }
}
