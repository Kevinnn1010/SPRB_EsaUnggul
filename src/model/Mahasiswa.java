package model;

public class Mahasiswa extends User {
    private String nim;
    private String jurusan;

    public Mahasiswa(String id, String nama, String email, String password, String nim, String jurusan) {
        super(id, nama, email, password, "Mahasiswa");
        this.nim = nim;
        this.jurusan = jurusan;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    @Override
    public String getDashboardTitle() {
        return "Dashboard Mahasiswa - Universitas Esa Unggul";
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