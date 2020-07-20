package com.railway.database;
import java.sql.*;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

public class DatabaseController {
    Connection connection;

    static private volatile DatabaseController instance;

    private DatabaseController() {
        this.connection = null;
    }

    public static DatabaseController getInstance() {
        DatabaseController local_instance = instance;
        if (instance == null) {
            synchronized (DatabaseController.class) {
                local_instance = instance;
                if (instance == null) {
                    instance = local_instance = new DatabaseController();
                }
            }
        }
        return local_instance;
    }

    public static String toURL(String address, int port) {
        final char sep = ':';
        final StringBuilder builder;

        builder = new StringBuilder();
        builder.append("jdbc").append(sep);
        builder.append("oracle").append(sep);
        builder.append("thin").append(sep);

        builder.append("@").append(address).append(sep);
        builder.append(port).append(sep);
        return builder.toString();
    }

    public void connect(String url, String login, String password) throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver not found");
            return;
        }
        Properties props = new Properties();
        props.setProperty("user", login);
        props.setProperty("password", password);

        TimeZone timeZone = TimeZone.getTimeZone("GMT+7");
        TimeZone.setDefault(timeZone);
        Locale.setDefault(Locale.ENGLISH);

        connection = DriverManager.getConnection(url, props);
    }

    public Connection getConnection() {
        return connection;
    }
}
