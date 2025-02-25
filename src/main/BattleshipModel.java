package src.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class BattleshipModel extends Observable {
    private static final int BOARD_SIZE = 10;
    private final CellState[][] board;
    private final List<Ship> ships;
    private int tries;

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

    public void initializeHardCodedBoard() {
        assert ships.isEmpty() : "Ships list should be empty at initialization";

        // Length 5: horizontal from (0,0) to (0,4)
        placeShip(new Ship(0, 0, 5, true));

        // Length 4: vertical from (2,2) to (5,2)
        placeShip(new Ship(2, 2, 4, false));

        // Length 3: horizontal from (4,5) to (4,7)
        placeShip(new Ship(4, 5, 3, true));

        // Length 2: horizontal from (6,1) to (6,2)
        placeShip(new Ship(6, 1, 2, true));

        // Length 2: horizontal from (9,8) to (9,9)
        placeShip(new Ship(9, 8, 2, true));

        setChanged();
        notifyObservers();
    }

    public void placeShip(Ship ship) {
        int row = ship.getStartRow();
        int col = ship.getStartCol();

        assert row >= 0 && row < BOARD_SIZE : "Row index out of bounds";
        assert col >= 0 && col < BOARD_SIZE : "Column index out of bounds";

        if (ship.isHorizontal()) {
            if (col + ship.getLength() > BOARD_SIZE) {
                throw new IllegalArgumentException("Ship extends beyond the right edge of the board.");
            }
        } else {
            if (row + ship.getLength() > BOARD_SIZE) {
                throw new IllegalArgumentException("Ship extends beyond the bottom edge of the board.");
            }
        }

        // Check for overlapping
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
        assert row >= 0 && row < BOARD_SIZE : "Row index out of bounds in guess()";
        assert col >= 0 && col < BOARD_SIZE : "Column index out of bounds in guess()";

        tries++;

        if (board[row][col] == CellState.HIT || board[row][col] == CellState.MISS) {
            return false;
        }

        boolean hit = false;
        if (board[row][col] == CellState.SHIP) {
            board[row][col] = CellState.HIT;
            hit = true;
            for (Ship s : ships) {
                if (isPartOfShip(s, row, col)) {
                    s.registerHit();
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
        assert row >= 0 && row < BOARD_SIZE : "Row index out of bounds in getCellState()";
        assert col >= 0 && col < BOARD_SIZE : "Column index out of bounds in getCellState()";
        return board[row][col];
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

