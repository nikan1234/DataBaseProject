package com.railway.ui.window.entrance.pages.cancellations;

import com.railway.database.DatabaseController;
import com.railway.database.Matcher;
import com.railway.database.tables.cancellations.CancellationsDomains;
import com.railway.database.tables.flights.FlightDomains;
import com.railway.ui.window.common.entity.Flight;
import org.jooq.Condition;
import org.jooq.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.field;

public class CancelFormModel {
    private Table<?> flightsTable;

    public CancelFormModel() {
        flightsTable = table(CancellationsDomains.TABLE_NAME)
                .innerJoin(FlightDomains.TABLE_NAME)
                .using(field(FlightDomains.FLIGHT_NUMBER));
    }

    public List<Flight> getFlights(Collection<Matcher> matchers) {
        try {
            List<Flight> flights = new LinkedList<>();

            Collection<Condition> conditions = matchers.stream()
                    .map(s -> condition(s.getCondition()))
                    .collect(Collectors.toList());

            String sql = select().from(flightsTable)
                    .where(conditions)
                    .orderBy(field(FlightDomains.FLIGHT_NUMBER))
                    .toString().replace("\"", "");

            PreparedStatement statement = DatabaseController
                    .getInstance()
                    .getConnection()
                    .prepareStatement(sql);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int number = result.getInt(FlightDomains.FLIGHT_NUMBER);
                String type = result.getString(FlightDomains.FLIGHT_TYPE);
                double cost = result.getDouble(FlightDomains.TICKET_COST);
                String date = formatDate(result.getString(FlightDomains.FLIGHT_DATE));
                flights.add(new Flight(number, date, type, cost));
            }
            return flights;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }

    public int getFlightsCount(Collection<Matcher> matchers) {
        try {
            Collection<Condition> conditions = matchers.stream()
                    .map(s -> condition(s.getCondition()))
                    .collect(Collectors.toList());

            String sql = selectCount().from(flightsTable).where(conditions)
                    .toString().replace("\"", "");
            PreparedStatement statement = DatabaseController
                    .getInstance()
                    .getConnection()
                    .prepareStatement(sql);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }


    private String formatDate(String date) {
        final DateTimeFormatter formatterFrom =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
        final DateTimeFormatter formatterTo =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.parse(date, formatterFrom).format(formatterTo);
    }
}
