package com.railway.ui.window.entrance.pages.passengers;

import com.railway.database.DatabaseController;
import com.railway.database.Matcher;
import com.railway.database.tables.flights.FlightDomains;
import com.railway.database.tables.routes.RouteDomains;
import com.railway.database.tables.tickets.TicketDomains;
import org.jooq.Table;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

public class PassengersFormModel {
    private Table<?> sourceTable;

    public PassengersFormModel() {
        sourceTable = table(TicketDomains.TABLE_NAME)
                .innerJoin(table(FlightDomains.TABLE_NAME)).using(field(FlightDomains.FLIGHT_NUMBER))
                .innerJoin(table(RouteDomains.TABLE_NAME)).using(field(RouteDomains.ROUTE_ID));
    }

    public int getPassengerCount(Collection<Matcher> matchers) {
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

    public List<Passenger> getPassengerList(Collection<Matcher> matchers) {
        try {
            String sql = select().from(sourceTable)
                    .where(matchers.stream().map(s -> condition(s.getCondition())).collect(Collectors.toList()))
                    .orderBy(field(TicketDomains.Passenger.LAST_NAME),
                             field(TicketDomains.Passenger.FIRST_NAME),
                             field(TicketDomains.Passenger.SECOND_NAME))
                    .toString().replace("\"", "");

            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            List<Passenger> passengers = new LinkedList<>();
            while (result.next()) {
                passengers.add(new Passenger(
                        result.getString(TicketDomains.Passenger.LAST_NAME),
                        result.getString(TicketDomains.Passenger.FIRST_NAME),
                        result.getString(TicketDomains.Passenger.SECOND_NAME),
                        result.getInt(TicketDomains.Passenger.AGE),
                        result.getString(TicketDomains.Passenger.GENDER)));
            }
            return passengers;

        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
