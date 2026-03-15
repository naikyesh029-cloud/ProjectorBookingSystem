package com.pbms.database;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class DatabaseHelper {

    // Change these if your XAMPP settings are different
    private static final String URL  = "jdbc:mysql://localhost:3306/pbms_db";
    private static final String USER = "root";
    private static final String PASS = "";  // XAMPP default has no password

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Database connection failed!\nMake sure XAMPP MySQL is running.\n" + e.getMessage(),
                "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }
}
