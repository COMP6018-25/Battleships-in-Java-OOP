package src.main;

import src.main.BattleshipController;
import src.main.BattleshipModel;
import src.main.CellState;

import javax.swing.*;
import java.awt.*;

public class BattleshipView extends JFrame {
    public static final int BOARD_SIZE = 10;
    private final JButton[][] buttons;
    private final JButton resetButton; // Reset button
    private final JPanel boardPanel;
    private final JPanel controlPanel;
    private final JLabel scoreboardLabel;

    public BattleshipView() {
        setTitle("Battleships (GUI)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for scoreboard
        JPanel scoreboardPanel = new JPanel();
        scoreboardLabel = new JLabel("Guesses: 0");
        scoreboardPanel.add(scoreboardLabel);
        add(scoreboardPanel, BorderLayout.NORTH);

        // Board panel in the center
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                JButton btn = new JButton();
                btn.setActionCommand(r + "," + c);
                buttons[r][c] = btn;
                boardPanel.add(btn);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Control panel at the bottom with reset button
        controlPanel = new JPanel();
        resetButton = new JButton("Reset Game");
        resetButton.setActionCommand("RESET_GAME");
        controlPanel.add(resetButton);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addController(BattleshipController controller) {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                buttons[r][c].addActionListener(controller);
            }
        }
        resetButton.addActionListener(controller);
    }

    public void updateCell(int row, int col, CellState state) {
        JButton btn = buttons[row][col];
        switch (state) {
            case HIT:
                btn.setText("H");
                btn.setBackground(Color.RED);
                break;
            case MISS:
                btn.setText("M");
                btn.setBackground(Color.LIGHT_GRAY);
                break;
            default:
                btn.setText("");
                btn.setBackground(null);
                break;
        }
    }

    public void updateBoard(BattleshipModel model) {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                updateCell(r, c, model.getCellState(r, c));
            }
        }
    }

    // update the scoreboard with the current number of guesses
    public void updateScoreboard(BattleshipModel model) {
        scoreboardLabel.setText("Guesses: " + model.getTries());
    }
}
