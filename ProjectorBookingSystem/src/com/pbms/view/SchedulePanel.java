package com.pbms.view;

import com.pbms.database.BookingDAO;
import com.pbms.database.ProjectorDAO;
import com.pbms.model.Booking;
import com.pbms.model.Projector;
import com.pbms.model.TimeSlot;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class SchedulePanel extends JPanel {

    private JLabel            lblDate;
    private JTable            scheduleTable;
    private DefaultTableModel tableModel;
    private Calendar          currentDate;
    private List<Projector>   projectors;
    private List<TimeSlot>    slots;

    public SchedulePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        currentDate = Calendar.getInstance();
        add(buildTopBar(),  BorderLayout.NORTH);
        add(buildGrid(),    BorderLayout.CENTER);
        add(buildLegend(),  BorderLayout.SOUTH);
        loadSchedule();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(13, 43, 85));
        bar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        lblDate = new JLabel("", SwingConstants.CENTER);
        lblDate.setFont(new Font("Arial", Font.BOLD, 15));
        lblDate.setForeground(Color.WHITE);

        JButton btnPrev  = navButton("◀  Previous Day");
        JButton btnNext  = navButton("Next Day  ▶");
        JButton btnToday = navButton("Today");
        btnToday.setBackground(new Color(21, 101, 192));

        btnPrev.addActionListener(e  -> { currentDate.add(Calendar.DAY_OF_MONTH, -1); loadSchedule(); });
        btnNext.addActionListener(e  -> { currentDate.add(Calendar.DAY_OF_MONTH,  1); loadSchedule(); });
        btnToday.addActionListener(e -> { currentDate = Calendar.getInstance();        loadSchedule(); });

        JPanel left  = new JPanel(new FlowLayout(FlowLayout.LEFT,  8, 0)); left.setOpaque(false);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); right.setOpaque(false);
        left.add(btnPrev);
        right.add(btnToday);
        right.add(btnNext);

        bar.add(left,    BorderLayout.WEST);
        bar.add(lblDate, BorderLayout.CENTER);
        bar.add(right,   BorderLayout.EAST);
        return bar;
    }

    private JButton navButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setBackground(new Color(30, 60, 110));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel buildGrid() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(187, 222, 251), 1));

        projectors = new ProjectorDAO().getAllActiveProjectors();
        slots      = new ProjectorDAO().getAllTimeSlots();

        String[] colNames = new String[projectors.size() + 1];
        colNames[0] = "Time Slot";
        for (int i = 0; i < projectors.size(); i++)
            colNames[i + 1] = projectors.get(i).getName();

        tableModel = new DefaultTableModel(colNames, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        scheduleTable = new JTable(tableModel);
        scheduleTable.setRowHeight(38);
        scheduleTable.setFont(new Font("Arial", Font.PLAIN, 12));
        scheduleTable.setShowGrid(true);
        scheduleTable.setGridColor(new Color(210, 225, 245));
        scheduleTable.setDefaultRenderer(Object.class, new ScheduleCellRenderer());

        JTableHeader header = scheduleTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(new Color(13, 43, 85));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));

        JScrollPane scroll = new JScrollPane(scheduleTable);
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildLegend() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        legend.setBackground(new Color(245, 248, 255));
        legend.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(187, 222, 251)));

        legend.add(new JLabel("Legend: "));
        legend.add(legendBadge("  FREE  ",     new Color(56, 142, 60),  Color.WHITE));
        legend.add(legendBadge("  BOOKED  ",   new Color(211, 47, 47),  Color.WHITE));
        legend.add(legendBadge("  Time Slot ", new Color(13, 43, 85),   Color.WHITE));
        return legend;
    }

    private JLabel legendBadge(String text, Color bg, Color fg) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setOpaque(true);
        lbl.setBackground(bg);
        lbl.setForeground(fg);
        lbl.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        return lbl;
    }

    public void loadSchedule() {
        SimpleDateFormat sdf     = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat display = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        String dateStr = sdf.format(currentDate.getTime());
        lblDate.setText("Schedule for:  " + display.format(currentDate.getTime()));

        Map<String, Booking> map = new HashMap<>();
        for (Booking b : new BookingDAO().getBookingsForDate(dateStr))
            map.put(b.getProjectorId() + "_" + b.getSlotId(), b);

        tableModel.setRowCount(0);
        for (TimeSlot slot : slots) {
            Object[] row = new Object[projectors.size() + 1];
            row[0] = slot.getLabel();
            for (int i = 0; i < projectors.size(); i++) {
                String  key = projectors.get(i).getProjectorId() + "_" + slot.getSlotId();
                Booking b   = map.get(key);
                // Show: "Lab C3 | Mr. Sharma" or just "FREE"
                row[i + 1] = (b != null)
                    ? (b.getLabName() != null ? "Lab " + b.getLabName() : "") + " | " + b.getBookedBy()
                    : "FREE";
            }
            tableModel.addRow(row);
        }
    }

    static class ScheduleCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col) {

            Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, col);

            String text = value != null ? value.toString() : "";
            ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);

            if (col == 0) {
                // Time slot column
                c.setBackground(new Color(13, 43, 85));
                c.setForeground(Color.WHITE);
                ((JLabel) c).setFont(new Font("Arial", Font.BOLD, 12));
            } else if (text.equals("FREE")) {
                c.setBackground(new Color(200, 230, 201));   // soft green
                c.setForeground(new Color(27, 94, 32));
                ((JLabel) c).setFont(new Font("Arial", Font.BOLD, 12));
                ((JLabel) c).setToolTipText("Available");
            } else {
                c.setBackground(new Color(255, 205, 210));   // soft red/pink
                c.setForeground(new Color(183, 28, 28));
                ((JLabel) c).setFont(new Font("Arial", Font.BOLD, 11));
                ((JLabel) c).setToolTipText(text);
            }

            if (isSelected) {
                c.setBackground(new Color(144, 202, 249));
                c.setForeground(Color.BLACK);
            }
            return c;
        }
    }
}