package view;

import model.*;
import db.Database;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JadwalFrame extends JFrame {
    private User currentUser;
    private JTable tableJadwal;
    private DefaultTableModel tableModel;
    private JTextField txtTanggal;
    
    public JadwalFrame(User user) {
        this.currentUser = user;
        initComponents();
        loadJadwalHarian(LocalDate.now());
    }
    
    private void initComponents() {
        setTitle("Jadwal Peminjaman Ruang");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Tanggal:"));
        
        txtTanggal = new JTextField(LocalDate.now().toString(), 10);
        filterPanel.add(txtTanggal);
        
        JButton btnCari = new JButton("Cari");
        btnCari.addActionListener(e -> {
            try {
                LocalDate tanggal = LocalDate.parse(txtTanggal.getText().trim());
                loadJadwalHarian(tanggal);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Format tanggal salah! Gunakan format YYYY-MM-DD", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        filterPanel.add(btnCari);
        
        JButton btnHariIni = new JButton("Hari Ini");
        btnHariIni.addActionListener(e -> {
            txtTanggal.setText(LocalDate.now().toString());
            loadJadwalHarian(LocalDate.now());
        });
        filterPanel.add(btnHariIni);
        
        JButton btnBesok = new JButton("Besok");
        btnBesok.addActionListener(e -> {
            LocalDate besok = LocalDate.now().plusDays(1);
            txtTanggal.setText(besok.toString());
            loadJadwalHarian(besok);
        });
        filterPanel.add(btnBesok);
        
        // Table untuk jadwal
        String[] columns = {"Ruang", "Jenis", "Kapasitas", "Peminjam", "Jam", "Keperluan", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableJadwal = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableJadwal);
        
        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void loadJadwalHarian(LocalDate tanggal) {
        try {
            Database db = Database.getInstance();
            List<Peminjaman> jadwal = db.getJadwalHarian(tanggal);
            
            tableModel.setRowCount(0);
            
            for (Peminjaman p : jadwal) {
                Object[] row = {
                    p.getRuang().getNama(),
                    p.getRuang().getJenis(),
                    p.getRuang().getKapasitas(),
                    p.getPeminjam().getNama() + " (" + p.getPeminjam().getRole() + ")",
                    p.getJamMulai() + " - " + p.getJamSelesai(),
                    p.getKeperluan(),
                    p.getStatus().getDeskripsi()
                };
                tableModel.addRow(row);
            }
            
            setTitle("Jadwal Peminjaman - " + 
                     tanggal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
