package src.main;

import java.io.IOException;
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
            BattleshipCLI cli = new BattleshipCLI(model);
            cli.startGame();
        } else if ("gui".equals(choice)) {
            // Random or File Loaded board
            System.out.print("Press [H] for Random board or [F] for file loading: ");
            String boardChoice = scanner.nextLine().trim().toUpperCase();
            if (boardChoice.startsWith("F")) {
                System.out.print("Enter path to file: ");
                String path = scanner.nextLine().trim();
                try {
                    model.loadFromFile(path);
                } catch (IOException e) {
                    System.out.println("Failed to load from file. Using random board instead.");
                    model.initializeRandomBoard();
                }
            } else {
                model.initializeRandomBoard();
            }

            BattleshipView view = new BattleshipView(model);
            BattleshipController controller = new BattleshipController(model, view);
            view.addController(controller);
        } else {
            System.out.println("Invalid input. Defaulting to GUI with random board...");
            model.initializeRandomBoard();
            BattleshipView view = new BattleshipView(model);
            BattleshipController controller = new BattleshipController(model, view);
            view.addController(controller);
        }

    }
}
