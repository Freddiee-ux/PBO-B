# Laporan Implementasi CRUD Java menggunakan JDBC (BlueJ)


## 1. Tujuan

Program ini dibuat untuk melakukan operasi **CRUD (Create, Read, Update, Delete)** pada tabel **`buku`** di database **`perpustakaan`** menggunakan **Java JDBC** dan dijalankan melalui **BlueJ**.

---

## 2. Tools & Teknologi

* **Java (BlueJ)** sebagai IDE dan runtime program
* **MySQL** sebagai DBMS
* **MySQL Connector/J (.jar)** sebagai driver JDBC agar Java dapat terhubung ke MySQL

---

## 3. Struktur Database

Database: `perpustakaan`
Tabel: `buku`

Kolom utama:

* `id_buku` (INT, AUTO_INCREMENT, PRIMARY KEY)
* `judul` (VARCHAR)
* `pengarang` (VARCHAR)

---

## 4. Struktur Program (Class & Fungsinya)

### A. `DBConfig.java`

Berisi konfigurasi koneksi database:

* Nama driver: `com.mysql.cj.jdbc.Driver`
* URL database (host, port, nama database, timezone)
* Username dan password MySQL

Tujuannya supaya konfigurasi terpusat dan mudah diubah.

---

### B. `DBConnection.java`

Berisi method:

* `getConnection()`

Fungsi utama:

1. Me-register driver JDBC (`Class.forName(...)`)
2. Membuat dan mengembalikan objek koneksi (`DriverManager.getConnection(...)`)

Dengan class ini, kode koneksi tidak perlu ditulis berulang di banyak tempat.

---

### C. `BukuDAO.java`

DAO (Data Access Object) adalah class khusus untuk **akses database**.
Di sini seluruh query SQL CRUD berada, menggunakan **PreparedStatement** agar lebih aman.

Fungsi yang tersedia:

* `insert(judul, pengarang)`
  Menambahkan data buku baru ke tabel `buku`
* `showAll()`
  Menampilkan semua data buku dari tabel `buku`
* `update(id, judul, pengarang)`
  Mengubah data buku berdasarkan `id_buku`
* `delete(id)`
  Menghapus data buku berdasarkan `id_buku`
* `exists(id)`
  Mengecek apakah ID buku ada, supaya update tidak dilakukan untuk data yang tidak tersedia

Kelebihan `PreparedStatement`: mengurangi risiko SQL Injection dan handling string lebih aman.

---

### D. `JavaCRUDBlueJ.java`

Berisi program utama dan menu console.

Alur program:

1. Program menampilkan menu utama berulang (loop)
2. User memilih opsi:

   * 1: Insert
   * 2: Show
   * 3: Edit (Update)
   * 4: Delete
   * 0: Keluar
3. Input user dibaca menggunakan `BufferedReader`
4. Saat memilih menu, program memanggil method CRUD pada `BukuDAO`

---

## 5. Alur Kerja CRUD

### Create (Insert)

User input `judul` dan `pengarang`, lalu program menjalankan query:
`INSERT INTO buku (judul, pengarang) VALUES (?, ?)`

### Read (Show)

Program mengambil semua data:
`SELECT id_buku, judul, pengarang FROM buku ORDER BY id_buku`
Hasil ditampilkan ke console.

### Update (Edit)

User memasukkan `id_buku`, lalu program:

1. cek ID ada atau tidak (`exists`)
2. jika ada → update judul & pengarang:
   `UPDATE buku SET judul=?, pengarang=? WHERE id_buku=?`

### Delete

User memasukkan `id_buku`, lalu program menghapus:
`DELETE FROM buku WHERE id_buku=?`

---

## 6. Kesimpulan dan Dokumentasi

Program CRUD ini berhasil menghubungkan Java dengan MySQL melalui JDBC, dan menyediakan menu sederhana untuk melakukan operasi tambah, tampil, ubah, dan hapus data buku. Pemisahan class (Config, Connection, DAO, Main) membuat kode lebih rapi, mudah dipelihara, dan lebih aman karena query menggunakan `PreparedStatement`.

<img width="1454" height="1052" alt="image" src="https://github.com/user-attachments/assets/799776cc-b1b8-4168-8828-79c050c9c5c9" />
<img width="977" height="911" alt="image" src="https://github.com/user-attachments/assets/71ad7df0-6187-4b18-ba77-89015c7e9840" />
