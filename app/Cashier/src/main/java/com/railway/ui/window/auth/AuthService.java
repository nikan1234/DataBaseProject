package com.railway.ui.window.auth;

import com.railway.database.DatabaseController;
import org.jooq.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    public static final String databaseName = "17208_Dolgov";
    public static final String databasePassword = "140506";

    private static final String sql =
            "SELECT user_password FROM " +
                    "USERS_DATA " +
                    "WHERE access_level >= ? " +
                    "AND user_id = ?";

    private final int accessLevel;

    public AuthService(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    boolean login(int userID, String userPassword) throws SQLException {
        final int ACCESS_POSITION = 1;
        final int ID_POSITION = 2;

        final String url = DatabaseController.toURL(
                SettingsStorage.getInstance().getDatabaseAddress(),
                SettingsStorage.getInstance().getDatabasePort());

        DatabaseController controller = DatabaseController.getInstance();
        controller.connect(url, databaseName, databasePassword);

        PreparedStatement statement = controller.getConnection().prepareStatement(sql);
        statement.setInt(ACCESS_POSITION, accessLevel);
        statement.setInt(ID_POSITION, userID);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            return userPassword.equals(result.getString(1));
        }
        return false;
    }
}
