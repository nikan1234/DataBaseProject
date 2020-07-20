package com.railway.ui.window.entrance.pages.flightDetails;

import com.railway.database.DatabaseController;
import com.railway.database.tables.flights.FlightDomains;
import com.railway.database.tables.locomotives.LocomotiveDomains;
import com.railway.database.utils.DatabaseUtils;
import com.railway.database.tables.stations.StationDomains;
import com.railway.ui.window.common.entity.Locomotive;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private Locomotive locomotive;

    private static final String GOOD = "OK";
    private static final String DELAYED = "Delayed";
    private static final String CANCELLED = "Cancelled";

    private static final String sqlGetRoute =
            "SELECT * FROM FLIGHT_DETAILS " +
            "WHERE flight_number = ? " +
            "ORDER BY TO_DATE(arrival_time, 'DD-MM-YYYY HH24:MI:SS')";

    private static final String sqlGetFlightInfo =
            "SELECT * FROM FLIGHTS_TIMETABLE " +
            "WHERE flight_number = ? ";

    private static final String sqlGetLocomotive =
            "SELECT * FROM " +
            "FLIGHTS INNER JOIN LOCOMOTIVES USING(locomotive_id) " +
            "WHERE flight_number = ?";

    public FlightDetailsFormModel(int flightNumber) {
        this.describe = "";
        this.flightNumber = flightNumber;
        this.stations = new LinkedList<>();
        this.status = GOOD;

        try {
            Connection connection = DatabaseController.getInstance().getConnection();

            /* Route history */
            PreparedStatement statement = connection.prepareCall(sqlGetRoute);
            statement.setInt(1, this.flightNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String station = result.getString(StationDomains.STATION_NAME);
                String date    = result.getString(FlightDomains.Timetable.ARRIVAL_TIME);
                stations.add(new Pair<>(station, date));
            }

            statement = connection.prepareStatement(sqlGetFlightInfo);
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
                describe = "This flight cancelled. Cause: " + cancelCause;
            }
            duration = result.getInt(FlightDomains.Timetable.FLIGHT_DURATION);
            ticketCost = result.getDouble(FlightDomains.TICKET_COST);

            /* Locomotive */
            statement = connection.prepareStatement(sqlGetLocomotive);
            statement.setInt(1, flightNumber);
            result = statement.executeQuery();
            if (result.next()) {
                locomotive = new Locomotive(
                        result.getInt(LocomotiveDomains.ID),
                        result.getString(LocomotiveDomains.NAME),
                        result.getDate(LocomotiveDomains.ENTRY_DATE).toLocalDate()
                );
            }
        }
        catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public Locomotive getLocomotiveInfo() {
        return locomotive;
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

    public String getProblemDescribe() {
        return describe;
    }

    public List<Pair<String, String>> getRoute() {
        return stations;
    }

    public double getTicketCost() {
        return ticketCost;
    }

    public int getDuration() {
        return duration;
    }

    private String formatDate(String date) {
        final DateTimeFormatter formatterFrom =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
        final DateTimeFormatter formatterTo =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDateTime.parse(date, formatterFrom).format(formatterTo);
    }
}
