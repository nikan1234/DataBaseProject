package com.railway.ui.window.entrance.pages.timetable;

import com.railway.database.DatabaseController;
import com.railway.database.tables.flights.FlightDomains;
import com.railway.database.utils.DatabaseUtils;
import org.jooq.Condition;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;

public class TimetableModel {
    private LinkedList<Condition> conditions;
    private LinkedList<FlightInfoModel> flights = new LinkedList<>();

    public TimetableModel() {
        conditions = new LinkedList<>();
    }

    public void setFilters(Collection<Condition> filters) {
        conditions.clear();
        conditions.addAll(filters);
    }

    public List<FlightInfoModel> getTimetable() {
        flights.clear();
        try {
            String pattern = "TO_DATE(%s, 'DD-MM-YYYY HH24:MI:SS')";

            String sql = select()
                    .from(FlightDomains.Timetable.TABLE_NAME)
                    .where(conditions)
                    .orderBy(field(String.format(pattern, FlightDomains.Timetable.START_TIME)).desc())
                    .toString();

            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String status = "OK";
                if (DatabaseUtils.isTrue(result.getString(FlightDomains.Timetable.HAS_DELAY))) {
                    status = "Delayed";
                }
                if (DatabaseUtils.isTrue(result.getString(FlightDomains.Timetable.HAS_CANCELLATION))) {
                    status = "Cancelled";
                }

                FlightInfoModel info = new FlightInfoModel(
                        result.getInt(FlightDomains.FLIGHT_NUMBER),
                        result.getString(FlightDomains.Timetable.START_STATION_NAME),
                        result.getString(FlightDomains.Timetable.FINISH_STATION_NAME),
                        result.getString(FlightDomains.Timetable.START_TIME),
                        result.getString(FlightDomains.Timetable.ARRIVAL_TIME),
                        status
                );
                flights.add(info);
            }
        }
        catch (SQLException | IOException e) {
            System.err.println(e.getMessage());
        }
        return flights;
    }
}
