package model;

public class Dosen extends User {
    private String nip;

    public Dosen(String id, String nama, String email, String password, String nip) {
        super(id, nama, email, password, "Dosen");
        this.nip = nip;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    @Override
    public String getDashboardTitle() {
        return "Dashboard Dosen - Universitas Esa Unggul";
    }

    @Override
    public String[] getAvailableActions() {
        return new String[] {
            "Pinjam Ruang",
            "Lihat Jadwal",
            "Lihat Riwayat"
        };
    }
}