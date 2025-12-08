# World Of Zuul
---

<img width="1209" height="1529" alt="image" src="https://github.com/user-attachments/assets/016125cc-117c-4d36-a95f-66dc4dce2d12" />
## Gamejava
```java
/**
 * Class Game - mengontrol jalannya permainan World of Zuul.
 * 
 * Game membuat semua ruangan, parser, dan menjalankan loop utama.
 */
public class Game
{
    private Parser parser;
    private Room currentRoom;

    /**
     * Constructor Game: inisialisasi room dan parser.
     */
    public Game()
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Pembuatan dan pengaturan hubungan antar room.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office;

        // membuat room
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");

        // set exit
        outside.setExits(null, theater, lab, pub);
        theater.setExits(null, null, null, outside);
        pub.setExits(null, outside, null, null);
        lab.setExits(outside, office, null, null);
        office.setExits(null, null, null, lab);

        currentRoom = outside; // mulai dari luar kampus
    }

    /**
     * Memulai permainan.
     */
    public void play()
    {
        printWelcome();

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }

        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Menampilkan pesan pembuka game.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a simple text adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Menampilkan lokasi dan exit room saat ini.
     */
    private void printLocationInfo()
    {
        System.out.println("You are " + currentRoom.getDescription());
        System.out.println("Exits: " + currentRoom.getExitString());
        System.out.println();
    }

    /**
     * Proses command yang diberikan pemain.
     */
    private boolean processCommand(Command command)
    {
        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();

        return switch (commandWord) {
            case "help" -> {
                printHelp();
                yield false;
            }
            case "go" -> {
                goRoom(command);
                yield false;
            }
            case "quit" -> quit(command);
            default -> false;
        };
    }

    /**
     * Menampilkan daftar command.
     */
    private void printHelp()
    {
        System.out.println("Your command words are: go quit help");
    }

    /**
     * Menggerakkan pemain ke ruangan lain.
     */
    private void goRoom(Command command)
    {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            currentRoom = nextRoom;
            printLocationInfo();
        }
    }

    /**
     * Keluar dari permainan.
     */
    private boolean quit(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        return true; // player benar ingin quit
    }

    /**
     * Entry point jika dijalankan sebagai aplikasi.
     */
    public static void main(String[] args)
    {
        Game game = new Game();
        game.play();
    }
}
```

## Command.java
```
public class Command {
    private String commandWord;
    private String secondWord;

    public Command(String commandWord, String secondWord) {
        this.commandWord = commandWord;
        this.secondWord = secondWord;
    }

    public String getCommandWord() {
        return commandWord;
    }

    public String getSecondWord() {
        return secondWord;
    }

    public boolean isUnknown() {
        return commandWord == null;
    }

    public boolean hasSecondWord() {
        return secondWord != null;
    }
}

```

## Parser.java
```java
import java.util.Scanner;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */
public class Parser 
{
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() 
    {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     * @return The next command from the user.
     */
    public Command getCommand() 
    {
        String inputLine;   // will hold the full input line
        String word1 = null;
        String word2 = null;

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();

        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
            word1 = tokenizer.next();      // get first word
            if(tokenizer.hasNext()) {
                word2 = tokenizer.next();      // get second word
                // note: we just ignore the rest of the input line.
            }
        }

        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).
        if(commands.isCommand(word1)) {
            return new Command(word1, word2);
        }
        else {
            return new Command(null, word2); 
        }
    }
}
```

## Room.java
```java
/**
 * Class Room - sebuah ruangan di dalam game World of Zuul.
 *
 * Setiap Room punya deskripsi, dan 4 kemungkinan exit:
 * north, east, south, west.
 *
 */
public class Room
{
    private String description;
    private Room northExit;
    private Room eastExit;
    private Room southExit;
    private Room westExit;

    /**
     * Buat room dengan deskripsi tertentu.
     */
    public Room(String description)
    {
        this.description = description;
    }

    /**
     * Set semua exit dari room ini.
     */
    public void setExits(Room north, Room east, Room south, Room west)
    {
        this.northExit = north;
        this.eastExit = east;
        this.southExit = south;
        this.westExit = west;
    }

    /**
     * Ambil exit berdasarkan arah.
     */
    public Room getExit(String direction)
    {
        return switch (direction) {
            case "north" -> northExit;
            case "east"  -> eastExit;
            case "south" -> southExit;
            case "west"  -> westExit;
            default -> null;
        };
    }

    /**
     * Ambil deskripsi ruangan.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Mendapatkan teks daftar exit.
     */
    public String getExitString()
    {
        String exits = "";

        if (northExit != null) exits += "north ";
        if (eastExit != null)  exits += "east ";
        if (southExit != null) exits += "south ";
        if (westExit != null)  exits += "west ";

        return exits;
    }
}

```

## CommandWords.java
```java
public class CommandWords {
    private final String[] validCommands = { "go", "quit", "help" };

    public boolean isCommand(String aString) {
        for (String cmd : validCommands) {
            if (cmd.equals(aString)) {
                return true;
            }
        }
        return false;
    }

    public String getCommandList() {
        return String.join(" ", validCommands);
    }
}

```
