package view;

import db.Database;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;

public class RiwayatFrame extends JFrame {
    private User currentUser;
    private JTable tableRiwayat;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbFilter;
    
    public RiwayatFrame(User user) {
        this.currentUser = user;
        initComponents();
        loadAllRiwayat();
    }
    
    private void initComponents() {
        setTitle("Riwayat Peminjaman");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter Status:"));
        
        cbFilter = new JComboBox<>(new String[]{"Semua", "Menunggu", "Disetujui", "Ditolak", "Selesai"});
        cbFilter.addActionListener(e -> filterRiwayat());
        filterPanel.add(cbFilter);
        
        // Table untuk riwayat
        String[] columns = {"ID", "Peminjam", "Ruang", "Tanggal", "Jam", "Keperluan", "Status", "Catatan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableRiwayat = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableRiwayat);
        
        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void loadAllRiwayat() {
        try {
            Database db = Database.getInstance();
            List<Peminjaman> riwayat;
            
            if (currentUser.getRole().equals("Admin")) {
                riwayat = db.getAllPeminjaman();
            } else {
                riwayat = db.getPeminjamanByUser(currentUser);
            }
            
            tableModel.setRowCount(0);
            
            for (Peminjaman p : riwayat) {
                Object[] row = {
                    p.getId(),
                    p.getPeminjam().getNama() + " (" + p.getPeminjam().getRole() + ")",
                    p.getRuang().getNama(),
                    p.getTanggal().toString(),
                    p.getJamMulai() + " - " + p.getJamSelesai(),
                    p.getKeperluan(),
                    p.getStatus().getDeskripsi(),
                    p.getCatatanAdmin()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void filterRiwayat() {
        String filter = (String) cbFilter.getSelectedItem();
        if (filter.equals("Semua")) {
            loadAllRiwayat();
            return;
        }
        
        StatusPeminjaman status;
        switch (filter) {
            case "Menunggu": status = StatusPeminjaman.MENUNGGU; break;
            case "Disetujui": status = StatusPeminjaman.DISETUJUI; break;
            case "Ditolak": status = StatusPeminjaman.DITOLAK; break;
            case "Selesai": status = StatusPeminjaman.SELESAI; break;
            default: return;
        }
        
        try {
            Database db = Database.getInstance();
            List<Peminjaman> filtered = db.getPeminjamanByStatus(status);
            
            tableModel.setRowCount(0);
            
            for (Peminjaman p : filtered) {
                Object[] row = {
                    p.getId(),
                    p.getPeminjam().getNama() + " (" + p.getPeminjam().getRole() + ")",
                    p.getRuang().getNama(),
                    p.getTanggal().toString(),
                    p.getJamMulai() + " - " + p.getJamSelesai(),
                    p.getKeperluan(),
                    p.getStatus().getDeskripsi(),
                    p.getCatatanAdmin()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
