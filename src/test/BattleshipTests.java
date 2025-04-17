package src.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import src.main.BattleshipModel;
import src.main.CellState;
import src.main.Ship;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BattleshipTests {

    @Test
    public void testPlaceShipAndSink() {
        // start with an empty model and place one ship of length 3 at (0,0) horizontally
        BattleshipModel model = new BattleshipModel();
        model.resetGame();
        Ship ship = new Ship(0, 0, 3, true);
        model.placeShip(ship);

        // guess each part of the ship
        assertTrue(model.guess(0, 0));
        assertTrue(model.guess(0, 1));
        assertTrue(model.guess(0, 2));

        // after the third hit, a sunk message should appear
        String sunkMsg = model.getLastSunkMessage();
        assertEquals("Ship sunk! (Length: 3)", sunkMsg);

        // no ships remain, tries should be 3
        assertEquals(0, model.getShipsRemaining());
        assertEquals(3, model.getTries());
    }

    @Test
    public void testLoadFromFileValid(@TempDir Path tempDir) throws Exception {
        // create a temp file defining two ships:
        // a length-2 ship at (0,0) horizontal
        // a length-3 ship at (1,2) vertical
        Path config = tempDir.resolve("ships.txt");
        Files.write(config, Arrays.asList(
                "0 0 2 H",
                "1 2 3 V"
        ));

        BattleshipModel model = new BattleshipModel();
        model.loadFromFile(config.toString());

        // after loading, there should be 2 ships and no guesses
        assertEquals(2, model.getShipsRemaining());
        assertEquals(0, model.getTries());

        // verify the correct cells are marked as SHIP
        assertEquals(CellState.SHIP, model.getCellState(0, 0));
        assertEquals(CellState.SHIP, model.getCellState(0, 1));
        assertEquals(CellState.SHIP, model.getCellState(1, 2));
        assertEquals(CellState.SHIP, model.getCellState(2, 2));
        assertEquals(CellState.SHIP, model.getCellState(3, 2));
    }

    @Test
    public void testRandomBoardInvariants() {
        // generate a random board and verify invariants:
        // 5 ships in total, with a total of 16 ship cells (5+4+3+2+2)
        BattleshipModel model = new BattleshipModel();
        model.initializeRandomBoard();

        assertEquals(5, model.getShipsRemaining());

        int shipCells = 0;
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (model.getCellState(r, c) == CellState.SHIP) {
                    shipCells++;
                }
            }
        }
        assertEquals(16, shipCells);
    }
}
