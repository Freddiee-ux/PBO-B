# Membuat TechSupportSystem
<img width="1357" height="1020" alt="image" src="https://github.com/user-attachments/assets/195f5856-2dc5-4ab2-b4ee-9b5284543e68" />

## Class SupportSystem
```c
public class SupportSystem
{
    private InputReader reader;
    private Responder responder;
    
    /**
     * Creates a technical support system.
     */
    public SupportSystem()
    {
        reader = new InputReader();
        responder = new Responder();
    }

    /**
     * Start the technical support system. This will print a welcome
     * message and enter into a dialog with the user, until the user
     * ends the dialog.
     */
    public void start()
    {
        boolean finished = false;

        printWelcome();

        while(!finished) {
            String input = reader.getInput();

            if(input.toLowerCase().startsWith("bye")) {
                finished = true;
            }
            else {
                String response = responder.generateResponse(input.toLowerCase());
                System.out.println(response);
            }
        }
        printGoodbye();
    }

    /**
     * Print a welcome message to the screen.
     */
    private void printWelcome()
    {
        System.out.println("Welcome to the DodgySoft Technical Support System.");
        System.out.println();
        System.out.println("Please tell us about your problem.");
        System.out.println("We will assist you with any problem you might have.");
        System.out.println("Please type 'bye' to exit our system.");
    }

    /**
     * Print a good-bye message to the screen.
     */
    private void printGoodbye()
    {
        System.out.println("Nice talking to you. Bye...");
    }
}
```

## Class InputReader
## Class Responder


<img width="793" height="420" alt="image" src="https://github.com/user-attachments/assets/d154c2ba-d3aa-482a-9de9-ed51699b1972" />
