package ru.javabegin.tutorial.androidfinance.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private static Connection connection;
    private static String urlConnection;
    private static String driverClassName;

    public static void init(String driverName, String url) {
        urlConnection = url;
        driverClassName = driverName;
        createConnection();
    }

    private static void createConnection() {
        try {
            Class.forName(driverClassName).newInstance();
            if (connection == null) {
                connection = DriverManager.getConnection(urlConnection);
                connection.createStatement().execute("PRAGMA foreign_keys = ON");
                connection.createStatement().execute("PRAGMA encoding = \"UTF-8\"");
            }
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection() {
        try {
            if(connection == null || connection.isClosed()) {
                createConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
