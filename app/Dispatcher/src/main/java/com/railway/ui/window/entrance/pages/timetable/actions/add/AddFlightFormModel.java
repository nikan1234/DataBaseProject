package com.railway.ui.window.entrance.pages.timetable.actions.add;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.ui.window.common.entity.Flight;
import com.railway.ui.window.common.entity.Locomotive;
import com.railway.ui.window.common.entity.Route;
import org.jooq.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddFlightFormModel {
    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static PreparedStatement flightDurationSelect;
    private static PreparedStatement insertFlight;
    private static PreparedStatement insertInspection;
    private static PreparedStatement insertRepair;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();

            flightDurationSelect = connection.prepareStatement(
                    "SELECT route_duration FROM ROUTE_DURATIONS WHERE route_id = ?"
            );
            insertFlight = connection.prepareStatement(
                    "INSERT INTO FLIGHTS VALUES(?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?, ?)"
            );
            insertInspection = connection.prepareStatement(
                    "INSERT INTO INSPECTIONS VALUES(0, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'))"
            );
            insertRepair = connection.prepareStatement(
                    "INSERT INTO REPAIR VALUES(0, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'))"
            );
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getRouteDuration(Route route) {
        try {
            flightDurationSelect.setInt(1, route.getRouteId());
            ResultSet result= flightDurationSelect.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    public int addFlight(Flight flight, Locomotive locomotive, Route route, LocalDateTime date) {
        final int NUMBER_POS = 1;
        final int DATE_POS = 2;
        final int LOCOMOTIVE_POS= 3;
        final int ROUTE_POS = 4;
        final int COST_POS = 5;
        final int TYPE_POS = 6;

        try {
            insertFlight.setInt(NUMBER_POS, flight.getFlightNumber());
            insertFlight.setString(DATE_POS, date.format(formatter));
            insertFlight.setInt(LOCOMOTIVE_POS, locomotive.getLocomotiveId());
            insertFlight.setInt(ROUTE_POS, route.getRouteId());
            insertFlight.setDouble(COST_POS, flight.getTicketCost());
            insertFlight.setString(TYPE_POS, flight.getFlightType());

            int rows = insertFlight.executeUpdate();
            if (rows != 1) {
                return Errors.NO_DATA_FOUND;
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }

    public int addInspection(Flight flight, LocalDateTime date) {
        try {
            insertInspection.setInt(1, flight.getFlightNumber());
            insertInspection.setString(2, date.format(formatter));
            int rows = insertInspection.executeUpdate();
            if (rows != 1) {
                return Errors.NO_DATA_FOUND;
            }
        }
        catch (final SQLException e) {
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }

    public int addRepair(Locomotive locomotive, LocalDateTime date) {
        try {
            insertRepair.setInt(1, locomotive.getLocomotiveId());
            insertRepair.setString(2, date.format(formatter));
            int rows = insertRepair.executeUpdate();
            if (rows != 1) {
                return Errors.NO_DATA_FOUND;
            }
        }
        catch (final SQLException e) {
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }
}
