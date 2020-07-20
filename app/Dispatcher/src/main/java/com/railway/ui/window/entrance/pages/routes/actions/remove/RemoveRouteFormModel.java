package com.railway.ui.window.entrance.pages.routes.actions.remove;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.database.tables.routes.RouteDomains;
import com.railway.database.tables.routes.RouteMatchers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import static org.jooq.impl.DSL.*;

public class RemoveRouteFormModel {
    private static PreparedStatement deleteStatement;

    static {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();

            deleteStatement = connection.prepareStatement("DELETE FROM ROUTES WHERE route_id = ?");
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public int removeRoute(int routeId) {
        try {
            deleteStatement.setInt(1, routeId);
            int rows = deleteStatement.executeUpdate();
            if (rows == 0) {
                return Errors.NO_DATA_FOUND;
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }
}
