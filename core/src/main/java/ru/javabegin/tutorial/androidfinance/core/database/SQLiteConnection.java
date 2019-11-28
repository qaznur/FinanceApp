package ru.javabegin.tutorial.androidfinance.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private static Connection connection;

    public static Connection getConnection() {
        try{
            String url = "jdbc:sqlite:C:\\android projects\\courses\\javabegin.ru\\money.db";
            if(connection == null) {
                connection = DriverManager.getConnection(url);
            }
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
