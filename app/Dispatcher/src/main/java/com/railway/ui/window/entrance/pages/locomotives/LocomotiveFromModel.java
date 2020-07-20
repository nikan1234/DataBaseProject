package com.railway.ui.window.entrance.pages.locomotives;

import com.railway.database.DatabaseController;
import com.railway.database.Matcher;
import com.railway.database.tables.locomotives.LocomotiveDomains;
import com.railway.ui.window.common.entity.Locomotive;
import org.jooq.Condition;
import org.jooq.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

public class LocomotiveFromModel {
    private Table<?> sourceTable;

    public LocomotiveFromModel() {
        sourceTable = table(LocomotiveDomains.TABLE_NAME);
    }

    List<Locomotive> getLocomotives(Collection<Matcher> matchers) {
        try {
            Collection<Condition> conditions = matchers.stream()
                    .map(m -> condition(m.getCondition()))
                    .collect(Collectors.toList());

            List<Locomotive> locomotives = new LinkedList<>();

            final String sql = select()
                    .from(sourceTable)
                    .where(conditions)
                    .orderBy(field(LocomotiveDomains.ID))
                    .toString();

            PreparedStatement statement = DatabaseController
                    .getInstance().getConnection().prepareStatement(sql);

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

    public int getLocomotivesCount(Collection<Matcher> matchers) {
        try {
            Collection<Condition> conditions = matchers.stream()
                    .map(m -> condition(m.getCondition()))
                    .collect(Collectors.toList());

            final String sql = selectCount()
                    .from(sourceTable)
                    .where(conditions)
                    .orderBy(field(LocomotiveDomains.ID))
                    .toString();

            PreparedStatement statement = DatabaseController
                    .getInstance().getConnection().prepareStatement(sql);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }
}
