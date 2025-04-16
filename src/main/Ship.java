package src.main;

public class Ship {
    private final int startRow;
    private final int startCol;
    private final int length;
    private final boolean horizontal;
    private int hits;
    private boolean sunkAnnounced;

    public Ship(int startRow, int startCol, int length, boolean horizontal) {
        assert startRow >= 0 : "Start row must be non-negative";
        assert startCol >= 0 : "Start column must be non-negative";
        assert length > 0 : "Ship length must be positive";

        this.startRow = startRow;
        this.startCol = startCol;
        this.length = length;
        this.horizontal = horizontal;
        this.hits = 0;
        this.sunkAnnounced = false; // initialize to false
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

    public boolean justSunk() {
        if (isSunk() && !sunkAnnounced) {
            sunkAnnounced = true;
            return true;
        }
        return false;
    }
}
