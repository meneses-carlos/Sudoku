package com.meneses.carlos.sudoku.model;

/**
 * Represents a move made by the player on the Sudoku board.
 * Stored in the move history Stack to allow undoing actions.
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 * @version 1.0
 */
public class Move {

    private final int row;
    private final int col;
    private final int previousValue;

    /**
     * Constructs a move recording the previous state of a cell.
     *
     * @param row           row of the modified cell (0-5)
     * @param col           column of the modified cell (0-5)
     * @param previousValue value the cell had BEFORE the change
     */
    public Move(int row, int col, int previousValue) {
        this.row = row;
        this.col = col;
        this.previousValue = previousValue;
    }

    /**
     * Returns the row of the modified cell.
     * @return row index (0-5)
     */
    public int getRow() { return row; }

    /**
     * Returns the column of the modified cell.
     * @return column index (0-5)
     */
    public int getCol() { return col; }

    /**
     * Returns the value the cell had before this move.
     * Used to restore the cell when undoing.
     * @return previous cell value (0-6)
     */
    public int getPreviousValue() { return previousValue; }
}
