package src.main;

public class Ship {
    private final int startRow;
    private final int startCol;
    private final int length;
    private final boolean horizontal;
    private int hits;

    public Ship(int startRow, int startCol, int length, boolean horizontal) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.length = length;
        this.horizontal = horizontal;
        this.hits = 0;
    }

    public int getLength() {
        return length;
    }

    public void registerHit() {
        hits++;
    }

    public boolean isSunk() {
        return hits >= length;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public boolean isHorizontal() {
        return horizontal;
    }
}
