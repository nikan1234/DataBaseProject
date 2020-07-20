package com.railway.ui.window.entrance.pages.routes;

import com.railway.database.DatabaseController;
import com.railway.database.Matcher;
import com.railway.database.tables.routes.RouteDomains;
import com.railway.database.tables.stations.StationDomains;
import com.railway.ui.window.common.entity.Route;
import com.railway.ui.window.common.entity.Station;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

public class RouteFormModel {

    private static final String START_STATION_NAME = "start_station_name";
    private static final String FINISH_STATION_NAME = "finish_station_name";

    private static Table<?> sourceTable = table(
            "ROUTES R INNER JOIN STATIONS S1  ON R.start_station = S1.station_id " +
                    "INNER JOIN STATIONS S2 ON R.finish_station = S2.station_id "
    );

    private static List<Field<?>> fields = Arrays.asList(
            field(RouteDomains.ROUTE_ID),
            field(RouteDomains.ROUTE_TYPE),
            field(RouteDomains.START_STATION),
            field(RouteDomains.FINISH_STATION),
            field("S1." + StationDomains.STATION_NAME).as(START_STATION_NAME),
            field("S2." + StationDomains.STATION_NAME).as(FINISH_STATION_NAME)
    );

    public RouteFormModel() {}

    public List<Route> getRoutes(Collection<Matcher> matchers) {
        try {
            List<Route> routes = new LinkedList<>();

            Collection<Condition> conditions = matchers.stream()
                    .map(s -> condition(s.getCondition()))
                    .collect(Collectors.toList());

            final String sql = select(fields).from(sourceTable)
                    .where(conditions).orderBy(field(RouteDomains.ROUTE_ID))
                    .toString().replace("\"", "");

            PreparedStatement statement = DatabaseController
                    .getInstance()
                    .getConnection()
                    .prepareStatement(sql);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Station start = new Station(
                        result.getInt(RouteDomains.START_STATION),
                        result.getString(START_STATION_NAME)
                );
                Station finish = new Station(
                        result.getInt(RouteDomains.FINISH_STATION),
                        result.getString(FINISH_STATION_NAME)
                );
                routes.add(new Route(
                        result.getInt(RouteDomains.ROUTE_ID),
                        start, finish,
                        result.getString(RouteDomains.ROUTE_TYPE)
                ));
            }
            return routes;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }

    public int getRouteCount(Collection<Matcher> matchers) {
        try {
            Collection<Condition> conditions = matchers.stream()
                    .map(s -> condition(s.getCondition()))
                    .collect(Collectors.toList());

            final String sql = selectCount().from(sourceTable)
                    .where(conditions).orderBy(field(RouteDomains.ROUTE_ID))
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
 }
