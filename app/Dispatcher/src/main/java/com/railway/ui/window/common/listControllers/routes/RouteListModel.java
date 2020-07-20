package com.railway.ui.window.common.listControllers.routes;

import com.railway.database.DatabaseController;
import com.railway.database.tables.routes.RouteDomains;
import com.railway.ui.window.common.entity.Route;
import com.railway.ui.window.common.entity.Station;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RouteListModel {
    private static final String sql =
            "SELECT route_id, route_type, " +
            "       start_station, S_FROM.station_name start_station_name, " +
            "       finish_station, S_TO.station_name finish_station_name " +
            "FROM ROUTES R INNER JOIN ROUTE_TYPES USING(route_type) " +
            "              LEFT JOIN  STATIONS S_FROM ON R.start_station = S_FROM.station_id " +
            "              LEFT JOIN  STATIONS S_TO   ON R.finish_station = S_TO.station_id " +
            "ORDER BY route_id";

    private static PreparedStatement statement;

    static {
        try {
            statement = DatabaseController.getInstance().getConnection().prepareStatement(sql);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Route> getRoutes() {
        final String START_STATION_NAME = "start_station_name";
        final String FINISH_STATION_NAME = "finish_station_name";
        try {
            List<Route> routes = new LinkedList<>();
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
}
