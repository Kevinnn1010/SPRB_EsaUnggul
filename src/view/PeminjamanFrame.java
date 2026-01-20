package view;

import model.*;
import db.Database;
import exc.JadwalBentrokException;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PeminjamanFrame extends JFrame {
    private User currentUser;
    private UserDashboard parent;
    
    private JComboBox<String> cbRuang;
    private JTextField txtTanggal;
    private JTextField txtJamMulai;
    private JTextField txtJamSelesai;
    private JTextArea txtKeperluan;
    private JButton btnSubmit;
    private JButton btnCancel;
    
    public PeminjamanFrame(User user, UserDashboard parent) {
        this.currentUser = user;
        this.parent = parent;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Form Peminjaman Ruang");
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Label dan field untuk Ruang
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Pilih Ruang:"), gbc);
        
        gbc.gridx = 1;
        cbRuang = new JComboBox<>();
        loadRuangOptions();
        formPanel.add(cbRuang, gbc);
        
        // Tanggal
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        txtTanggal = new JTextField(LocalDate.now().toString(), 15);
        formPanel.add(txtTanggal, gbc);
        
        // Jam Mulai
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Jam Mulai (HH:MM):"), gbc);
        
        gbc.gridx = 1;
        txtJamMulai = new JTextField("08:00", 15);
        formPanel.add(txtJamMulai, gbc);
        
        // Jam Selesai
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Jam Selesai (HH:MM):"), gbc);
        
        gbc.gridx = 1;
        txtJamSelesai = new JTextField("10:00", 15);
        formPanel.add(txtJamSelesai, gbc);
        
        // Keperluan
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Keperluan:"), gbc);
        
        gbc.gridx = 1;
        txtKeperluan = new JTextArea(3, 15);
        txtKeperluan.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(txtKeperluan);
        formPanel.add(scrollPane, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSubmit = new JButton("Submit");
        btnCancel = new JButton("Cancel");
        
        btnSubmit.addActionListener(e -> submitPeminjaman());
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSubmit);
        
        // Panel info
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("Info Format"));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel lblInfo1 = new JLabel("Format tanggal: YYYY-MM-DD (contoh: 2024-01-20)");
        JLabel lblInfo2 = new JLabel("Format jam: HH:MM (contoh: 09:00)");
        JLabel lblInfo3 = new JLabel("Jam operasional: 08:00 - 20:00");
        
        lblInfo1.setFont(new Font("Arial", Font.PLAIN, 10));
        lblInfo2.setFont(new Font("Arial", Font.PLAIN, 10));
        lblInfo3.setFont(new Font("Arial", Font.PLAIN, 10));
        
        infoPanel.add(lblInfo1);
        infoPanel.add(lblInfo2);
        infoPanel.add(lblInfo3);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.CENTER);
        container.add(infoPanel, BorderLayout.SOUTH);
        
        add(container);
    }
    
    private void loadRuangOptions() {
        try {
            Database db = Database.getInstance();
            for (Ruang ruang : db.getAllRuang()) {
                cbRuang.addItem(ruang.getKode() + " - " + ruang.getNama() + 
                               " (" + ruang.getJenis() + ")");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void submitPeminjaman() {
        try {
            // Validasi input
            if (cbRuang.getSelectedIndex() == -1) {
                throw new IllegalArgumentException("Pilih ruang terlebih dahulu!");
            }
            
            String ruangInfo = (String) cbRuang.getSelectedItem();
            String kodeRuang = ruangInfo.split(" - ")[0];
            
            LocalDate tanggal = LocalDate.parse(txtTanggal.getText().trim());
            LocalTime jamMulai = LocalTime.parse(txtJamMulai.getText().trim());
            LocalTime jamSelesai = LocalTime.parse(txtJamSelesai.getText().trim());
            String keperluan = txtKeperluan.getText().trim();
            
            if (keperluan.isEmpty()) {
                throw new IllegalArgumentException("Keperluan harus diisi!");
            }
            
            if (jamMulai.isAfter(jamSelesai)) {
                throw new IllegalArgumentException("Jam mulai harus sebelum jam selesai!");
            }
            
            if (jamMulai.isBefore(LocalTime.of(8, 0)) || 
                jamSelesai.isAfter(LocalTime.of(20, 0))) {
                throw new IllegalArgumentException("Jam peminjaman harus antara 08:00 - 20:00!");
            }
            
            // Buat peminjaman
            Database db = Database.getInstance();
            Ruang ruang = db.findRuangByKode(kodeRuang);
            
            String id = "P" + System.currentTimeMillis();
            Peminjaman peminjaman = new Peminjaman(
                id, currentUser, ruang, tanggal, jamMulai, jamSelesai, keperluan
            );
            
            // Tambahkan ke database
            db.addPeminjaman(peminjaman);
            
            JOptionPane.showMessageDialog(this, 
                "Peminjaman berhasil diajukan!\nID: " + id + 
                "\nMenunggu persetujuan admin.", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (JadwalBentrokException e) {
            JOptionPane.showMessageDialog(this, 
                "Gagal mengajukan peminjaman: " + e.getMessage(), 
                "Jadwal Bentrok", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
