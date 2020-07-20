package com.railway.ui.window.entrance.pages.locomotives.actions.remove;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;
import com.railway.database.tables.locomotives.LocomotiveDomains;
import com.railway.database.tables.locomotives.LocomotiveMatchers;
import com.railway.ui.window.common.entity.Locomotive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class RemoveLocomotiveFormModel {
    private static final String deleteSql = "DELETE FROM LOCOMOTIVES WHERE locomotive_id = ?";

    public List<Locomotive> getLocomotivesList(String idBeginning) {
        try {
            List<Locomotive> locomotives = new LinkedList<>();

            final String sql = select().from(LocomotiveDomains.TABLE_NAME)
                    .where(condition(new LocomotiveMatchers
                            .MatchByIdBeginning()
                            .bind(idBeginning)
                            .getCondition()))
                    .toString();

            PreparedStatement statement = DatabaseController.getInstance()
                    .getConnection().prepareStatement(sql);

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

    public int removeLocomotive(int locomotiveId) {
        try {
            PreparedStatement statement = DatabaseController.getInstance()
                    .getConnection().prepareStatement(deleteSql);
            statement.setInt(1, locomotiveId);
            int rows = statement.executeUpdate();
            if (rows != 1) {
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
