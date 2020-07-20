package com.railway.ui.window.entrance.pages.cancellations.actions.remove;


import com.railway.database.DatabaseController;
import com.railway.database.tables.Errors;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RemoveCancellationModel {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static final String sql = "{call REMOVE_CANCELLATION(?, ?)}";

    private static PreparedStatement statement;

    static {
        try {
            statement = DatabaseController.getInstance().getConnection().prepareCall(sql);
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public int removeCancellation(int flightNumber, LocalDateTime operationDate) {
        try {
            statement.setInt(1, flightNumber);
            statement.setString(2, operationDate.format(formatter));
            int rows = statement.executeUpdate();
            if (rows == 0)
                return Errors.NO_DATA_FOUND;
        }
        catch (final SQLException e) {
            System.err.println(e.getMessage());
            return e.getErrorCode();
        }
        return Errors.QUERY_SUCCESS;
    }

}
