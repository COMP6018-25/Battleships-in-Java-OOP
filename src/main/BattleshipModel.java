package src.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

public class BattleshipModel extends Observable {
    private static final int BOARD_SIZE = 10;
    private final CellState[][] board;
    private final List<Ship> ships;
    private int tries;

    private String lastSunkMessage = "";

    public String getLastSunkMessage() {
        String msg = lastSunkMessage;
        lastSunkMessage = "";
        return msg;
    }


    public BattleshipModel() {
        board = new CellState[BOARD_SIZE][BOARD_SIZE];
        ships = new ArrayList<>();
        tries = 0;
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                board[r][c] = CellState.EMPTY;
            }
        }
    }

    public void resetGame() {
        initializeEmptyBoard();
        ships.clear();
        tries = 0;
        setChanged();
        notifyObservers();
    }

    public void initializeRandomBoard() {
        resetGame(); // Clear any existing board/ships.
        int[] shipLengths = {5, 4, 3, 2, 2};
        Random rand = new Random();
        for (int length : shipLengths) {
            boolean placed = false;
            while (!placed) {
                boolean horizontal = rand.nextBoolean();
                int maxRow = horizontal ? BOARD_SIZE : BOARD_SIZE - length;
                int maxCol = horizontal ? BOARD_SIZE - length : BOARD_SIZE;
                int row = rand.nextInt(maxRow);
                int col = rand.nextInt(maxCol);
                Ship ship = new Ship(row, col, length, horizontal);
                try {
                    placeShip(ship);
                    placed = true;
                } catch (IllegalArgumentException e) {
                    // Overlap occurred; try another random placement.
                }
            }
        }
        setChanged();
        notifyObservers();
    }

    public void placeShip(Ship ship) {
        int row = ship.getStartRow();
        int col = ship.getStartCol();

        if (ship.isHorizontal()) {
            if (col + ship.getLength() > BOARD_SIZE) {
                throw new IllegalArgumentException("Ship extends beyond the right edge of the board.");
            }
        } else {
            if (row + ship.getLength() > BOARD_SIZE) {
                throw new IllegalArgumentException("Ship extends beyond the bottom edge of the board.");
            }
        }

        int r = row;
        int c = col;
        for (int i = 0; i < ship.getLength(); i++) {
            if (board[r][c] != CellState.EMPTY) {
                throw new IllegalArgumentException("Ship placement overlaps another ship at (" + r + ", " + c + ").");
            }
            if (ship.isHorizontal()) {
                c++;
            } else {
                r++;
            }
        }

        ships.add(ship);
        r = row;
        c = col;
        for (int i = 0; i < ship.getLength(); i++) {
            board[r][c] = CellState.SHIP;
            if (ship.isHorizontal()) {
                c++;
            } else {
                r++;
            }
        }
        setChanged();
        notifyObservers();
    }

    public void loadFromFile(String filename) throws IOException {
        initializeEmptyBoard();
        ships.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length != 4) {
                    throw new IOException("Invalid file format line: " + line);
                }
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                int length = Integer.parseInt(parts[2]);
                boolean horizontal = parts[3].equalsIgnoreCase("H");
                placeShip(new Ship(row, col, length, horizontal));
            }
        }
        setChanged();
        notifyObservers();
    }

    public boolean guess(int row, int col) {
        tries++;

        if (board[row][col] == CellState.HIT || board[row][col] == CellState.MISS) {
            return false;
        }

        boolean hit = false;
        if (board[row][col] == CellState.SHIP) {
            board[row][col] = CellState.HIT;
            hit = true;
            // Checks if a ship was sunk
            for (Ship s : ships) {
                if (isPartOfShip(s, row, col)) {
                    s.registerHit();
                    if (s.justSunk()) {
                        lastSunkMessage = "Ship sunk! (Length: " + s.getLength() + ")";
                    }
                    break;
                }
            }
        } else {
            board[row][col] = CellState.MISS;
        }
        setChanged();
        notifyObservers();
        return hit;
    }


    public boolean isGameOver() {
        for (Ship s : ships) {
            if (!s.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public int getTries() {
        return tries;
    }

    public CellState getCellState(int row, int col) {
        return board[row][col];
    }

    public int getShipsRemaining() {
        int count = 0;
        for (Ship s : ships) {
            if (!s.isSunk()) {
                count++;
            }
        }
        return count;
    }

    private boolean isPartOfShip(Ship ship, int row, int col) {
        int sr = ship.getStartRow();
        int sc = ship.getStartCol();
        for (int i = 0; i < ship.getLength(); i++) {
            if (sr == row && sc == col) {
                return true;
            }
            if (ship.isHorizontal()) {
                sc++;
            } else {
                sr++;
            }
        }
        return false;
    }
}
