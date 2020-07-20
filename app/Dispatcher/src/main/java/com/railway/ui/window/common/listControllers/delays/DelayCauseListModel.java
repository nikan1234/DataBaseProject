package com.railway.ui.window.common.listControllers.delays;

import com.railway.database.DatabaseController;
import com.railway.database.tables.delays.DelayDomains;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class DelayCauseListModel {
    static PreparedStatement statement;

    static {
        try {
            statement = DatabaseController.getInstance().getConnection().prepareStatement(
                    selectDistinct(field(DelayDomains.DELAY_CAUSE))
                            .from(DelayDomains.TABLE_NAME)
                            .orderBy(field(DelayDomains.DELAY_CAUSE))
                            .toString()
            );
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<String> getDelayCauses() {
        try {
            List<String> causes = new LinkedList<>();
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                causes.add(
                        result.getString(DelayDomains.DELAY_CAUSE)
                );
            }
            return causes;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
