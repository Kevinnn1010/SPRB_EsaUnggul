package view;

import db.Database;
import java.awt.*;
import javax.swing.*;
import model.User;

public class LoginFrame extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    
    public LoginFrame() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Login - Sistem Peminjaman Ruang");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel lblTitle = new JLabel("Sistem Peminjaman Ruang Belajar");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel("Universitas Esa Unggul");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(lblSubtitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        formPanel.add(txtPassword, gbc);
        
        // Panel button
        JPanel buttonPanel = new JPanel();
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> login());
        buttonPanel.add(btnLogin);
        
        // Panel info akun
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Akun Demo"));
        
        JLabel lblAdmin = new JLabel("Admin: admin@esaunggul.ac.id / admin123");
        JLabel lblDosen = new JLabel("Dosen: ryanpl@esaunggul.ac.id / dosen123");
        JLabel lblMahasiswa = new JLabel("Mahasiswa: kevinyp@student.esaunggul.ac.id / mahasiswa123");
        
        lblAdmin.setFont(new Font("Arial", Font.PLAIN, 10));
        lblDosen.setFont(new Font("Arial", Font.PLAIN, 10));
        lblMahasiswa.setFont(new Font("Arial", Font.PLAIN, 10));
        
        infoPanel.add(lblAdmin);
        infoPanel.add(lblDosen);
        infoPanel.add(lblMahasiswa);
        
        // Gabungkan semua panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.CENTER);
        container.add(infoPanel, BorderLayout.SOUTH);
        
        add(container);
        
        // Enter key untuk login
        getRootPane().setDefaultButton(btnLogin);
    }
    
    private void login() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email dan password harus diisi!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Database db = Database.getInstance();
            User user = db.authenticate(email, password);
            
            if (user != null) {
                dispose();
                
                if (user.getRole().equals("Admin")) {
                    new AdminDashboard(user).setVisible(true);
                } else {
                    new UserDashboard(user).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Email atau password salah!", 
                    "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}