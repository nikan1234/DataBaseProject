package com.railway.ui.window.entrance.pages.routes.actions.add;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.ui.window.common.entity.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AddRouteFormModel {

    private static PreparedStatement addRoute;
    private static PreparedStatement addPath;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();

            addRoute = connection.prepareStatement("INSERT INTO ROUTES VALUES(?, ?, ?, ?)");
            addPath = connection.prepareStatement("INSERT INTO PATHS VALUES(0, ?, ?, ?, ?)");
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public int addRoute(int id, String type, List<AddRouteFormController.Path> paths) {
        final int ROUTE_ID_POS = 1;
        final int STATION_FROM_POS = 2;
        final int STATION_TO_POS = 3;
        final int ROUTE_TYPE_POS = 4;


        try {
            Station from = paths.get(0).from;
            Station to = paths.get(paths.size() - 1).to;

            addRoute.setInt(ROUTE_ID_POS, id);
            addRoute.setInt(STATION_FROM_POS, from.getId());
            addRoute.setInt(STATION_TO_POS, to.getId());
            addRoute.setString(ROUTE_TYPE_POS, type);
            addRoute.executeQuery();

            addPaths(id, paths);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }

    private void addPaths(int routeId, List<AddRouteFormController.Path> paths) throws SQLException {
        final int STATION_FROM_POS = 1;
        final int STATION_TO_POS = 2;
        final int DURATION_POS = 3;
        final int ROUTE_ID_POS = 4;

        for (AddRouteFormController.Path path : paths) {
            addPath.setInt(STATION_FROM_POS, path.from.getId());
            addPath.setInt(STATION_TO_POS, path.to.getId());
            addPath.setInt(DURATION_POS, path.duration);
            addPath.setInt(ROUTE_ID_POS, routeId);
            addPath.executeQuery();
        }
    }
}
