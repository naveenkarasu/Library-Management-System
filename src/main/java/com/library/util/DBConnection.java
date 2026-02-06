package com.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection utility class.
 * Provides methods to establish and manage database connections.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
public class DBConnection {

    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    // Static block to load database properties
    static {
        loadProperties();
    }

    /**
     * Load database properties from db.properties file
     */
    private static void loadProperties() {
        Properties props = new Properties();
        try (InputStream inputStream = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (inputStream != null) {
                props.load(inputStream);
                driver = props.getProperty("db.driver");
                url = props.getProperty("db.url");
                username = props.getProperty("db.username");
                password = props.getProperty("db.password");

                // Load the JDBC driver
                Class.forName(driver);
            } else {
                System.err.println("db.properties file not found in classpath!");
                // Set default values for development
                driver = "com.mysql.jdbc.Driver";
                url = "jdbc:mysql://localhost:3306/library_db?useSSL=false";
                username = "root";
                password = "root";
                Class.forName(driver);
            }
        } catch (IOException e) {
            System.err.println("Error loading db.properties: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get a new database connection
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Close a database connection safely
     *
     * @param connection the connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test database connection
     *
     * @return true if connection is successful
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get database URL (for debugging)
     *
     * @return database URL
     */
    public static String getUrl() {
        return url;
    }
}
