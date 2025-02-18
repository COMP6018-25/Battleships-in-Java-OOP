package src.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class BattleshipModel extends Observable {
    private static final int BOARD_SIZE = 10;

    // 10x10 board
    private final CellState[][] board;

    // Ships in the game
    private final List<Ship> ships;

    // Guesses made
    private int tries;


    public BattleshipModel() {
        board = new CellState[BOARD_SIZE][BOARD_SIZE];
        ships = new ArrayList<>();
        tries = 0;
        initializeEmptyBoard();
    }

    // Create EMPTY board
    private void initializeEmptyBoard() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                assert r >= 0 && r < BOARD_SIZE : "Row index out of bounds";
                assert c >= 0 && c < BOARD_SIZE : "Column index out of bounds";
                board[r][c] = CellState.EMPTY;
            }
        }
    }
}
