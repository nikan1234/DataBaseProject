package com.railway.ui.window.common.routes;

import com.railway.database.DatabaseController;
import com.railway.database.tables.routes.RouteDomains;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import static org.jooq.impl.DSL.*;

public class RouteTypeListModel {
    private List<String> types;

    public RouteTypeListModel() {
        this.types = new LinkedList<>();
        try {
            final String sql = select()
                    .from(RouteDomains.Types.TABLE_NAME)
                    .orderBy(field(RouteDomains.Types.ROUTE_TYPE))
                    .toString();

            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                types.add(resultSet.getString(RouteDomains.Types.ROUTE_TYPE));
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<String> getRouteTypes() {
        return types;
    }
}
