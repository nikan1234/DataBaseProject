package com.railway.ui.window.entrance.pages.delays;

import com.railway.database.DatabaseController;
import com.railway.database.Matcher;
import com.railway.database.tables.delays.DelayDomains;
import com.railway.database.tables.flights.FlightDomains;
import com.railway.database.tables.tickets.TicketDomains;
import com.railway.database.tables.tickets.TicketMatchers;
import com.railway.ui.window.common.entity.Passenger;
import com.railway.ui.window.common.entity.Ticket;
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

public class DelaysFormModel {
    private Table<?> flightsTable;
    private Table<?> ticketsTable;

    public DelaysFormModel() {
        flightsTable = table(DelayDomains.TABLE_NAME);
        ticketsTable = table(TicketDomains.TABLE_NAME)
                .innerJoin(flightsTable)
                .using(field(FlightDomains.FLIGHT_NUMBER));
    }

    public List<DelayedFlight> getFlights(Collection<Matcher> matchers) {
        try {
            List<DelayedFlight> flights = new LinkedList<>();

            Collection<Condition> conditions = matchers.stream()
                    .map(s -> condition(s.getCondition()))
                    .collect(Collectors.toList());

            String sql = select().from(flightsTable)
                    .where(conditions).orderBy(field(FlightDomains.FLIGHT_NUMBER))
                    .toString();

            PreparedStatement statement = DatabaseController
                    .getInstance()
                    .getConnection()
                    .prepareStatement(sql);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                flights.add(new DelayedFlight(
                        result.getInt(FlightDomains.FLIGHT_NUMBER),
                        result.getString(FlightDomains.FLIGHT_DATE),
                        result.getString(DelayDomains.DELAYED_UNTIL),
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

    public List<Ticket> getTickets(Collection<Matcher> matchers) {
        List<Matcher> matchersUpdated = new LinkedList<>(matchers);
        matchersUpdated.add(new TicketMatchers.MatchByDelayedFlight());
        try {
            List<Ticket> tickets = new LinkedList<>();

            Collection<Condition> conditions = matchersUpdated.stream()
                    .map(s -> condition(s.getCondition()))
                    .collect(Collectors.toList());
            String sql = select().from(ticketsTable)
                    .where(conditions).orderBy(field(TicketDomains.TICKET_ID))
                    .toString().replace("\"", "");

            PreparedStatement statement = DatabaseController
                    .getInstance()
                    .getConnection()
                    .prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Passenger owner = new Passenger(
                        result.getString(TicketDomains.Passenger.LAST_NAME),
                        result.getString(TicketDomains.Passenger.FIRST_NAME),
                        result.getString(TicketDomains.Passenger.SECOND_NAME),
                        result.getInt(TicketDomains.Passenger.AGE),
                        result.getString(TicketDomains.Passenger.GENDER));

                String id = result.getString(TicketDomains.TICKET_ID);
                String purchased = result.getString(TicketDomains.PURCHASED);
                String date = result.getString(TicketDomains.OPERATION_DATE);


                double cost = result.getDouble(TicketDomains.TICKET_COST);

                tickets.add(new Ticket(id, formatDate(date), purchased, cost, owner));
            }
            return tickets;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }

    public int getFlightsCount(Collection<Matcher> matchers) {
        return getCount(flightsTable, matchers);
    }

    public int getTicketsCount(Collection<Matcher> matchers) {
        List<Matcher> matchersUpdated = new LinkedList<>(matchers);
        matchersUpdated.add(new TicketMatchers.MatchByDelayedFlight());
        return getCount(ticketsTable, matchersUpdated);
    }

    private int getCount(Table<?> table, Collection<Matcher> matchers) {
        try {
            Collection<Condition> conditions = matchers.stream()
                    .map(s -> condition(s.getCondition()))
                    .collect(Collectors.toList());

            String sql = selectCount().from(table).where(conditions)
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