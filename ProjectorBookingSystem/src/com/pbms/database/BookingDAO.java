package com.pbms.database;

import com.pbms.model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public boolean isSlotAvailable(String projectorId, String date, int slotId) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE projector_id=? AND booking_date=? AND slot_id=?";
        try {
            PreparedStatement ps = DatabaseHelper.getConnection().prepareStatement(sql);
            ps.setString(1, projectorId);
            ps.setString(2, date);
            ps.setInt(3, slotId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) == 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean addBooking(String projectorId, String bookedBy,
                              String date, int slotId, String purpose, String labName) {
        if (!isSlotAvailable(projectorId, date, slotId)) return false;
        String sql = "INSERT INTO bookings(projector_id,booked_by,booking_date,slot_id,purpose,lab_name) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement ps = DatabaseHelper.getConnection().prepareStatement(sql);
            ps.setString(1, projectorId);
            ps.setString(2, bookedBy);
            ps.setString(3, date);
            ps.setInt(4, slotId);
            ps.setString(5, purpose);
            ps.setString(6, labName);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean cancelBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE booking_id=?";
        try {
            PreparedStatement ps = DatabaseHelper.getConnection().prepareStatement(sql);
            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Booking> getBookingsForDate(String date) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.projector_id, b.booked_by, b.booking_date, " +
                     "b.slot_id, t.label, b.purpose, p.name, b.lab_name " +
                     "FROM bookings b " +
                     "JOIN time_slots t ON b.slot_id = t.slot_id " +
                     "JOIN projectors p ON b.projector_id = p.projector_id " +
                     "WHERE b.booking_date = ?";
        try {
            PreparedStatement ps = DatabaseHelper.getConnection().prepareStatement(sql);
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Booking(
                    rs.getInt("booking_id"), rs.getString("projector_id"),
                    rs.getString("booked_by"), rs.getString("booking_date"),
                    rs.getInt("slot_id"),     rs.getString("label"),
                    rs.getString("purpose"),  rs.getString("name"),
                    rs.getString("lab_name")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.projector_id, b.booked_by, b.booking_date, " +
                     "b.slot_id, t.label, b.purpose, p.name, b.lab_name " +
                     "FROM bookings b " +
                     "JOIN time_slots t ON b.slot_id = t.slot_id " +
                     "JOIN projectors p ON b.projector_id = p.projector_id " +
                     "ORDER BY b.booking_date, b.slot_id";
        try {
            PreparedStatement ps = DatabaseHelper.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Booking(
                    rs.getInt("booking_id"), rs.getString("projector_id"),
                    rs.getString("booked_by"), rs.getString("booking_date"),
                    rs.getInt("slot_id"),     rs.getString("label"),
                    rs.getString("purpose"),  rs.getString("name"),
                    rs.getString("lab_name")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<String> getAllLabs() {
        List<String> labs = new ArrayList<>();
        String sql = "SELECT lab_name FROM labs ORDER BY lab_id";
        try {
            PreparedStatement ps = DatabaseHelper.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) labs.add(rs.getString("lab_name"));
        } catch (SQLException e) { e.printStackTrace(); }
        return labs;
    }
}