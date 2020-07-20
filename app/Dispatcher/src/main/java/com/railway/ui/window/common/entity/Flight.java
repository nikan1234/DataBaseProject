package com.railway.ui.window.common.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Flight {
    private int flightNumber;
    private LocalDateTime flightDate;
    private String flightType;
    private double ticketCost;

    public DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Flight(int number,
                  String date,
                  String type,
                  double cost) {
        this.flightNumber = number;
        this.flightDate = LocalDateTime.parse(date, formatter);
        this.flightType = Objects.requireNonNullElse(type, "Unknown");
        this.ticketCost = cost;
    }

    public Flight(int number,
                  LocalDateTime date,
                  String type,
                  double cost) {
        this.flightNumber = number;
        this.flightDate = date;
        this.flightType = Objects.requireNonNullElse(type, "Unknown");
        this.ticketCost = cost;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public String getFlightDate() {
        return flightDate.format(formatter);
    }

    public String getFlightType() {
        return flightType;
    }

    public double getTicketCost() {
        return ticketCost;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = LocalDateTime.parse(flightDate, formatter);
    }

    public void setFlightType(String flightType) {
        this.flightType = flightType;
    }

    public void setTicketCost(double ticketCost) {
        this.ticketCost = ticketCost;
    }

    @Override
    public String toString() {
        String format = "Flight #%d\tDate: %s\tType: %s";
        return String.format(format,
                flightNumber,
                flightDate.format(formatter),
                Objects.requireNonNullElse(flightType, "Unknown"));
    }
}
