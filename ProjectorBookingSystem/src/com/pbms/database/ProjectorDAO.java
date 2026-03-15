package com.pbms.database;

import com.pbms.model.Projector;
import com.pbms.model.TimeSlot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectorDAO {

    // Get all active projectors
    public List<Projector> getAllActiveProjectors() {
        List<Projector> list = new ArrayList<>();
        String sql = "SELECT * FROM projectors WHERE is_active = 1";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Projector(
                    rs.getString("projector_id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getInt("is_active") == 1
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Get ALL projectors (for admin panel)
    public List<Projector> getAllProjectors() {
        List<Projector> list = new ArrayList<>();
        String sql = "SELECT * FROM projectors";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Projector(
                    rs.getString("projector_id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getInt("is_active") == 1
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Add a new projector
    public boolean addProjector(String id, String name, String location) {
        String sql = "INSERT INTO projectors(projector_id,name,location,is_active) VALUES(?,?,?,1)";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, location);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // Deactivate a projector
    public boolean deactivateProjector(String id) {
        String sql = "UPDATE projectors SET is_active=0 WHERE projector_id=?";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // Get all time slots
    public List<TimeSlot> getAllTimeSlots() {
        List<TimeSlot> list = new ArrayList<>();
        String sql = "SELECT * FROM time_slots ORDER BY slot_id";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new TimeSlot(
                    rs.getInt("slot_id"),
                    rs.getString("start_time"),
                    rs.getString("end_time"),
                    rs.getString("label")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
