package com.pbms.view;

import com.pbms.database.BookingDAO;
import com.pbms.database.ProjectorDAO;
import com.pbms.model.Booking;
import com.pbms.model.Projector;
import com.pbms.model.TimeSlot;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookingPanel extends JPanel {

    private JComboBox<Projector> cmbProjector;
    private JComboBox<TimeSlot>  cmbTimeSlot;
    private JComboBox<String>    cmbLab;
    private JSpinner             spnDate;
    private JTextField           txtBookedBy;
    private JTextField           txtPurpose;
    private JLabel               lblStatus;
    private JTable               tblBookings;
    private DefaultTableModel    tableModel;
    private String               loggedInUser;

    public BookingPanel(String loggedInUser) {
        this.loggedInUser = loggedInUser;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(buildFormPanel(),  BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        loadProjectors();
        loadTimeSlots();
        loadLabs();
        refreshTable();
    }

    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(new Color(245, 248, 255));

        JLabel heading = new JLabel("  New Booking");
        heading.setFont(new Font("Arial", Font.BOLD, 14));
        heading.setForeground(new Color(13, 43, 85));
        heading.setOpaque(true);
        heading.setBackground(new Color(187, 222, 251));
        heading.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        outer.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createLineBorder(new Color(187, 222, 251), 1));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 12, 8, 12);
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;

        cmbProjector = new JComboBox<>();
        cmbTimeSlot  = new JComboBox<>();
        cmbLab       = new JComboBox<>();

        spnDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor de = new JSpinner.DateEditor(spnDate, "yyyy-MM-dd");
        spnDate.setEditor(de);
        spnDate.setValue(new Date());

        txtBookedBy = new JTextField(20);
        txtBookedBy.setText(loggedInUser);
        txtPurpose  = new JTextField(20);

        lblStatus = new JLabel("Fill in the form and click Confirm Booking.");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 12));
        lblStatus.setForeground(new Color(90, 90, 90));

        // Left column fields
        addField(form, g, 0, 0, "Projector:",  cmbProjector);
        addField(form, g, 1, 0, "Lab:",         cmbLab);
        addField(form, g, 2, 0, "Date:",         spnDate);
        addField(form, g, 3, 0, "Time Slot:",    cmbTimeSlot);
        addField(form, g, 4, 0, "Booked By:",    txtBookedBy);
        addField(form, g, 5, 0, "Purpose:",      txtPurpose);

        // Confirm button
        JButton btnConfirm = makeButton("✔  Confirm Booking", new Color(21, 101, 192));
        btnConfirm.addActionListener(e -> handleBooking());
        g.gridx=0; g.gridy=6; g.gridwidth=2; g.insets=new Insets(12,12,8,12);
        form.add(btnConfirm, g);

        g.gridy=7; g.insets=new Insets(0,12,10,12);
        form.add(lblStatus, g);

        outer.add(form, BorderLayout.CENTER);
        return outer;
    }

    private void addField(JPanel p, GridBagConstraints g, int row, int startCol, String label, JComponent comp) {
        g.gridwidth=1; g.gridx=startCol; g.gridy=row; g.insets=new Insets(8,12,8,6);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(new Color(40, 60, 100));
        p.add(lbl, g);
        g.gridx=startCol+1; g.insets=new Insets(8,0,8,12);
        p.add(comp, g);
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(new Color(245, 248, 255));

        JLabel heading = new JLabel("  All Bookings");
        heading.setFont(new Font("Arial", Font.BOLD, 14));
        heading.setForeground(new Color(13, 43, 85));
        heading.setOpaque(true);
        heading.setBackground(new Color(187, 222, 251));
        heading.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        String[] cols = {"ID","Projector","Lab","Date","Time Slot","Booked By","Purpose"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblBookings = new JTable(tableModel);
        tblBookings.setRowHeight(26);
        tblBookings.setFont(new Font("Arial", Font.PLAIN, 12));
        tblBookings.setGridColor(new Color(220, 230, 245));
        tblBookings.getTableHeader().setBackground(new Color(21, 101, 192));
        tblBookings.getTableHeader().setForeground(Color.WHITE);
        tblBookings.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblBookings.setSelectionBackground(new Color(187, 222, 251));

        // Hide ID column visually but keep data
        tblBookings.getColumnModel().getColumn(0).setMinWidth(0);
        tblBookings.getColumnModel().getColumn(0).setMaxWidth(0);
        tblBookings.getColumnModel().getColumn(0).setWidth(0);

        JButton btnCancel = makeButton("✖  Cancel Selected Booking", new Color(183, 28, 28));
        btnCancel.addActionListener(e -> handleCancel());

        panel.add(heading,                   BorderLayout.NORTH);
        panel.add(new JScrollPane(tblBookings), BorderLayout.CENTER);
        panel.add(btnCancel,                 BorderLayout.SOUTH);
        return panel;
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadProjectors() {
        cmbProjector.removeAllItems();
        for (Projector p : new ProjectorDAO().getAllActiveProjectors())
            cmbProjector.addItem(p);
    }

    private void loadTimeSlots() {
        cmbTimeSlot.removeAllItems();
        for (TimeSlot t : new ProjectorDAO().getAllTimeSlots())
            cmbTimeSlot.addItem(t);
    }

    private void loadLabs() {
        cmbLab.removeAllItems();
        for (String lab : new BookingDAO().getAllLabs())
            cmbLab.addItem(lab);
    }

    private void handleBooking() {
        Projector proj    = (Projector) cmbProjector.getSelectedItem();
        TimeSlot  slot    = (TimeSlot)  cmbTimeSlot.getSelectedItem();
        String    lab     = (String)    cmbLab.getSelectedItem();
        String    bookedBy = txtBookedBy.getText().trim();
        String    purpose  = txtPurpose.getText().trim();

        if (proj == null || slot == null || lab == null || bookedBy.isEmpty()) {
            lblStatus.setText("Please fill in all fields.");
            lblStatus.setForeground(Color.RED);
            return;
        }

        String date = new SimpleDateFormat("yyyy-MM-dd").format((Date) spnDate.getValue());
        boolean ok = new BookingDAO().addBooking(
            proj.getProjectorId(), bookedBy, date, slot.getSlotId(), purpose, lab);

        if (ok) {
            lblStatus.setText("✔  Booking confirmed for " + proj.getName() + " in Lab " + lab + "!");
            lblStatus.setForeground(new Color(27, 94, 32));
            lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
            txtPurpose.setText("");
            refreshTable();
        } else {
            lblStatus.setText("✘  Slot already booked! Choose a different projector, date, or time.");
            lblStatus.setForeground(new Color(183, 28, 28));
            lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        }
    }

    private void handleCancel() {
        int row = tblBookings.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a booking row to cancel.", "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int bookingId = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Cancel this booking?\n\nProjector: " + tableModel.getValueAt(row, 1) +
            "\nLab: " + tableModel.getValueAt(row, 2) +
            "\nDate: "  + tableModel.getValueAt(row, 3) +
            "\nTime: "  + tableModel.getValueAt(row, 4),
            "Confirm Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (new BookingDAO().cancelBooking(bookingId)) {
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully.",
                    "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            }
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Booking b : new BookingDAO().getAllBookings()) {
            tableModel.addRow(new Object[]{
                b.getBookingId(), b.getProjectorName(), b.getLabName(),
                b.getBookingDate(), b.getSlotLabel(), b.getBookedBy(), b.getPurpose()
            });
        }
    }
}