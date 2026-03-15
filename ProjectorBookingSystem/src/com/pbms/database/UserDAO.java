package com.pbms.database;

import java.sql.*;

public class UserDAO {

    // Returns the role ('admin' or 'user') if login is valid, or null if invalid
    public String validateLogin(String username, String password) {
        // MD5 is used to match what we stored in the database
        String sql = "SELECT role FROM users WHERE username=? AND password=MD5(?)";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("role");
        } catch (SQLException e) { e.printStackTrace(); }
        return null;  // login failed
    }
}
