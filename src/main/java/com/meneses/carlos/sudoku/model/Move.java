package com.meneses.carlos.sudoku.model;

/**
 * Represents a move made by the player in the Sudoku grid.
 * Used to keep track of the move history for potential undo features.
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 */
public class Move {
    private int row;
    private int col;
    private int previousValue;

    /**
     * Constructs a new Move to record a change in the grid.
     *
     * @param row           The row index of the modified cell.
     * @param col           The column index of the modified cell.
     * @param previousValue The value the cell held before this move was made.
     */
    public Move(int row, int col, int previousValue) {
        this.row = row;
        this.col = col;
        this.previousValue = previousValue;
    }

    /**
     * Gets the row index of the cell that was changed.
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index of the cell that was changed.
     * @return The column index.
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the value the cell held before it was modified.
     * @return The previous value.
     */
    public int getPreviousValue() {
        return previousValue;
    }
}