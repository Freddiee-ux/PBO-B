# Latihan Ticket Machine

## TicketMachine
```java
public class TicketMachine
{
    // instance variables - replace the example below with your own
    private int price;
    private int balance;
    private int total;
    
    public TicketMachine (int ticketCost)
    {
        price = ticketCost;
        balance = 0;
        total = 0;
    }
    
    public int getPrice(){
        return price;
    }
    
    public int getBalance(){
        return balance;
    }
    
    public void insertMoney(int amount){
        balance = balance + amount;
    }
    
    public void printTicket(){
        if (balance >= price) {
            System.out.println(" The Bluej Line");
            System.out.println(" Ticket");
            System.out.println(price + " cents.");
            System.out.println();
            
            total = total + price;
            balance = balance - price;
        } else {
            System.out.println("You must insert at least " + (price - balance) + " more cents.");
        }
    }

}

```

## TestMain

```java
import java.util.Scanner;


public class TestMachine {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.print("Masukkan harga tiket (dalam sen): ");
        int harga = input.nextInt();
        
        TicketMachine machine = new TicketMachine(harga);
        
        System.out.print("Masukkan uang Anda: ");
        int uang = input.nextInt();
        machine.insertMoney(uang);
        
        System.out.println("Saldo saat ini: " + machine.getBalance());
        
        machine.printTicket();
        
        input.close();
    }
}
```


<img width="957" height="241" alt="Screenshot 2025-10-13 193331" src="https://github.com/user-attachments/assets/6fb6f56d-b25f-49ef-bd8d-83be357347d0" />

