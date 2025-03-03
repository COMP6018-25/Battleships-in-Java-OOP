package src.main;

import java.util.Scanner;

public class BattleshipMain {
    public static void main(String[] args) {
        BattleshipModel model = new BattleshipModel();

        // Prompt user to choose "cli" or "gui"
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose interface: Type \"cli\" or \"gui\"");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if ("cli".equals(choice)) {
            // CLI
            BattleshipCLI cli = new BattleshipCLI(model);
            cli.startGame();
        } else if ("gui".equals(choice)) {
            // GUI
            model.initializeHardCodedBoard();
            BattleshipView view = new BattleshipView();
            BattleshipController controller = new BattleshipController(model, view);
            view.addController(controller);
        } else {
            System.out.println("Invalid input. Defaulting to GUI...");
            model.initializeHardCodedBoard();
            BattleshipView view = new BattleshipView();
            BattleshipController controller = new BattleshipController(model, view);
            view.addController(controller);
        }
    }
}
