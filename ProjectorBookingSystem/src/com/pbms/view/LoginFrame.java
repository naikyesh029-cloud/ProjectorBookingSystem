package com.pbms.view;

import com.pbms.database.UserDAO;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JLabel         lblMessage;

    public LoginFrame() {
        setTitle("PBMS - Login");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // center on screen
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(13, 43, 85));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("Projector Booking System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        mainPanel.add(lblTitle, gbc);

        // Subtitle
        JLabel lblSub = new JLabel("College Laboratory", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(new Color(187, 222, 251));
        gbc.gridy=1;
        mainPanel.add(lblSub, gbc);

        // Username label + field
        gbc.gridwidth=1; gbc.gridy=2; gbc.gridx=0;
        JLabel lblU = new JLabel("Username:");
        lblU.setForeground(Color.WHITE);
        mainPanel.add(lblU, gbc);
        txtUsername = new JTextField(15);
        gbc.gridx=1;
        mainPanel.add(txtUsername, gbc);

        // Password label + field
        gbc.gridy=3; gbc.gridx=0;
        JLabel lblP = new JLabel("Password:");
        lblP.setForeground(Color.WHITE);
        mainPanel.add(lblP, gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx=1;
        mainPanel.add(txtPassword, gbc);

        // Login button
        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(21, 101, 192));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy=4; gbc.gridx=0; gbc.gridwidth=2;
        mainPanel.add(btnLogin, gbc);

        // Message label (for errors)
        lblMessage = new JLabel("", SwingConstants.CENTER);
        lblMessage.setForeground(new Color(255, 100, 100));
        gbc.gridy=5;
        mainPanel.add(lblMessage, gbc);

        add(mainPanel);

        // Login button action
        btnLogin.addActionListener(e -> handleLogin());
        // Also login on Enter key in password field
        txtPassword.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please enter username and password.");
            return;
        }

        UserDAO dao = new UserDAO();
        String role = dao.validateLogin(username, password);

        if (role != null) {
            this.dispose();  // close login window
            new DashboardFrame(username, role).setVisible(true);
        } else {
            lblMessage.setText("Invalid username or password.");
            txtPassword.setText("");
        }
    }
}
