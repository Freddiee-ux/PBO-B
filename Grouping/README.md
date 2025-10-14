# Membuat implementasi grouping object dengan program ToDoList

Konsep Utama: Grouping Object
Secara sederhana, grouping object di sini berarti kita memiliki satu objek (yaitu ToDoList) yang bertindak sebagai "wadah" atau "pengelola" untuk sekumpulan objek lain (yaitu objek-objek Task).
- Task.java adalah blueprint untuk satu tugas individu. Setiap objek Task menyimpan informasi tentang satu hal yang harus dilakukan (judul, deskripsi, status).
- ToDoList.java adalah blueprint untuk kumpulan tugas. Objek ToDoList inilah yang melakukan grouping. Ia memiliki sebuah ArrayList di dalamnya untuk menampung banyak objek Task.
- Main.java adalah titik awal program yang berinteraksi dengan pengguna dan menggunakan objek ToDoList untuk mengelola semua tugas.

Langkah-langkah: 
- Inisialisasi Wadah: Di Main.java, kita membuat satu objek myTodoList. Saat objek ini dibuat, constructor dari kelas ToDoList langsung menyiapkan "wadah" kosong berupa new ArrayList<Task>().
- Membuat Objek Individu: Saat pengguna memilih menu "Tambah Tugas", program meminta input title dan description. Data ini kemudian digunakan untuk membuat objek baru dari kelas Task melalui Task newTask = new Task(title, description);.
- Memasukkan ke Wadah: Objek newTask yang baru dibuat kemudian dimasukkan ke dalam "wadah" ArrayList di dalam myTodoList menggunakan method myTodoList.addTask(newTask);. Di sinilah proses grouping terjadi.
- Mengelola Grup: Semua operasi lain (menampilkan, menghapus, menandai selesai) dilakukan melalui objek myTodoList. Objek ini tidak berurusan langsung dengan satu Task, melainkan dengan kumpulan Task yang ada di dalam ArrayList-nya. Ia tahu cara mengakses, memodifikasi, dan menghapus anggota dari grup tersebut.
  
## Main.java
```java
      myTodoList.displayTasks();
                    System.out.print("Masukkan nomor tugas yang ingin dihapus (DELETE): ");
                    if (scanner.hasNextInt()) {
                        int deleteIndex = scanner.nextInt();
                        myTodoList.deleteTask(deleteIndex);
                    } else {
                        System.out.println("Input harus berupa angka.");
                    }
                    scanner.nextLine();
                    break;
                case 5:
                    myTodoList.displayTasks();
                    System.out.print("Masukkan nomor tugas yang ingin dilihat detailnya: ");
                    if (scanner.hasNextInt()) {
                        int detailIndex = scanner.nextInt();
                        myTodoList.showTaskDetails(detailIndex);
                    } else {
                        System.out.println("Input harus berupa angka.");
                    }
                    scanner.nextLine();
                    break;
                case 0:
                    System.out.println("Terima kasih telah menggunakan aplikasi ToDo List. Sampai jumpa!");
                    break;
                default:
                    System.out.println("Pilihan menu tidak tersedia.");
            }
        } while (choice != 0);
        
        scanner.close();
    }
}
```

## ToDoList.java
```c
      myTodoList.displayTasks();
                    System.out.print("Masukkan nomor tugas yang ingin dihapus (DELETE): ");
                    if (scanner.hasNextInt()) {
                        int deleteIndex = scanner.nextInt();
                        myTodoList.deleteTask(deleteIndex);
                    } else {
                        System.out.println("Input harus berupa angka.");
                    }
                    scanner.nextLine();
                    break;
                case 5:
                    myTodoList.displayTasks();
                    System.out.print("Masukkan nomor tugas yang ingin dilihat detailnya: ");
                    if (scanner.hasNextInt()) {
                        int detailIndex = scanner.nextInt();
                        myTodoList.showTaskDetails(detailIndex);
                    } else {
                        System.out.println("Input harus berupa angka.");
                    }
                    scanner.nextLine();
                    break;
                case 0:
                    System.out.println("Terima kasih telah menggunakan aplikasi ToDo List. Sampai jumpa!");
                    break;
                default:
                    System.out.println("Pilihan menu tidak tersedia.");
            }
        } while (choice != 0);
        
        scanner.close();
    }
}
```

## Task.java
```java
  public class Task
  {
      private String title;
      private String description;
      private boolean isDone;
      
      public Task(String title, String description) {
              this.title = title;
              this.description = description;
              this.isDone = false; 
      }
  
      public void markAsDone() {
          this.isDone = true;
      }
  
      public String getTitle() { return title; }
      public boolean isDone() { return isDone; }
      
      @Override
      public String toString() {
          String status = isDone ? "[DONE]" : "[TODO]";
          return status + " " + title; 
      }
  }
```

<img width="994" height="956" alt="image" src="https://github.com/user-attachments/assets/0c5f8485-96ef-4927-9f51-161a51824aea" />
