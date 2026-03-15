package com.pbms.view;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private String username;
    private String role;

    public DashboardFrame(String username, String role) {
        this.username = username;
        this.role     = role;
        setTitle("PBMS - Projector Booking System");
        setSize(960, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // ── Header bar ────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(13, 43, 85));
        header.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        // Left: title + subtitle stacked
        JPanel leftInfo = new JPanel(new GridLayout(2, 1));
        leftInfo.setOpaque(false);
        JLabel lblTitle = new JLabel("Projector Booking Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 17));
        lblTitle.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("College Laboratory  |  " + getLabs());
        lblSub.setFont(new Font("Arial", Font.PLAIN, 11));
        lblSub.setForeground(new Color(144, 202, 249));
        leftInfo.add(lblTitle);
        leftInfo.add(lblSub);

        // Right: user badge + logout button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        // Role badge
        JLabel lblRole = new JLabel("  " + role.toUpperCase() + "  ");
        lblRole.setFont(new Font("Arial", Font.BOLD, 11));
        lblRole.setForeground(Color.WHITE);
        lblRole.setOpaque(true);
        lblRole.setBackground(role.equals("admin") ? new Color(183, 28, 28) : new Color(21, 101, 192));
        lblRole.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

        JLabel lblUser = new JLabel(username);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 13));
        lblUser.setForeground(new Color(187, 222, 251));

        // Logout button — top right
        JButton btnLogout = new JButton("⏻  Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.setBackground(new Color(183, 28, 28));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });

        rightPanel.add(lblRole);
        rightPanel.add(lblUser);
        rightPanel.add(btnLogout);

        header.add(leftInfo,   BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        // ── Tabs ──────────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.setBackground(new Color(227, 242, 253));

        tabs.addTab("📋  Book Projector", new BookingPanel(username));
        tabs.addTab("📅  View Schedule",  new SchedulePanel());
        if (role.equals("admin")) {
            tabs.addTab("⚙  Admin Panel", new AdminPanel());
        }

        add(header, BorderLayout.NORTH);
        add(tabs,   BorderLayout.CENTER);
    }

    private String getLabs() {
        return "Labs: C1 – C12";
    }
}