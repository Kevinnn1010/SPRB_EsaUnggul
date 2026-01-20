# Sistem Peminjaman Ruang Belajar
## Universitas Esa Unggul

---

## Deskripsi
Sistem Peminjaman Ruang Belajar merupakan aplikasi desktop berbasis Java yang dikembangkan untuk mengelola peminjaman ruang belajar seperti laboratorium, ruang kelas, dan ruang diskusi di Universitas Esa Unggul. Aplikasi ini dirancang untuk menggantikan proses manual agar lebih terstruktur, menghindari bentrok jadwal, serta memudahkan pencatatan dan pemantauan riwayat peminjaman.

---

## Fitur Utama
1. **Autentikasi Pengguna**: Sistem mendukung tiga jenis pengguna, yaitu Admin, Dosen, dan Mahasiswa.  
2. **Peminjaman Ruang**: Mahasiswa dan Dosen dapat mengajukan permohonan peminjaman ruang belajar sesuai jadwal yang diinginkan.  
3. **Persetujuan Admin**: Admin dapat menyetujui atau menolak permohonan peminjaman beserta catatan.  
4. **Pengecekan Bentrok Jadwal**: Sistem secara otomatis mendeteksi dan menolak peminjaman apabila terjadi bentrok jadwal.  
5. **Riwayat Peminjaman**: Setiap pengguna dapat melihat riwayat peminjaman ruang yang pernah dilakukan.  
6. **Jadwal Penggunaan Ruang**: Menampilkan jadwal penggunaan ruang berdasarkan hari atau rentang waktu tertentu.  

---

## Konsep Pemrograman Berorientasi Objek (PBO)
Aplikasi ini menerapkan konsep-konsep Pemrograman Berorientasi Objek sebagai berikut:
- **Encapsulation**: Seluruh atribut class bersifat `private` dan diakses melalui method getter dan setter.  
- **Inheritance**: Class `User` digunakan sebagai superclass dengan turunan `Admin`, `Dosen`, dan `Mahasiswa`.  
- **Polymorphism**: Method seperti `getDashboardTitle()` dan `getAvailableActions()` dioverride untuk membedakan perilaku setiap jenis pengguna.  
- **Exception Handling**: Menggunakan custom exception `JadwalBentrokException` untuk menangani konflik jadwal peminjaman.  
- **Collection Framework**: Menggunakan `ArrayList` untuk menyimpan data pengguna, ruang, dan peminjaman.  
- **Java GUI**: Antarmuka grafis dibangun menggunakan Java Swing.  

---

## Struktur Folder Proyek
SPRB_EsaUnggul/
├── src/
│ ├── model/
│ ├── view/
│ ├── db/
│ └── exc/
├── data/
│ ├── users.dat
│ ├── ruang.dat
│ └── peminjaman.dat
├── bin/
└── README.md

---

## Daftar User Saat Ini (Untuk login ke sistem)
Admin:
    Username: Admin Utama
    Email: admin@esaunggul.ac.id
    Password: admin123

Dosen:
    Username: Ryan Putra Laksana, S.Kom., M.M.
    Email: ryanpl@esaunggul.ac.id
    Password: dosen123

Admin:
    Username: Kevin Yulian Pamungkas
    Email: kevinyp@student.esaunggul.ac.id
    Password: mahasiswa123

---

## Cara Menjalankan Aplikasi
Pastikan Java Development Kit (JDK) versi 8 atau lebih tinggi telah terinstal dan variabel lingkungan Java telah dikonfigurasi dengan benar.

1. **Cek Versi Java**
java -version

2. **Kompilasi Program**  
Jalankan perintah berikut dari direktori utama proyek:
javac -d bin -encoding UTF8 src/model/.java src/db/.java src/exc/.java src/view/.java

Catatan: Pesan `uses unchecked or unsafe operations` merupakan warning dan tidak memengaruhi jalannya aplikasi.

3. **Menjalankan Aplikasi**
java -cp bin view.MainApp

4. **Reset Data (Opsional)**  
Jika ingin mengembalikan data ke kondisi awal (misalnya setelah mengubah akun default di kode), hapus file berikut:
data/users.dat
data/ruang.dat
data/peminjaman.dat

Kemudian jalankan kembali aplikasi.

---

## Penutup
Aplikasi ini dikembangkan sebagai studi kasus penerapan Pemrograman Berorientasi Objek pada aplikasi desktop berbasis Java. Dengan desain yang modular dan penerapan prinsip PBO, sistem ini diharapkan mudah dikembangkan serta dipelihara di masa mendatang.