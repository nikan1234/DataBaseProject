package com.railway.ui.window.common.listControllers.flights;

import com.railway.database.DatabaseController;
import org.jooq.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class FlightTypeListModel {

    private static PreparedStatement statement;

    static {
        try {
            statement = DatabaseController.getInstance().getConnection().prepareStatement(
                    "SELECT * FROM FLIGHT_TYPES"
            );
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<String> getFlightTypes() {
        try {
            List<String> types = new LinkedList<>();
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                types.add(result.getString(1));
            }
            return types;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
