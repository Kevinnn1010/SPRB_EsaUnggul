package view;

import db.Database;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;

public class UserDashboard extends JFrame {

    private User currentUser;
    private JTable tableRiwayat;
    private DefaultTableModel tableModel;

    public UserDashboard(User user) {
        this.currentUser = user;
        initComponents();
        loadRiwayat();
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
        headerPanel.setBackground(new Color(39, 174, 96));
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

        // Table untuk riwayat
        String[] columns = {"ID", "Ruang", "Tanggal", "Jam", "Keperluan", "Status", "Catatan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableRiwayat = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableRiwayat);

        contentPanel.add(new JLabel("Riwayat Peminjaman Anda:"), BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Gabungkan semua panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void handleAction(String action) {
        switch (action) {
            case "Pinjam Ruang":
                new PeminjamanFrame(currentUser, this).setVisible(true);
                break;
            case "Lihat Jadwal":
                new JadwalFrame(currentUser).setVisible(true);
                break;
            case "Lihat Riwayat":
                new RiwayatFrame(currentUser).setVisible(true);
                break;
        }
    }

    private void loadRiwayat() {
        try {
            Database db = Database.getInstance();
            tableModel.setRowCount(0);

            for (Peminjaman p : db.getPeminjamanByUser(currentUser)) {
                Object[] row = {
                    p.getId(),
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

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
