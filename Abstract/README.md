# **Abstract Class**

<img width="1102" height="1124" alt="image" src="https://github.com/user-attachments/assets/e2ca95bc-c7e5-4180-85d3-a34cf6f22c96" />
<img width="986" height="875" alt="image" src="https://github.com/user-attachments/assets/d08877aa-6777-4d94-a1be-46d1f6d758cf" />

Pada proyek ini, saya mengimplementasikan konsep **kelas abstrak (abstraction)** dalam pemrograman berorientasi objek menggunakan Java. Kelas abstrak digunakan sebagai cetak biru bagi kelas-kelas turunan sehingga logika umum dapat ditempatkan di satu tempat, sementara perilaku spesifik diimplementasikan oleh subclass.

Saya membuat kelas **LivingCreature** sebagai kelas abstrak yang memiliki metode abstrak seperti `eat()`, `move()`, dan metode konkret seperti `sleep()` dan `grow()`. Kemudian dibuat dua kelas abstrak turunan: **Herbivore** dan **Carnivore**, yang mewakili dua kelompok hewan berdasarkan jenis makanannya.

Dari kedua kelas ini, saya membuat kelas **Rabbit** sebagai herbivora dan **Fox** sebagai karnivora. Sistem ini kemudian dijalankan dalam kelas **Ecosystem** dan **Simulator**, di mana hewan-hewan dapat bergerak, makan, dan berinteraksi satu sama lain. Rubah dapat memangsa kelinci, sedangkan kelinci hanya makan tumbuhan.

Melalui proyek ini, saya mempelajari bagaimana abstraction membantu menyederhanakan desain sistem dengan membagi perilaku umum dan khusus secara terstruktur. Selain itu, konsep inheritance dan polymorphism juga terlihat ketika objek-objek hewan dipanggil melalui referensi superclass, tetapi tetap menjalankan perilaku spesifik masing-masing.

---

# **Source code**

## **1. LivingCreature.java**

```java
public abstract class LivingCreature
{
    protected String name;
    protected boolean alive = true;

    public LivingCreature(String name) {
        this.name = name;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
        System.out.println(name + " has died.");
    }

    public abstract void move(Ecosystem eco);
    public abstract void eat(Ecosystem eco);

    public void sleep() {
        System.out.println(name + " is sleeping.");
    }

    public void breathe() {
        System.out.println(name + " is breathing.");
    }

    public void grow() {
        System.out.println(name + " is growing.");
    }
}
```

---

## **2. Herbivore.java**

```java
public abstract class Herbivore extends LivingCreature
{
    public Herbivore(String name) {
        super(name);
    }

    @Override
    public void eat(Ecosystem eco) {
        System.out.println(name + " is eating plants.");
    }
}
```

---

## **3. Carnivore.java**

```java
public abstract class Carnivore extends LivingCreature
{
    public Carnivore(String name) {
        super(name);
    }

    @Override
    public void eat(Ecosystem eco) {
        System.out.println(name + " is searching for prey...");
    }
}
```

---

## **4. Rabbit.java**

```java
public class Rabbit extends Herbivore
{
    private int x, y;

    public Rabbit(int x, int y) {
        super("Rabbit");
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(Ecosystem eco) {
        int newX = eco.randomMove(x);
        int newY = eco.randomMove(y);

        System.out.println("Rabbit hops from (" + x + "," + y + ") to (" + newX + "," + newY + ")");
        x = newX;
        y = newY;
    }
}
```

---

## **5. Fox.java**

```java
public class Fox extends Carnivore
{
    private int x, y;

    public Fox(int x, int y) {
        super("Fox");
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(Ecosystem eco) {
        int newX = eco.randomMove(x);
        int newY = eco.randomMove(y);

        System.out.println("Fox moves from (" + x + "," + y + ") to (" + newX + "," + newY + ")");
        x = newX;
        y = newY;
    }

    @Override
    public void eat(Ecosystem eco) {
        Rabbit rabbit = eco.findRabbit();
        if (rabbit != null) {
            System.out.println("Fox eats a rabbit!");
            rabbit.die();
        } else {
            System.out.println("Fox found no rabbit nearby.");
        }
    }
}
```

---

## **6. Ecosystem.java**

```java
import java.util.ArrayList;
import java.util.List;

public class Ecosystem
{
    private int width;
    private int height;

    private List<LivingCreature> creatures = new ArrayList<>();

    public Ecosystem(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addCreature(LivingCreature c) {
        creatures.add(c);
    }

    public int randomMove(int pos) {
        int move = pos + ((int)(Math.random() * 3) - 1); // random -1, 0, 1
        if (move < 0) move = 0;
        if (move >= width) move = width - 1;
        return move;
    }

    public Rabbit findRabbit() {
        for (LivingCreature c : creatures) {
            if (c instanceof Rabbit && c.isAlive()) {
                return (Rabbit) c;
            }
        }
        return null;
    }

    public List<LivingCreature> getCreatures() {
        return creatures;
    }
}
```

---

## **7. Simulator.java**

```java
public class Simulator
{
    private Ecosystem eco;

    public Simulator() {
        eco = new Ecosystem(10, 10);

        eco.addCreature(new Rabbit(2, 3));
        eco.addCreature(new Rabbit(5, 2));
        eco.addCreature(new Fox(4, 4));
    }

    public void simulate(int steps) {
        for (int step = 1; step <= steps; step++) {
            System.out.println("\n=== Simulation Step " + step + " ===");

            for (LivingCreature c : eco.getCreatures()) {
                if (c.isAlive()) {
                    c.move(eco);
                    c.eat(eco);
                }
            }
        }
    }

    public static void main(String[] args) {
        Simulator sim = new Simulator();
        sim.simulate(5);
    }
}

