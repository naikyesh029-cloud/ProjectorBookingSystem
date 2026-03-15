package com.pbms.view;

import com.pbms.database.ProjectorDAO;
import com.pbms.model.Projector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel extends JPanel {

    private JTable tblProjectors;
    private DefaultTableModel tableModel;

    public AdminPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        add(buildAddForm(), BorderLayout.NORTH);
        add(buildProjectorList(), BorderLayout.CENTER);
        refreshTable();
    }

    private JPanel buildAddForm() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p.setBorder(BorderFactory.createTitledBorder("Add New Projector"));
        JTextField txtId   = new JTextField(8);
        JTextField txtName = new JTextField(15);
        JTextField txtLoc  = new JTextField(15);
        JButton    btnAdd  = new JButton("Add Projector");
        btnAdd.setBackground(new Color(21,101,192));
        btnAdd.setForeground(Color.WHITE);

        p.add(new JLabel("ID:")); p.add(txtId);
        p.add(new JLabel("Name:")); p.add(txtName);
        p.add(new JLabel("Location:")); p.add(txtLoc);
        p.add(btnAdd);

        btnAdd.addActionListener(e -> {
            if (txtId.getText().trim().isEmpty() || txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Name are required."); return;
            }
            boolean ok = new ProjectorDAO().addProjector(
                txtId.getText().trim(), txtName.getText().trim(), txtLoc.getText().trim());
            if (ok) { JOptionPane.showMessageDialog(this, "Projector added!"); refreshTable(); }
            else    { JOptionPane.showMessageDialog(this, "Failed. ID may already exist."); }
        });
        return p;
    }

    private JPanel buildProjectorList() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("All Projectors"));
        String[] cols = {"ID","Name","Location","Active"};
        tableModel = new DefaultTableModel(cols,0) {
            public boolean isCellEditable(int r,int c){return false;}
        };
        tblProjectors = new JTable(tableModel);
        tblProjectors.setRowHeight(24);
        JButton btnDeactivate = new JButton("Deactivate Selected");
        btnDeactivate.setBackground(new Color(183,28,28));
        btnDeactivate.setForeground(Color.WHITE);
        btnDeactivate.addActionListener(e -> {
            int row = tblProjectors.getSelectedRow();
            if (row<0){JOptionPane.showMessageDialog(this,"Select a projector.");return;}
            String id = (String) tableModel.getValueAt(row,0);
            int c = JOptionPane.showConfirmDialog(this,"Deactivate projector "+id+"?",
                "Confirm",JOptionPane.YES_NO_OPTION);
            if(c==JOptionPane.YES_OPTION){new ProjectorDAO().deactivateProjector(id);refreshTable();}
        });
        p.add(new JScrollPane(tblProjectors), BorderLayout.CENTER);
        p.add(btnDeactivate, BorderLayout.SOUTH);
        return p;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Projector p : new ProjectorDAO().getAllProjectors())
            tableModel.addRow(new Object[]{p.getProjectorId(),p.getName(),p.getLocation(),p.isActive()?"Yes":"No"});
    }
}
