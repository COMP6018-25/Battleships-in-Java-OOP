package src.main;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class BattleshipView extends JFrame implements Observer {
    public static final int BOARD_SIZE = 10;
    private final JButton[][] buttons;
    private final JButton resetButton;
    private final JPanel boardPanel;
    private final JPanel controlPanel;
    private final JLabel scoreboardLabel;
    private BattleshipModel model;

    public BattleshipView(BattleshipModel model) {
        this.model = model;
        model.addObserver(this);

        setTitle("Battleships (GUI)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for scoreboard
        JPanel scoreboardPanel = new JPanel();
        scoreboardLabel = new JLabel("Guesses: 0");
        scoreboardPanel.add(scoreboardLabel);
        add(scoreboardPanel, BorderLayout.NORTH);

        // Center grid board
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                JButton btn = new JButton();
                btn.setActionCommand(r + "," + c);
                btn.setPreferredSize(new Dimension(50, 50));
                buttons[r][c] = btn;
                boardPanel.add(btn);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Bottom control panel with reset
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

    public void updateScoreboard(BattleshipModel model) {
        scoreboardLabel.setText("Guesses: " + model.getTries());
    }

    @Override
    public void update(Observable o, Object arg) {
        updateBoard(model);
        updateScoreboard(model);
    }
}
