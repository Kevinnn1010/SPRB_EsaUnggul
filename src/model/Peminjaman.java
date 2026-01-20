package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Peminjaman implements Serializable {
    private String id;
    private User peminjam;
    private Ruang ruang;
    private LocalDate tanggal;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String keperluan;
    private StatusPeminjaman status;
    private String catatanAdmin;

    public Peminjaman(String id, User peminjam, Ruang ruang, LocalDate tanggal, 
                     LocalTime jamMulai, LocalTime jamSelesai, String keperluan) {
        this.id = id;
        this.peminjam = peminjam;
        this.ruang = ruang;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.keperluan = keperluan;
        this.status = StatusPeminjaman.MENUNGGU;
        this.catatanAdmin = "";
    }

    // Getter dan Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getPeminjam() {
        return peminjam;
    }

    public void setPeminjam(User peminjam) {
        this.peminjam = peminjam;
    }

    public Ruang getRuang() {
        return ruang;
    }

    public void setRuang(Ruang ruang) {
        this.ruang = ruang;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public LocalTime getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(LocalTime jamMulai) {
        this.jamMulai = jamMulai;
    }

    public LocalTime getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public String getKeperluan() {
        return keperluan;
    }

    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }

    public StatusPeminjaman getStatus() {
        return status;
    }

    public void setStatus(StatusPeminjaman status) {
        this.status = status;
    }

    public String getCatatanAdmin() {
        return catatanAdmin;
    }

    public void setCatatanAdmin(String catatanAdmin) {
        this.catatanAdmin = catatanAdmin;
    }

    @Override
    public String toString() {
        return "Peminjaman [" + id + "] - " + ruang.getNama() + 
               " - " + tanggal + " " + jamMulai + "-" + jamSelesai;
    }
}