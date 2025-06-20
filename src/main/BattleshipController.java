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
            model.resetGame();
            model.initializeRandomBoard();
            view.updateBoard(model);
            view.updateScoreboard(model);
            return;
        }

        // Otherwise, assume it's a board cell click
        String[] parts = command.split(",");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);

        boolean hit = model.guess(row, col);

        // Update cell in view
        view.updateCell(row, col, model.getCellState(row, col));
        view.updateScoreboard(model);

        // Check for sunk ship message and show it
        String sunkMsg = model.getLastSunkMessage();
        if (!sunkMsg.isEmpty()) {
            JOptionPane.showMessageDialog(view, sunkMsg);
        }

        // Check game state and show overall end-of-game message if needed
        if (model.isGameOver()) {
            JOptionPane.showMessageDialog(view,
                    "All ships sunk! You used " + model.getTries() + " tries.");
        }
    }

}
