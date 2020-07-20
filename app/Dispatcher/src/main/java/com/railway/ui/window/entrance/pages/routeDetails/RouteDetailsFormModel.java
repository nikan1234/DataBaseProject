package com.railway.ui.window.entrance.pages.routeDetails;

import com.railway.database.DatabaseController;
import com.railway.database.tables.routes.RouteDomains;
import com.railway.database.tables.routes.RouteMatchers;
import com.railway.database.tables.stations.StationDomains;
import com.railway.ui.window.common.entity.Route;
import com.railway.ui.window.common.entity.Station;
import org.jooq.Field;
import org.jooq.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class RouteDetailsFormModel {

    private static final String FROM_STATION_NAME = "from_station_name";
    private static final String TO_STATION_NAME = "to_station_name";

    private static Table<?> sourceTable = table(
            "ROUTE_INFO R INNER JOIN STATIONS S1 ON R.station_from = S1.station_id " +
                    "INNER JOIN STATIONS S2 ON R.station_to = S2.station_id "
    );

    private static List<Field<?>> fields = Arrays.asList(
            field(RouteDomains.ROUTE_ID),
            field(RouteDomains.History.CURRENT_TIME),
            field(RouteDomains.History.STATION_FROM),
            field(RouteDomains.History.STATION_TO),
            field("S1." + StationDomains.STATION_NAME).as(FROM_STATION_NAME),
            field("S2." + StationDomains.STATION_NAME).as(TO_STATION_NAME)
    );


    private int routeDuration = 0;

    public List<Station> getRouteDetails(Route route) {
        try {
            List<Station> stations = new LinkedList<>();
            final String sql = select(fields).from(sourceTable)
                    .where(condition(new RouteMatchers
                            .MatchByRouteId()
                            .bind(route.getRouteId())
                            .getCondition()))
                    .orderBy(field(RouteDomains.ROUTE_ID),
                            field(RouteDomains.History.CURRENT_TIME))
                    .toString().replace("\"", "");

            PreparedStatement statement = DatabaseController
                    .getInstance()
                    .getConnection()
                    .prepareStatement(sql);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                stations.add(new Station(
                        result.getInt(RouteDomains.History.STATION_FROM),
                        result.getString(FROM_STATION_NAME)));
            }
            do {
                stations.add(new Station(
                        result.getInt(RouteDomains.History.STATION_TO),
                        result.getString(TO_STATION_NAME)));
                routeDuration = result.getInt(RouteDomains.History.CURRENT_TIME);
            }
            while (result.next());
            return stations;
        } catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }

    public int getDuration() {
        return routeDuration;
    }
}
