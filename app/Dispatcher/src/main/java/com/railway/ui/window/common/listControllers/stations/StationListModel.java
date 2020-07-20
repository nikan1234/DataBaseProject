package com.railway.ui.window.common.listControllers.stations;

import com.railway.database.DatabaseController;
import com.railway.database.tables.stations.StationDomains;
import com.railway.ui.window.common.entity.Station;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class StationListModel {

    private static PreparedStatement statement;
    static {
        try {
            statement = DatabaseController.getInstance().getConnection().prepareStatement(
                    select().from(StationDomains.TABLE)
                            .orderBy(field(StationDomains.STATION_ID))
                            .toString()
            );
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Station> getStationList() {
        try {
            List<Station> stations = new LinkedList<>();
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                stations.add(new Station(
                        result.getInt(StationDomains.STATION_ID),
                        result.getString(StationDomains.STATION_NAME)
                ));
            }
            return stations;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
