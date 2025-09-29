## Sistem FRS Sederrhana

<img width="1706" height="1372" alt="image" src="https://github.com/user-attachments/assets/a66e600c-1a82-4e7e-9c5c-66cc270ed19a" />


### Course.java
```java
import java.io.Serializable;
import java.util.Objects;

public class Course implements Serializable {
    private String nama;
    private int credits;
    private Dosen dosenPengampu;

    // Konstruktor: Course tanpa kode
    public Course(String nama, int credits, Dosen dosen) {
        if (nama == null) nama = "";
        this.nama = nama.trim();
        this.credits = Math.max(0, credits);
        this.dosenPengampu = dosen;
    }

    // Getter / setter
    public Dosen getDosenPengampu() {
        return dosenPengampu;
    }

    public void setDosenPengampu(Dosen dosen) {
        this.dosenPengampu = dosen;
    }

    public String getName() {
        return nama;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        String d = (dosenPengampu == null) ? "Belum ada" : dosenPengampu.getNama();
        return nama + " (" + credits + " SKS) - Dosen: " + d;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Course)) return false;
        Course other = (Course) obj;
        // Samakan berdasarkan nama mata kuliah (case-insensitive)
        return this.nama.equalsIgnoreCase(other.nama);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nama == null ? "" : nama.toLowerCase());
    }
}

```

### Kelas.java
```java
import java.io.Serializable;
import java.util.ArrayList;

public class Kelas implements Serializable {
    private String id;           // contoh: "IF101-A"
    private Course course;       // referensi ke Course (mata kuliah)
    private Dosen dosen;         // dosen pengampu kelas ini
    private int capacity;        // kapasitas maksimal mahasiswa
    private ArrayList<Mahasiswa> enrolled;

    private String hari;
    private int startMinute;
    private int endMinute;   

    public Kelas(String id, Course course, Dosen dosen, int capacity,
                 String hari, int startMinute, int endMinute) {
        this.id = id.toUpperCase();
        this.course = course;
        this.dosen = dosen;
        this.capacity = Math.max(1, capacity);
        this.enrolled = new ArrayList<>();
        this.hari = hari;
        this.startMinute = startMinute;
        this.endMinute = endMinute;
    }

    public String getId() { return id; }
    public Course getCourse() { return course; }
    public Dosen getDosen() { return dosen; }
    public int getCapacity() { return capacity; }
    public int getEnrolledCount() { return enrolled.size(); }
    public boolean isFull() { return enrolled.size() >= capacity; }
    public String getHari() { return hari; }
    public int getStartMinute() { return startMinute; }
    public int getEndMinute() { return endMinute; }

    public boolean addStudent(Mahasiswa m) {
        if (m == null) return false;
        if (isFull()) return false;
        if (isEnrolled(m.getNrp())) return false;
        enrolled.add(m);
        return true;
    }

    public boolean removeStudent(String nrp) {
        for (int i = 0; i < enrolled.size(); i++) {
            if (enrolled.get(i).getNrp().equalsIgnoreCase(nrp)) {
                enrolled.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean isEnrolled(String nrp) {
        for (Mahasiswa m : enrolled) if (m.getNrp().equalsIgnoreCase(nrp)) return true;
        return false;
    }

    // Cek apakah jadwal bertabrakan dengan kelas lain
    public boolean conflictsWith(Kelas other) {
        if (other == null) return false;
        if (!this.hari.equalsIgnoreCase(other.hari)) return false; // beda hari -> tidak tabrakan
        // overlap waktu
        return !(this.endMinute <= other.startMinute || other.endMinute <= this.startMinute);
    }

    private String minuteToHHMM(int m) {
        int hh = m / 60;
        int mm = m % 60;
        return String.format("%02d:%02d", hh, mm);
    }

    @Override
    public String toString() {
        String d = (dosen == null) ? "Belum ada" : dosen.getNama();
        String waktu = hari + " " + minuteToHHMM(startMinute) + "-" + minuteToHHMM(endMinute);
        // gunakan course.getName() karena Course sekarang tidak punya kode
        return id + " | " + course.getName() + " (" + course.getCredits() + " SKS) | Dosen: " + d +
               " | " + waktu + " | Kapasitas: " + getEnrolledCount() + "/" + capacity;
    }
}
```

### Mahasiswa.java
```java
import java.io.Serializable;
import java.util.ArrayList;

public class Mahasiswa implements Serializable {
    private String nrp;
    private String nama;
    private ArrayList<Kelas> enrolled; // menyimpan Kelas (section)

    public Mahasiswa(String nrp, String nama) {
        this.nrp = nrp;
        this.nama = nama;
        this.enrolled = new ArrayList<>();
    }

    // Getter
    public String getNrp() { return nrp; }
    public String getNim() { return nrp; } // alias kompatibilitas
    public String getName() { return nama; }

    // Cek sudah terdaftar di ID kelas tertentu
    public boolean isEnrolled(String kelasId) {
        for (Kelas k : enrolled) if (k.getId().equalsIgnoreCase(kelasId)) return true;
        return false;
    }

    // Cek apakah sudah ambil mata kuliah (berdasarkan nama/mata kuliah pada Course)
    public boolean hasCourse(String courseName) {
        for (Kelas k : enrolled) {
            if (k.getCourse().getName().equalsIgnoreCase(courseName)) return true;
        }
        return false;
    }

    public boolean addKelas(Kelas k, int maxCredits) {
        if (k == null) return false;

        // 1) cek sudah ambil course yang sama
        if (hasCourse(k.getCourse().getName())) return false;

        // 2) cek kapasitas kelas
        if (k.isFull()) return false;

        // 3) cek bentrok jadwal dengan kelas yang sudah diambil
        for (Kelas eks : enrolled) {
            if (eks.conflictsWith(k)) return false;
        }

        // 4) cek SKS limit
        int newTotal = getTotalCredits() + k.getCourse().getCredits();
        if (newTotal > maxCredits) return false;

        // 5) tambahkan dua arah: ke Kelas dan ke daftar enrolled Mahasiswa
        boolean ok = k.addStudent(this);
        if (!ok) return false;
        enrolled.add(k);
        return true;
    }

    // Hapus kelas: juga menghapus mahasiswa dari daftar di Kelas
    public boolean dropKelas(String kelasId) {
        for (int i = 0; i < enrolled.size(); i++) {
            if (enrolled.get(i).getId().equalsIgnoreCase(kelasId)) {
                Kelas k = enrolled.get(i);
                k.removeStudent(this.nrp);
                enrolled.remove(i);
                return true;
            }
        }
        return false;
    }

    // Total SKS dihitung dari Course pada setiap Kelas yang diambil
    public int getTotalCredits() {
        int sum = 0;
        for (Kelas k : enrolled) sum += k.getCourse().getCredits();
        return sum;
    }

    public ArrayList<Kelas> getEnrolledKelas() {
        return new ArrayList<>(enrolled);
    }

    @Override
    public String toString() {
        return nrp + " - " + nama + " | SKS: " + getTotalCredits();
    }

    public void displayInfo() {
        System.out.println("Mahasiswa: " + nama + " (NRP: " + nrp + ")");
        if (enrolled.isEmpty()) {
            System.out.println("  Belum mengambil kelas apapun.");
            return;
        }
        System.out.println("  Kelas yang diambil:");
        for (Kelas k : enrolled) {
            System.out.println("   - " + k.getId() + " | " + k.getCourse().getName() +
                               " (" + k.getCourse().getCredits() + " SKS) | " +
                               k.getHari() + " " + formatMinute(k.getStartMinute()) + "-" + formatMinute(k.getEndMinute()));
        }
    }

    // Helper format menit -> hh:mm untuk display kecil
    private String formatMinute(int m) {
        int hh = m / 60;
        int mm = m % 60;
        return String.format("%02d:%02d", hh, mm);
    }
}
```

### Dosen.java
```java
public class Dosen {
    // Atribut Dosen
    private String nip; // Nomor Induk Pegawai/Dosen
    private String nama;
    
    // Konstruktor
    public Dosen(String nip, String nama) {
        this.nip = nip;
        this.nama = nama;
    }
    
    // Getter
    public String getNip() {
        return nip;
    }
    
    public String getNama() {
        return nama;
    }
    
    // Metode untuk menampilkan informasi Dosen
    public void displayInfo() {
        System.out.println("Dosen: " + nama + " (NIP: " + nip + ")");
    }
    
    @Override
    public String toString() {
        return nama + " (NIP: " + nip + ")";
    }
}
```

### SistemFRS.java
```java
import java.util.*;
import java.io.*;

public class SistemFRS {
    private ArrayList<Course> catalog;
    private ArrayList<Dosen> daftarDosen;
    private ArrayList<Mahasiswa> students;
    private ArrayList<Kelas> kelasList;
    private Scanner sc;
    private final int MAX_CREDITS = 24;
    private final String DATA_FILE = "frs_data.csv";

    public SistemFRS() {
        catalog = new ArrayList<>();
        daftarDosen = new ArrayList<>();
        students = new ArrayList<>();
        kelasList = new ArrayList<>();
        sc = new Scanner(System.in);
        seedDosen();
        seedCourses();
        seedKelas();
        loadData();
    }

    private void seedDosen() {
        daftarDosen.add(new Dosen("19700101", "Prof. Ir. Ary Mazharuddin Shiddiqi, S.Kom., M.Comp.Sc., Ph.D"));
        daftarDosen.add(new Dosen("19800505", "Fajar Baskoro, S.Kom., M.T."));
        daftarDosen.add(new Dosen("19900202", "Ilham Gurat Adillion, S.Kom., M.Kom."));
        daftarDosen.add(new Dosen("19900303", "Dr. Sarwosri, S.Kom., M.T."));
        daftarDosen.add(new Dosen("19900404", "Ir. Arya Yudhi Wijaya, S.Kom., M.Kom."));
        daftarDosen.add(new Dosen("19900606", "Prof. Dr. Eng. Chastine Fatichah, S.Kom., M.Kom."));
    }

    // sekarang Course dibuat tanpa kode: new Course(nama, credits, dosen)
    private void seedCourses() {
        Dosen d1 = daftarDosen.get(0);
        Dosen d2 = daftarDosen.get(1);
        Dosen d3 = daftarDosen.get(2);
        Dosen d4 = daftarDosen.get(3);
        Dosen d5 = daftarDosen.get(4);
        Dosen d6 = daftarDosen.get(5);
        
        catalog.add(new Course("Pemrograman Web", 3, d2));
        catalog.add(new Course("Matematika Diskrit", 3, d3));
        catalog.add(new Course("Pemrograman Berorientasi Objek", 4, d2));
        catalog.add(new Course("Teori Graf", 3, d5));
        catalog.add(new Course("Jaringan Komputer", 3, d1));
        catalog.add(new Course("Konsep Kecerdasan Artifisial", 3, d6));
        catalog.add(new Course("Konsep Pengembangan Perangkat Lunak", 3, d4));
    }

    // Cari Course berdasarkan nama (case-insensitive)
    private Course findCourseByName(String name) {
        if (name == null) return null;
        for (Course c : catalog) if (c.getName().equalsIgnoreCase(name)) return c;
        return null;
    }

    private void seedKelas() {
        // sekarang kita cari Course berdasarkan nama
        Course c1 = findCourseByName("Pemrograman Web");
        Course c2 = findCourseByName("Matematika Diskrit");
        Course c3 = findCourseByName("Pemrograman Berorientasi Objek");
        Course c4 = findCourseByName("Teori Graf");
        Course c5 = findCourseByName("Jaringan Komputer");
        Course c6 = findCourseByName("Konsep Kecerdasan Artifisial");
        Course c7 = findCourseByName("Konsep Pengembangan Perangkat Lunak");

        if (c1 != null) {
            // IF101-A/B adalah ID kelas saja (identifier section), course tidak punya kode
            kelasList.add(new Kelas("IF101-A", c1, daftarDosen.get(1), 40, "Senin", 8*60 + 0, 9*60 + 40));  // 08:00-09:40
            kelasList.add(new Kelas("IF101-B", c1, daftarDosen.get(1), 35, "Rabu", 10*60 + 0, 11*60 + 40)); // 10:00-11:40
        }
        if (c2 != null) kelasList.add(new Kelas("IF102-A", c2, daftarDosen.get(2), 40, "Selasa", 8*60+0, 9*60+40));
        if (c3 != null) kelasList.add(new Kelas("IF201-A", c3, daftarDosen.get(1), 30, "Kamis", 13*60+0, 14*60+40));
        if (c4 != null) kelasList.add(new Kelas("IF202-A", c4, daftarDosen.get(4), 30, "Jumat", 8*60+0, 9*60+40));
        if (c5 != null) kelasList.add(new Kelas("IF301-A", c5, daftarDosen.get(0), 30, "Senin", 10*60+0, 11*60+40));
        if (c6 != null) kelasList.add(new Kelas("IF302-A", c6, daftarDosen.get(5), 30, "Selasa", 10*60+0, 11*60+40));
        if (c7 != null) kelasList.add(new Kelas("IF303-A", c7, daftarDosen.get(3), 30, "Kamis", 8*60+0, 9*60+40));
    }

    // ================= Persistence sederhana =================
    private void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Mahasiswa s : students) {
                StringBuilder sb = new StringBuilder();
                sb.append(escape(s.getNrp())).append(",");
                sb.append(escape(s.getName())).append(",");
                ArrayList<Kelas> ek = s.getEnrolledKelas();
                for (int i = 0; i < ek.size(); i++) {
                    sb.append(ek.get(i).getId());
                    if (i < ek.size() - 1) sb.append("|");
                }
                pw.println(sb.toString());
            }
            System.out.println("Data tersimpan ke " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan data: " + e.getMessage());
        }
    }

    private void loadData() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = splitCSV(line);
                if (parts.length >= 2) {
                    String nrp = unescape(parts[0]);
                    String name = unescape(parts[1]);
                    if (findStudentByNrp(nrp) != null) continue;

                    Mahasiswa m = new Mahasiswa(nrp, name);
                    students.add(m);
                    if (parts.length >= 3 && parts[2].trim().length() > 0) {
                        String[] kelasIds = parts[2].split("\\|");
                        for (String id : kelasIds) {
                            Kelas k = findKelasById(id.trim());
                            if (k != null) {
                                boolean ok = m.addKelas(k, MAX_CREDITS);
                                if (!ok) {
                                    System.out.println("Warning: Tidak dapat memulihkan " + id.trim() + " untuk " + nrp);
                                }
                            } else {
                                System.out.println("Warning: Kelas " + id.trim() + " tidak ditemukan saat load untuk " + nrp);
                            }
                        }
                    }
                }
            }
            System.out.println("Data dimuat dari " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Gagal memuat data: " + e.getMessage());
        }
    }

    private String escape(String s) { return s.replace(",", "\\,"); }
    private String unescape(String s) { return s.replace("\\,", ","); }

    private String[] splitCSV(String line) {
        ArrayList<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean esc = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (esc) {
                cur.append(ch);
                esc = false;
            } else {
                if (ch == '\\') esc = true;
                else if (ch == ',') { parts.add(cur.toString()); cur = new StringBuilder(); }
                else cur.append(ch);
            }
        }
        parts.add(cur.toString());
        return parts.toArray(new String[0]);
    }
    // =========================================================

    private Mahasiswa findStudentByNrp(String nrp) {
        for (Mahasiswa m : students) if (m.getNrp().equalsIgnoreCase(nrp)) return m;
        return null;
    }

    private Dosen findDosenByNIP(String nip) {
        for (Dosen d : daftarDosen) if (d.getNip().equalsIgnoreCase(nip)) return d;
        return null;
    }

    private Kelas findKelasById(String id) {
        for (Kelas k : kelasList) if (k.getId().equalsIgnoreCase(id)) return k;
        return null;
    }

    // ================= Console UI =================
    public void run() {
        while (true) {
            System.out.println("\n===== SISTEM FRS =====");
            System.out.println("1. Lihat katalog mata kuliah");
            System.out.println("2. Lihat daftar dosen");
            System.out.println("3. Lihat daftar kelas (section)");
            System.out.println("4. Registrasi mahasiswa baru");
            System.out.println("5. Enroll kelas (ambil)");
            System.out.println("6. Drop kelas");
            System.out.println("7. Lihat FRS mahasiswa");
            System.out.println("8. Simpan data");
            System.out.println("9. Keluar");
            System.out.print("Pilih menu: ");
            String pilih = sc.nextLine().trim();
            switch (pilih) {
                case "1": menuShowCatalog(); break;
                case "2": menuShowDosen(); break;
                case "3": menuShowKelas(); break;
                case "4": menuRegisterStudent(); break;
                case "5": menuEnrollKelas(); break;
                case "6": menuDropKelas(); break;
                case "7": menuViewFRS(); break;
                case "8": saveData(); break;
                case "9":
                    saveData();
                    System.out.println("Bye!");
                    return;
                default: System.out.println("Pilihan tidak dikenal.");
            }
        }
    }

    private void menuShowCatalog() {
        System.out.println("\n--- KATALOG MATA KULIAH ---");
        for (Course c : catalog) System.out.println("  " + c);
    }

    private void menuShowDosen() {
        System.out.println("\n--- DAFTAR DOSEN ---");
        for (Dosen d : daftarDosen) System.out.println("  " + d);
    }

    private void menuShowKelas() {
        System.out.println("\n--- DAFTAR KELAS / SECTION ---");
        for (Kelas k : kelasList) System.out.println("  " + k);
    }

    private void menuRegisterStudent() {
        System.out.print("Masukkan NRP: ");
        String nrp = sc.nextLine().trim();
        if (nrp.length() == 0) { System.out.println("NRP kosong."); return; }
        if (findStudentByNrp(nrp) != null) { System.out.println("Mahasiswa sudah terdaftar."); return; }
        System.out.print("Nama: ");
        String name = sc.nextLine().trim();
        students.add(new Mahasiswa(nrp, name));
        System.out.println("Mahasiswa terdaftar: " + nrp + " - " + name);
    }

    private void menuEnrollKelas() {
        System.out.print("NRP mahasiswa: ");
        String nrp = sc.nextLine().trim();
        Mahasiswa s = findStudentByNrp(nrp);
        if (s == null) { System.out.println("Mahasiswa tidak ditemukan."); return; }

        System.out.println("Daftar kelas tersedia:");
        for (Kelas k : kelasList) System.out.println("  " + k);

        System.out.print("Masukkan ID kelas (contoh IF101-A): ");
        String kelasId = sc.nextLine().trim();
        Kelas k = findKelasById(kelasId);
        if (k == null) { System.out.println("ID kelas tidak ditemukan."); return; }

        boolean ok = s.addKelas(k, MAX_CREDITS);
        if (ok) System.out.println("Berhasil mendaftar ke kelas " + k.getId());
        else System.out.println("Gagal mendaftar: mungkin sudah terdaftar (sudah ambil mata kuliah yg sama), kelas penuh, bentrok jadwal, atau melebihi batas SKS.");
    }

    private void menuDropKelas() {
        System.out.print("NRP mahasiswa: ");
        String nrp = sc.nextLine().trim();
        Mahasiswa s = findStudentByNrp(nrp);
        if (s == null) { System.out.println("Mahasiswa tidak ditemukan."); return; }

        System.out.println("Kelas yang diambil mahasiswa:");
        for (Kelas k : s.getEnrolledKelas()) System.out.println("  " + k.getId() + " - " + k.getCourse().getName());

        System.out.print("Masukkan ID kelas yang ingin di-drop: ");
        String kelasId = sc.nextLine().trim();
        boolean ok = s.dropKelas(kelasId);
        if (ok) System.out.println("Berhasil menghapus " + kelasId);
        else System.out.println("Gagal menghapus: mahasiswa tidak terdaftar di kelas tersebut.");
    }

    private void menuViewFRS() {
        System.out.print("NRP mahasiswa: ");
        String nrp = sc.nextLine().trim();
        Mahasiswa s = findStudentByNrp(nrp);
        if (s == null) { System.out.println("Mahasiswa tidak ditemukan."); return; }
        System.out.println("\nFRS untuk " + s.getNrp() + " - " + s.getName());
        System.out.println("Total SKS: " + s.getTotalCredits());
        System.out.println("Daftar kelas:");
        for (Kelas k : s.getEnrolledKelas()) System.out.println("  " + k);
    }

    // ================ MAIN =================
    public static void main(String[] args) {
        SistemFRS sys = new SistemFRS();
        sys.run();
    }
}
```

### Output:
