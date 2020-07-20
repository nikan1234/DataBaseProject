package com.railway.ui.window.common.stations;

import com.railway.database.DatabaseController;
import com.railway.database.tables.stations.StationDomains;
import javafx.util.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.jooq.impl.DSL.*;


public class StationListModel {
    private List<Pair<Integer, String>> stations;

    public StationListModel() {
        stations = new ArrayList<>();
        try {
            final String sql = select()
                    .from(StationDomains.TABLE)
                    .orderBy(field(StationDomains.STATION_ID))
                    .toString();

            PreparedStatement statement = DatabaseController.getInstance().getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                stations.add(new Pair<>(
                        result.getInt(StationDomains.STATION_ID),
                        result.getString(StationDomains.STATION_NAME)));
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Pair<Integer, String>> getStations() {
        return stations;
    }
}
