package src.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class BattleshipController implements ActionListener {
    private final BattleshipModel model;
    private final BattleshipView view;

    public BattleshipController(BattleshipModel model, BattleshipView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Handle reset game action
        if ("RESET_GAME".equals(command)) {
            // Reset, new random board
            model.resetGame();
            model.initializeRandomBoard();
            view.updateBoard(model);
            return;
        }

        // Otherwise, assume it's a board cell click
        String[] parts = command.split(",");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);

        boolean hit = model.guess(row, col);

        // Update cell in view
        view.updateCell(row, col, model.getCellState(row, col));

        // Check game state and show message if all ships are sunk
        if (model.isGameOver()) {
            JOptionPane.showMessageDialog(view,
                    "All ships sunk! You used " + model.getTries() + " tries.");
        }
    }
}
