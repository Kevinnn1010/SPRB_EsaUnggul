package model;

public enum StatusPeminjaman {
    MENUNGGU("Menunggu Persetujuan"),
    DISETUJUI("Disetujui"),
    DITOLAK("Ditolak"),
    SELESAI("Selesai");

    private final String deskripsi;

    StatusPeminjaman(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }
}