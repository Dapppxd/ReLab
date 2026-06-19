# SiPela (Sistem Peminjaman Laboratorium)

SiPela adalah aplikasi Android berbasis Kotlin yang dirancang untuk mengelola ketersediaan dan peminjaman ruang laboratorium secara *real-time*. Aplikasi ini menyederhanakan proses peminjaman dengan menghilangkan sistem pengisian formulir manual, digantikan dengan sistem *one-click booking* berdasarkan slot jadwal yang telah ditetapkan oleh Admin.

## Fitur Utama

Aplikasi ini membagi hak akses ke dalam dua *role* pengguna:

**1. Hak Akses Admin**
* **Manajemen Ketersediaan:** Admin dapat menambahkan jadwal lab (nama lab, tanggal, waktu) ke dalam sistem.
* **Auto-Routing:** Admin memiliki halaman *dashboard* khusus yang terpisah dari mahasiswa.

**2. Hak Akses Mahasiswa**
* **Daftar Ketersediaan Terkini:** Mahasiswa hanya melihat jadwal lab yang berstatus "Tersedia".
* **One-Click Booking:** Pemesanan lab dilakukan hanya dengan satu klik konfirmasi. Status lab akan otomatis berpindah tangan dan berubah menjadi "Dipesan".
* **Riwayat Peminjaman:** Mahasiswa dapat melacak daftar lab yang sudah berhasil mereka *booking* di tab khusus.

## Stack Teknologi
* **Bahasa Pemrograman:** Kotlin
* **Antarmuka Pengguna:** XML, Material Design (Bottom Navigation, CardView)
* **Autentikasi:** Firebase Authentication (Email & Password)
* **Database:** Firebase Realtime Database
* **Navigasi:** Android Jetpack Navigation Component

## Cara Instalasi (Lokal)

1. Lakukan *clone* repository ini ke lokal:
   ```bash
   git clone [https://github.com/](https://github.com/)[USERNAME-GITHUB-KAMU]/[NAMA-REPO-KAMU].git
