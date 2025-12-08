# Vehicle Rental System

<img width="1190" height="1078" alt="image" src="https://github.com/user-attachments/assets/decfd91d-05a1-4b27-997d-ac6a3d53dda7" />
<img width="1269" height="1293" alt="image" src="https://github.com/user-attachments/assets/404a790b-90d3-4514-9158-80982995120d" />

Program Vehicle Rental System ini dibangun dengan menerapkan konsep Pemrograman Berorientasi Objek (OOP), khususnya **inheritance**, **polymorphism**, dan **encapsulation**. Sistem ini mengelola berbagai jenis kendaraan yang dapat disewa oleh pengguna, seperti mobil (Car), sepeda motor (Motorcycle), dan sepeda (Bike). Seluruh jenis kendaraan tersebut diturunkan dari satu kelas induk yaitu **Vehicle** yang menyimpan atribut dasar seperti *make*, *model*, dan *year*. Melalui pewarisan, setiap subclass menambahkan atribut dan perilaku khusus sesuai karakteristiknya, namun tetap mewarisi properti umum dari superclass.

Kelas **InputReader** digunakan untuk membaca input dari pengguna, sehingga logika I/O terpisah dari logika bisnis. Hal ini membuat program lebih terstruktur dan mudah dipelihara. Selanjutnya, kelas **VehicleRentalSystem** berfungsi sebagai pengelola utama sistem. Kelas ini menyimpan daftar semua kendaraan dalam *ArrayList* dan mencatat kendaraan yang sedang disewa menggunakan *HashMap* yang memetakan kendaraan ke nama penyewa.

Program menyediakan fitur untuk:

* menampilkan kendaraan yang tersedia,
* menampilkan kendaraan yang sedang disewa,
* melakukan penyewaan kendaraan,
* melakukan pengembalian kendaraan,
* serta menambah data kendaraan baru.

Metode `showDetail()` pada setiap subclass dioverride untuk menampilkan informasi kendaraan secara lengkap dan sesuai jenisnya (polymorphism). Ketika objek kendaraan dipanggil melalui referensi `Vehicle`, Java tetap mengeksekusi versi method yang dimiliki objek sebenarnya, sehingga keluaran informasi menjadi akurat untuk setiap tipe kendaraan.

Secara keseluruhan, kode ini menunjukkan bagaimana konsep OOP dapat digunakan untuk membangun sistem yang modular, fleksibel, dan mudah dikembangkan. Struktur pewarisan mempermudah penambahan jenis kendaraan baru tanpa mengubah kode yang sudah ada, sementara penggunaan koleksi seperti ArrayList dan HashMap memungkinkan pengelolaan data rental yang lebih efisien.

---

# Source Code

## **1. InputReader.java**

```java
import java.util.Scanner;

/**
 * Kelas InputReader digunakan untuk membaca input dari pengguna.
 * Memisahkan logika input dari logika bisnis utama agar program lebih bersih.
 */
public class InputReader
{
    private Scanner scanner;

    public InputReader() {
        scanner = new Scanner(System.in);
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine());
    }

    public void close() {
        scanner.close();
    }
}
```

---

## **2. Vehicle.java (Superclass)**

```java
/**
 * Kelas Vehicle merupakan superclass bagi semua jenis kendaraan.
 * Menyimpan informasi dasar seperti make, model, dan year.
 */
public class Vehicle
{
    private String make;
    private String model;
    private int year;

    public Vehicle(String make, String model, int year) {
        this.make  = make;
        this.model = model;
        this.year  = year;
    }

    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }

    /**
     * Menampilkan detail kendaraan dasar.
     */
    public void showDetail() {
        System.out.println("Make : " + make);
        System.out.println("Model: " + model);
        System.out.println("Year : " + year);
    }
}
```

---

## **3. Car.java**

```java
/**
 * Kelas Car adalah subclass dari Vehicle yang menambahkan atribut khusus mobil.
 */
public class Car extends Vehicle
{
    private int numberOfDoors;
    private int numberOfSeats;
    private int numberOfWheels;
    private boolean isElectric;

    public Car(String make, String model, int year,
               int numberOfDoors, int numberOfSeats,
               int numberOfWheels, boolean isElectric)
    {
        super(make, model, year);
        this.numberOfDoors  = numberOfDoors;
        this.numberOfSeats  = numberOfSeats;
        this.numberOfWheels = numberOfWheels;
        this.isElectric     = isElectric;
    }

    @Override
    public void showDetail() {
        super.showDetail();
        System.out.println("Doors   : " + numberOfDoors);
        System.out.println("Seats   : " + numberOfSeats);
        System.out.println("Wheels  : " + numberOfWheels);
        System.out.println("Electric: " + isElectric);
    }
}
```

---

## **4. Motorcycle.java**

```java
/**
 * Kelas Motorcycle adalah subclass dari Vehicle yang menyimpan
 * informasi tambahan seperti kapasitas mesin dan apakah motor listrik.
 */
public class Motorcycle extends Vehicle
{
    private boolean isElectric;
    private int engineCapacity;

    public Motorcycle(String make, String model, int year,
                      boolean isElectric, int engineCapacity)
    {
        super(make, model, year);
        this.isElectric     = isElectric;
        this.engineCapacity = engineCapacity;
    }

    @Override
    public void showDetail() {
        super.showDetail();
        System.out.println("Electric       : " + isElectric);
        System.out.println("Engine Capacity: " + engineCapacity + " cc");
    }
}
```

---

## **5. Bike.java**

```java
/**
 * Kelas Bike adalah subclass dari Vehicle yang menyimpan jenis sepeda
 * serta apakah sepeda memiliki keranjang (carrier).
 */
public class Bike extends Vehicle
{
    private String type;
    private boolean hasCarrier;

    public Bike(String make, String model, int year,
                String type, boolean hasCarrier)
    {
        super(make, model, year);
        this.type       = type;
        this.hasCarrier = hasCarrier;
    }

    @Override
    public void showDetail() {
        super.showDetail();
        System.out.println("Type      : " + type);
        System.out.println("HasCarrier: " + hasCarrier);
    }
}
```

---

## **6. VehicleRentalSystem.java (Main Program Lengkap)**

```java
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Kelas VehicleRentalSystem mengelola daftar kendaraan dan proses penyewaan.
 * Menggunakan ArrayList untuk menyimpan kendaraan dan HashMap untuk mencatat penyewa.
 */
public class VehicleRentalSystem
{
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private HashMap<Vehicle, String> rentedVehicles = new HashMap<>();
    private InputReader input = new InputReader();

    public static void main(String[] args) {
        VehicleRentalSystem app = new VehicleRentalSystem();
        app.seedData();
        app.run();
    }

    /**
     * Menambahkan data kendaraan awal untuk demonstrasi.
     */
    public void seedData() {
        vehicles.add(new Car("Toyota", "Camry", 2020, 4, 5, 4, false));
        vehicles.add(new Car("Tesla", "Model S", 2021, 4, 5, 4, true));

        vehicles.add(new Motorcycle("Harley Davidson", "Street 750", 2019, false, 750));
        vehicles.add(new Motorcycle("Zero SR/F", "Electric Moto", 2022, true, 110));

        vehicles.add(new Bike("Giant", "Escape 3", 2021, "Road Bike", true));
        vehicles.add(new Bike("Trek", "Marlin 7", 2020, "Mountain Bike", false));
    }

    /**
     * Program utama dengan menu interaktif.
     */
    public void run() {
        while (true) {
            showMenu();
            int choice = input.readInt("Choose option: ");

            switch (choice) {
                case 1 -> showAvailableVehicles();
                case 2 -> showRentedVehicles();
                case 3 -> rentVehicle();
                case 4 -> returnVehicle();
                case 5 -> exitProgram();
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n===== VEHICLE RENTAL SYSTEM =====");
        System.out.println("1. Show Available Vehicles");
        System.out.println("2. Show Rented Vehicles");
        System.out.println("3. Rent a Vehicle");
        System.out.println("4. Return a Vehicle");
        System.out.println("5. Exit");
    }

    private void showAvailableVehicles() {
        System.out.println("\n===== AVAILABLE VEHICLES =====");

        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);

            if (!rentedVehicles.containsKey(v)) {
                System.out.println("Index: " + i);
                v.showDetail();
                System.out.println("-----------------------------");
            }
        }
    }

    private void showRentedVehicles() {
        System.out.println("\n===== RENTED VEHICLES =====");

        for (Vehicle v : rentedVehicles.keySet()) {
            v.showDetail();
            System.out.println("Rented by: " + rentedVehicles.get(v));
            System.out.println("-----------------------------");
        }
    }

    private void rentVehicle() {
        showAvailableVehicles();

        int index = input.readInt("Enter vehicle index: ");
        String renterName = input.readString("Enter renter name: ");

        if (index >= 0 && index < vehicles.size()) {
            Vehicle v = vehicles.get(index);

            if (!rentedVehicles.containsKey(v)) {
                rentedVehicles.put(v, renterName);
                System.out.println("Vehicle rented successfully to " + renterName);
            } else {
                System.out.println("Vehicle is already rented.");
            }
        } else {
            System.out.println("Invalid index!");
        }
    }

    private void returnVehicle() {
        showRentedVehicles();

        int index = input.readInt("Enter index to return: ");

        if (index >= 0 && index < vehicles.size()) {
            Vehicle v = vehicles.get(index);

            if (rentedVehicles.containsKey(v)) {
                rentedVehicles.remove(v);
                System.out.println("Vehicle returned successfully.");
            } else {
                System.out.println("Vehicle is not rented.");
            }
        } else {
            System.out.println("Invalid index!");
        }
    }

    private void exitProgram() {
        System.out.println("Thank you for using the Vehicle Rental System!");
        input.close();
        System.exit(0);
    }
}
```



