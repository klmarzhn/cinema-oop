package com.example.cinema.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbConnectionManager {
    private static final DbConnectionManager INSTANCE = new DbConnectionManager();

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/cinema-db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";

    private DbConnectionManager() {
    }

    public static DbConnectionManager getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
