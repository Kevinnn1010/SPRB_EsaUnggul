package db;

import exc.JadwalBentrokException;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class Database {
    private static final String USERS_FILE = "data/users.dat";
    private static final String RUANG_FILE = "data/ruang.dat";
    private static final String PEMINJAMAN_FILE = "data/peminjaman.dat";
    
    private List<User> users;
    private List<Ruang> ruangList;
    private List<Peminjaman> peminjamanList;
    
    private static Database instance;
    
    private Database() {
        users = new ArrayList<>();
        ruangList = new ArrayList<>();
        peminjamanList = new ArrayList<>();
        loadData();
        initializeSampleData();
    }
    
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    
    private void loadData() {
        try {
            // Load users
            File usersFile = new File(USERS_FILE);
            if (usersFile.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usersFile));
                users = (List<User>) ois.readObject();
                ois.close();
            }
            
            // Load ruang
            File ruangFile = new File(RUANG_FILE);
            if (ruangFile.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruangFile));
                ruangList = (List<Ruang>) ois.readObject();
                ois.close();
            }
            
            // Load peminjaman
            File peminjamanFile = new File(PEMINJAMAN_FILE);
            if (peminjamanFile.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(peminjamanFile));
                peminjamanList = (List<Peminjaman>) ois.readObject();
                ois.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void saveData() {
        try {
            // Buat folder data jika belum ada
            new File("data").mkdirs();
            
            // Save users
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE));
            oos.writeObject(users);
            oos.close();
            
            // Save ruang
            oos = new ObjectOutputStream(new FileOutputStream(RUANG_FILE));
            oos.writeObject(ruangList);
            oos.close();
            
            // Save peminjaman
            oos = new ObjectOutputStream(new FileOutputStream(PEMINJAMAN_FILE));
            oos.writeObject(peminjamanList);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initializeSampleData() {
        // Inisialisasi data sample jika database kosong
        if (users.isEmpty()) {
            users.add(new Admin("ADM001", "Admin Utama", "admin@esaunggul.ac.id", "admin123"));
            users.add(new Dosen("DSN001", "Ryan Putra Laksana, S.Kom., M.M.", "ryanpl@esaunggul.ac.id", "dosen123", "8362"));
            users.add(new Mahasiswa("MHS001", "Kevin Yulian Pamungkas", "kevinyp@student.esaunggul.ac.id", "mahasiswa123", "20240801273", "Teknik Informatika"));
            saveData();
        }
        
        if (ruangList.isEmpty()) {
            ruangList.add(new Ruang("LAB01", "Lab Komputer 1", "Laboratorium", 40, "Komputer, Proyektor, AC"));
            ruangList.add(new Ruang("LAB02", "Lab Komputer 2", "Laboratorium", 40, "Komputer, Proyektor, AC"));
            ruangList.add(new Ruang("CR301", "Ruang Kelas 301", "Kelas", 50, "Proyektor, Papan Tulis, AC"));
            ruangList.add(new Ruang("CR302", "Ruang Kelas 302", "Kelas", 50, "Proyektor, Papan Tulis, AC"));
            ruangList.add(new Ruang("DISK01", "Ruang Diskusi 1", "Diskusi", 15, "Whiteboard, TV, Sofa"));
            ruangList.add(new Ruang("DISK02", "Ruang Diskusi 2", "Diskusi", 20, "Whiteboard, TV, Meja Rapat"));
            saveData();
        }
    }
    
    // User operations
    public User authenticate(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    
    public void addUser(User user) {
        users.add(user);
        saveData();
    }
    
    // Ruang operations
    public List<Ruang> getAllRuang() {
        return new ArrayList<>(ruangList);
    }
    
    public Ruang findRuangByKode(String kode) {
        for (Ruang ruang : ruangList) {
            if (ruang.getKode().equals(kode)) {
                return ruang;
            }
        }
        return null;
    }
    
    // Peminjaman operations
    public void addPeminjaman(Peminjaman peminjaman) throws JadwalBentrokException {
        // Cek bentrok jadwal
        for (Peminjaman p : peminjamanList) {
            if (p.getRuang().getKode().equals(peminjaman.getRuang().getKode()) &&
                p.getTanggal().equals(peminjaman.getTanggal()) &&
                p.getStatus() != StatusPeminjaman.DITOLAK) {
                
                // Cek overlap waktu
                if (isTimeOverlap(p.getJamMulai(), p.getJamSelesai(), 
                                 peminjaman.getJamMulai(), peminjaman.getJamSelesai())) {
                    throw new JadwalBentrokException(
                        "Ruang " + peminjaman.getRuang().getNama() + 
                        " sudah dipesan pada jam " + p.getJamMulai() + "-" + p.getJamSelesai()
                    );
                }
            }
        }
        
        peminjamanList.add(peminjaman);
        saveData();
    }
    
    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, 
                                  LocalTime start2, LocalTime end2) {
        return !(end1.isBefore(start2) || end2.isBefore(start1));
    }
    
    public List<Peminjaman> getPeminjamanByUser(User user) {
        List<Peminjaman> result = new ArrayList<>();
        for (Peminjaman p : peminjamanList) {
            if (p.getPeminjam().getId().equals(user.getId())) {
                result.add(p);
            }
        }
        return result;
    }
    
    public List<Peminjaman> getAllPeminjaman() {
        return new ArrayList<>(peminjamanList);
    }
    
    public List<Peminjaman> getPeminjamanByStatus(StatusPeminjaman status) {
        List<Peminjaman> result = new ArrayList<>();
        for (Peminjaman p : peminjamanList) {
            if (p.getStatus() == status) {
                result.add(p);
            }
        }
        return result;
    }
    
    public void updatePeminjamanStatus(String id, StatusPeminjaman status, String catatan) {
        for (Peminjaman p : peminjamanList) {
            if (p.getId().equals(id)) {
                p.setStatus(status);
                p.setCatatanAdmin(catatan);
                saveData();
                break;
            }
        }
    }
    
    public List<Peminjaman> getJadwalHarian(LocalDate tanggal) {
        List<Peminjaman> result = new ArrayList<>();
        for (Peminjaman p : peminjamanList) {
            if (p.getTanggal().equals(tanggal) && 
                (p.getStatus() == StatusPeminjaman.DISETUJUI || 
                 p.getStatus() == StatusPeminjaman.MENUNGGU)) {
                result.add(p);
            }
        }
        return result;
    }
}