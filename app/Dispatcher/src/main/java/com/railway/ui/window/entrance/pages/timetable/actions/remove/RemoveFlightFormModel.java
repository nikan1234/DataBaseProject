package com.railway.ui.window.entrance.pages.timetable.actions.remove;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.database.tables.flights.FlightDomains;
import com.railway.database.tables.flights.FlightMatchers;
import com.railway.ui.window.common.entity.Flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class RemoveFlightFormModel {

    public List<Flight> getFlights(String idBeginning) {
        Connection connection = DatabaseController.getInstance().getConnection();
        try {
            List<Flight> flights = new LinkedList<>();

            PreparedStatement selectFlightsStatement = connection.prepareStatement(
                    select().from(FlightDomains.TABLE_NAME)
                            .where(condition(new FlightMatchers
                                    .MatchByFlightIdBeginning()
                                    .bind(idBeginning)
                                    .getCondition()))
                            .orderBy(field(FlightDomains.FLIGHT_NUMBER))
                            .toString());
            ResultSet result = selectFlightsStatement.executeQuery();
            while (result.next()) {
                flights.add(new Flight(
                        result.getInt(FlightDomains.FLIGHT_NUMBER),
                        toDateTime(result.getString(FlightDomains.FLIGHT_DATE)),
                        result.getString(FlightDomains.FLIGHT_TYPE),
                        result.getDouble(FlightDomains.TICKET_COST)
                ));
            }
            return flights;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }

    public int removeFlight(int flightId) {
        Connection connection = DatabaseController.getInstance().getConnection();
        try {
            PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM FLIGHTS WHERE flight_number = ?"
            );
            deleteStatement.setInt(1, flightId);
            int rows = deleteStatement.executeUpdate();
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

    private LocalDateTime toDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
        return LocalDateTime.parse(date, formatter);
    }
}
