package com.railway.ui.window.entrance.pages.delays.actions.add;

import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddDelayModel {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static final String sql = "{call ADD_DELAY(?, ?, ?, ?)";

    private static PreparedStatement statement;

    static {
        try {
            statement = DatabaseController.getInstance().getConnection().prepareCall(sql);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public int addDelay(int flightId, int duration, String cause, LocalDateTime operationDate) {
        final int ID = 1;
        final int CAUSE = 2;
        final int DURATION = 3;
        final int OPERATION_DATE = 4;

        try {
            statement.setInt(ID, flightId);
            statement.setInt(DURATION, duration);
            statement.setString(CAUSE, cause);
            statement.setString(OPERATION_DATE, operationDate.format(formatter));
            int rows = statement.executeUpdate();
            if (rows != 1) {
                return Errors.SAME_DATA_ALREADY_EXISTS;
            }
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }

}
