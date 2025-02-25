package src.main;

import java.util.Scanner;

public class BattleshipCLI {
    private final BattleshipModel model;
    private final Scanner scanner;

    // For convenience in parsing A-J
    private final String ROW_LABELS = "ABCDEFGHIJ";

    public BattleshipCLI(BattleshipModel model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
    }

    public void startGame() {
        System.out.println("Welcome to Battleships (CLI Version)!");
        while (!model.isGameOver()) {
            printBoard();
            System.out.print("Enter guess (e.g. A1, J10): ");
            String input = scanner.nextLine().trim();
            int[] rc = parseInput(input);
            if (rc == null) {
                System.out.println("Invalid input, please try again.");
                continue;
            }
            boolean hit = model.guess(rc[0], rc[1]);
            System.out.println(hit ? "Hit!" : "Miss!");
        }
        System.out.println("Game over! You sank all ships in "
                + model.getTries() + " tries.");
    }

    private void printBoard() {
        System.out.println("   1  2  3  4  5  6  7  8  9  10");
        for (int r = 0; r < 10; r++) {
            System.out.print(ROW_LABELS.charAt(r) + " ");
            for (int c = 0; c < 10; c++) {
                CellState state = model.getCellState(r, c);
                char symbol;
                switch (state) {
                    case HIT:  symbol = 'H'; break;
                    case MISS: symbol = 'M'; break;
                    default:   symbol = '.'; break;
                }
                System.out.printf(" %c ", symbol);
            }
            System.out.println();
        }
    }

    private int[] parseInput(String input) {
        // i.e. A1 would be row=0, col=0
        if (input.length() < 2) return null;

        // get letter (row)
        char rowChar = input.charAt(0);
        int row = ROW_LABELS.indexOf(rowChar);
        if (row < 0) return null;

        // get number (col)
        String colStr = input.substring(1); // everything after the first character
        int col;
        try {
            col = Integer.parseInt(colStr) - 1; // 0-based
        } catch (NumberFormatException e) {
            return null;
        }

        if (col < 0 || col >= 10) {
            return null;
        }
        return new int[]{row, col};
    }
}

