package com.railway.ui.window.entrance.pages.flightDetails;

import com.railway.database.DatabaseController;
import com.railway.database.tables.flights.FlightDomains;
import com.railway.database.utils.DatabaseUtils;
import com.railway.database.tables.stations.StationDomains;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class FlightDetailsFormModel {
    private int flightNumber;
    private boolean good = true;
    private String status;
    private String describe;
    private double ticketCost;
    private int duration;
    private List<Pair<String, String>> stations;

    private static final String GOOD = "OK";
    private static final String DELAYED = "Delayed";
    private static final String CANCELLED = "Cancelled";

    private static final String sqlGetRoute = "SELECT * FROM FLIGHT_DETAILS " +
                                              "WHERE flight_number = ? " +
                                              "ORDER BY TO_DATE(arrival_time, 'DD-MM-YYYY HH24:MI:SS')";

    private static final String sqlGetInfo = "SELECT * FROM FLIGHTS_TIMETABLE " +
                                             "WHERE flight_number = ? ";

    public FlightDetailsFormModel(int flightNumber) {
        this.describe = "";
        this.flightNumber = flightNumber;
        this.stations = new LinkedList<>();
        this.status = GOOD;

        try {
            Connection connection = DatabaseController.getInstance().getConnection();
            PreparedStatement statement = connection.prepareCall(sqlGetRoute);
            statement.setInt(1, this.flightNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String station = result.getString(StationDomains.STATION_NAME);
                String date = result.getString(FlightDomains.Timetable.ARRIVAL_TIME);
                stations.add(new Pair<>(station, date));
            }

            statement = connection.prepareStatement(sqlGetInfo);
            statement.setInt(1, this.flightNumber);
            result = statement.executeQuery();
            if (!result.next()) {
                return;
            }

            if (DatabaseUtils.isTrue(result.getString(FlightDomains.Timetable.HAS_DELAY))) {
                good = false;
                status = DELAYED;

                String delayDuration = result.getString(FlightDomains.Timetable.DELAY_DURATION);
                String delayCause = result.getString(FlightDomains.Timetable.DELAY_CAUSE);
                describe = "Delayed by " + delayDuration + " hours. Cause: " + delayCause;
            }
            if (DatabaseUtils.isTrue(result.getString(FlightDomains.Timetable.HAS_CANCELLATION))) {
                good = false;
                status = CANCELLED;
                String cancelCause = result.getString(FlightDomains.Timetable.CANCEL_CAUSE);
                describe = "This flight cancelled. Cause " + cancelCause;
            }
            duration = result.getInt(FlightDomains.Timetable.FLIGHT_DURATION);
            ticketCost = result.getDouble(FlightDomains.TICKET_COST);
        }
        catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public boolean good() {
        return good;
    }

    public String getStatus() {
        return status;
    }

    public double getTicketCost() {
        return ticketCost;
    }

    public int getDuration() {
        return duration;
    }

    public String getProblemDescribe() {
        return describe;
    }

    public List<Pair<String, String>> getRoute() {
        return stations;
    }
}
