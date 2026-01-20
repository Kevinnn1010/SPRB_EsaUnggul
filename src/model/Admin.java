package model;

public class Admin extends User {
    public Admin(String id, String nama, String email, String password) {
        super(id, nama, email, password, "Admin");
    }

    @Override
    public String getDashboardTitle() {
        return "Dashboard Admin - Universitas Esa Unggul";
    }

    @Override
    public String[] getAvailableActions() {
        return new String[] {
            "Lihat Jadwal",
            "Lihat Riwayat"
        };
    }
}