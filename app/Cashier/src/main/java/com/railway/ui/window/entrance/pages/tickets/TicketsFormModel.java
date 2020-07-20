package com.railway.ui.window.entrance.pages.tickets;

import com.railway.database.DatabaseController;
import com.railway.database.Matcher;
import com.railway.database.tables.flights.FlightDomains;
import com.railway.database.tables.routes.RouteDomains;
import com.railway.database.tables.tickets.TicketDomains;
import com.railway.ui.window.entrance.pages.passengers.Passenger;
import org.jooq.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

public class TicketsFormModel {
    private Table<?> sourceTable;

    public TicketsFormModel() {
        sourceTable = table(TicketDomains.TABLE_NAME)
                .innerJoin(FlightDomains.Timetable.TABLE_NAME).using(field(FlightDomains.FLIGHT_NUMBER))
                .innerJoin(RouteDomains.TABLE_NAME).using(field(RouteDomains.ROUTE_ID));
    }

    public int getTicketCount(Collection<Matcher> matchers) {
        try {
            String sql = selectCount().from(sourceTable)
                    .where(matchers.stream().map(s -> condition(s.getCondition())).collect(Collectors.toList()))
                    .toString().replace("\"", "");

            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(sql);
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

    public List<Ticket> getTicketList(Collection<Matcher> matchers) {
        List<Ticket> tickets = new LinkedList<>();
        try {
            String sql = select().from(sourceTable)
                    .where(matchers.stream().map(s -> condition(s.getCondition())).collect(Collectors.toList()))
                    .orderBy(field(TicketDomains.TICKET_ID))
                    .toString().replace("\"", "");

            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(sql);
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
                double duration = result.getDouble(FlightDomains.Timetable.FLIGHT_DURATION);

                tickets.add(new Ticket(id, date, purchased, cost, duration, owner));
            }
            return tickets;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
