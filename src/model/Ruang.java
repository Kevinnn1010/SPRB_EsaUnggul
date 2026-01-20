package model;

import java.io.Serializable;

public class Ruang implements Serializable {
    private String kode;
    private String nama;
    private String jenis;
    private int kapasitas;
    private String fasilitas;

    public Ruang(String kode, String nama, String jenis, int kapasitas, String fasilitas) {
        this.kode = kode;
        this.nama = nama;
        this.jenis = jenis;
        this.kapasitas = kapasitas;
        this.fasilitas = fasilitas;
    }

    // Getter dan Setter
    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    @Override
    public String toString() {
        return nama + " (" + jenis + ") - Kapasitas: " + kapasitas;
    }
}