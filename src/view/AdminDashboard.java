package view;

import model.*;
import db.Database;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class AdminDashboard extends JFrame {
    private User currentUser;
    private JTable tablePeminjaman;
    private DefaultTableModel tableModel;
    
    public AdminDashboard(User user) {
        this.currentUser = user;
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle(currentUser.getDashboardTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblWelcome = new JLabel("Selamat Datang, " + currentUser.getNama());
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> logout());
        
        headerPanel.add(lblWelcome, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        
        // Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBackground(new Color(240, 240, 240));
        
        String[] actions = currentUser.getAvailableActions();
        for (String action : actions) {
            JButton btnAction = new JButton(action);
            btnAction.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnAction.setMaximumSize(new Dimension(180, 40));
            btnAction.addActionListener(e -> handleAction(action));
            sidebarPanel.add(btnAction);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        // Content area
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table untuk peminjaman
        String[] columns = {"ID", "Peminjam", "Ruang", "Tanggal", "Jam", "Keperluan", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablePeminjaman = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablePeminjaman);
        
        // Panel aksi untuk table
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSetuju = new JButton("Setujui");
        JButton btnTolak = new JButton("Tolak");
        JButton btnRefresh = new JButton("Refresh");
        
        btnSetuju.addActionListener(e -> approvePeminjaman());
        btnTolak.addActionListener(e -> rejectPeminjaman());
        btnRefresh.addActionListener(e -> loadData());
        
        actionPanel.add(btnSetuju);
        actionPanel.add(btnTolak);
        actionPanel.add(btnRefresh);
        
        contentPanel.add(new JLabel("Daftar Peminjaman Menunggu:"), BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Gabungkan semua panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void handleAction(String action) {
        switch (action) {
            case "Lihat Jadwal":
                new JadwalFrame(currentUser).setVisible(true);
                break;
            case "Lihat Riwayat":
                new RiwayatFrame(currentUser).setVisible(true);
                break;
        }
    }
    
    private void loadData() {
        try {
            Database db = Database.getInstance();
            tableModel.setRowCount(0);
            
            for (Peminjaman p : db.getPeminjamanByStatus(StatusPeminjaman.MENUNGGU)) {
                Object[] row = {
                    p.getId(),
                    p.getPeminjam().getNama() + " (" + p.getPeminjam().getRole() + ")",
                    p.getRuang().getNama(),
                    p.getTanggal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    p.getJamMulai() + " - " + p.getJamSelesai(),
                    p.getKeperluan(),
                    p.getStatus().getDeskripsi()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void approvePeminjaman() {
        int selectedRow = tablePeminjaman.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih peminjaman terlebih dahulu!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String catatan = JOptionPane.showInputDialog(this, "Masukkan catatan (opsional):");
        
        try {
            Database db = Database.getInstance();
            db.updatePeminjamanStatus(id, StatusPeminjaman.DISETUJUI, catatan != null ? catatan : "");
            loadData();
            JOptionPane.showMessageDialog(this, "Peminjaman berhasil disetujui!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rejectPeminjaman() {
        int selectedRow = tablePeminjaman.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih peminjaman terlebih dahulu!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String catatan = JOptionPane.showInputDialog(this, "Masukkan alasan penolakan:");
        
        if (catatan == null || catatan.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Alasan penolakan harus diisi!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Database db = Database.getInstance();
            db.updatePeminjamanStatus(id, StatusPeminjaman.DITOLAK, catatan);
            loadData();
            JOptionPane.showMessageDialog(this, "Peminjaman berhasil ditolak!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}