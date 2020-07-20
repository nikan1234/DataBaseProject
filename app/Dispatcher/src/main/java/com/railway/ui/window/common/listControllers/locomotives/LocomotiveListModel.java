package com.railway.ui.window.common.listControllers.locomotives;

import com.railway.database.DatabaseController;
import com.railway.database.tables.locomotives.LocomotiveDomains;
import com.railway.ui.window.common.entity.Locomotive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class LocomotiveListModel {
    private static PreparedStatement statement;

    static {
        try {
            statement = DatabaseController.getInstance().getConnection().prepareStatement(
                    "SELECT * FROM LOCOMOTIVES ORDER BY locomotive_id"
            );
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Locomotive> getLocomotives() {
        try {
            List<Locomotive> locomotives = new LinkedList<>();
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                locomotives.add(new Locomotive(
                        result.getInt(LocomotiveDomains.ID),
                        result.getString(LocomotiveDomains.NAME),
                        result.getDate(LocomotiveDomains.ENTRY_DATE).toLocalDate()
                ));
            }
            return locomotives;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
