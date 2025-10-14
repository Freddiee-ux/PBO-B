# ETS PBO Snack Machine

## Rancangan Class dan Objek
<img width="1434" height="1061" alt="image" src="https://github.com/user-attachments/assets/d3ab3fb9-4d2b-40f9-8e36-6873d42dc606" />

## Code

### SnackMachine.java
```java
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SnackMachine {
    public static void main(String[] args) {
        Snack[] snacks = {
            new Snack("Keripik Kentang", 15000.00, 10),   
            new Snack("Coklat Batang", 20000.00, 5),     
            new Snack("Minuman Soda", 12500.00, 8),       
            new Snack("Permen", 7500.00, 20),            
            new Snack("Permen Karet", 5000.00, 15),       
            new Snack("Kue Kering", 17500.00, 7),     
            new Snack("Kacang-kacangan", 25000.00, 1),    
            new Snack("Biskuit", 10000.00, 12)          
        };
        Machine snackMachine = new Machine(snacks);
        transactions transaction = new transactions();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to the Snack Machine!");
            System.out.println("1. Display Snacks");
            System.out.println("2. Select Snack");
            System.out.println("3. View Total Sales (Admin)");
            System.out.println("4. Exit");
            System.out.print("Please enter your choice: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    snackMachine.displaySnacks();
                    break;
                case 2:
                    snackMachine.displaySnacks();
                    System.out.print("Enter the number of the snack you want to select: ");
                    int snackNumber = scanner.nextInt();
                    snackMachine.selectSnack(snackNumber, scanner, transaction);
                    break;
                case 3:
                    System.out.print("Enter admin password: ");
                    String password = scanner.next();
                    if (password.equals("admin123")) {
                        System.out.printf("Total Sales: Rp.%.2f%n", transaction.getTotalSales());
                        snackMachine.displayStockStatus();
                        transaction.displayTransactionHistory();
                        System.out.println("***********************");
                    } else {
                        System.out.println("Incorrect password. Access denied.");
                    }
                    break;
                case 4:
                    running = false;
                    System.out.println("Thank you for using the snack machine!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}
```

### Machine.java
```java
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Machine
{
    // instance variables - replace the example below with your own
    private Snack[] snacks;

    public Machine(Snack[] snacks) {
        this.snacks = snacks;
    }

    public void displaySnacks() {
        System.out.println("Available Snacks:");
        for (int i = 0; i < snacks.length; i++) {
            System.out.println((i + 1) + ". " + snacks[i].getName() + " - Rp." + snacks[i].getPrice() + " (Quantity: " + snacks[i].getQuantity() + ")");
        }
    }

    public void selectSnack(int index, Scanner scanner, transactions transaction) {
        if (index < 1 || index > snacks.length) {
            System.out.println("Invalid selection. Please choose a valid snack number.");
            return;
        }
        Snack selectedSnack = snacks[index - 1];
        if (selectedSnack.getQuantity() <= 0) {
            System.out.println(selectedSnack.getName() + " is out of stock!");
            return;
        }

        System.out.println("Please pay Rp." + selectedSnack.getPrice());
        System.out.print("Enter payment amount: ");
        double paymentAmount = scanner.nextDouble();
        if (paymentAmount < selectedSnack.getPrice()) {
            System.out.println("Insufficient payment. Transaction cancelled.");
            return;
        }

        selectedSnack.dispense();
        LogTransaction(selectedSnack.getPrice(), selectedSnack, transaction);
        System.out.println("Thank you for your purchase!");
        if (paymentAmount > selectedSnack.getPrice()) {
            System.out.printf("Your change is Rp.%.2f%n", paymentAmount - selectedSnack.getPrice());
        }
    }

    public void LogTransaction(double amount, Snack snack, transactions transaction) {
        transaction.recordSale(snack, amount, 0);
    }

    public void displayStockStatus() {
        System.out.println("\n--- Snack Stock Status ---");
        for (Snack snack : snacks) {
            String status = snack.getQuantity() > 0 ? "In Stock" : "Out of Stock";
            System.out.printf("%s: %s (Quantity: %d)%n", snack.getName(), status, snack.getQuantity());
        }
        System.out.println("--------------------------");
    }
}
```
### Snack.java
```java
public class Snack
{
    // instance variables - replace the example below with your own
    private String name;
    private double price;
    private int quantity;

    public Snack(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void dispense() {
        if (quantity > 0) {
            quantity--;
            System.out.println("Dispensing " + name);
        } else {
            System.out.println(name + " is out of stock! Please restock.");
        }
    }
}
```

### transactions.java
```java
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class transactions
{
    private double totalSales;
    private List<String> transactionLog; 

    public transactions() {
        this.totalSales = 0;
        this.transactionLog = new ArrayList<>();
    }

    // Metode untuk mencatat detail penjualan langsung sebagai String
    public void recordSale(Snack snack, double amountPaid, double change) {
        double amount = snack.getPrice();
        totalSales += amount;
        
        // Format waktu untuk log
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(dtf);
        
        // Membuat string log yang detail
        String logEntry = String.format("[%s] Sold: %s | Price: Rp.%.2f | Paid: Rp.%.2f | Change: $%.2f",
                             timestamp, snack.getName(), amount, amountPaid, change);
        
        transactionLog.add(logEntry);
        
        System.out.println("Transaction logged for " + snack.getName() + ".");
    }

    public double getTotalSales() {
        return totalSales;
    }
    
    public void displayTransactionHistory() {
        System.out.println("\n--- Detailed Transaction History ---");
        if (transactionLog.isEmpty()) {
            System.out.println("No transactions recorded yet.");
        } else {
            for (String log : transactionLog) {
                System.out.println(log);
            }
        }
        System.out.println("------------------------------------");
    }
}
```

