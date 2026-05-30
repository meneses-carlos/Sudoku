package com.meneses.carlos.sudoku.model;

/**
 * Represents an individual cell of the 6x6 Sudoku board.
 * Each cell knows its position, current value, and whether
 * it is fixed (given by the game) or editable (entered by the player).
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 * @version 1.0
 */
public class Cell {

    private final int row;
    private final int col;
    private int value;
    private final boolean fixed;

    /**
     * Constructs a cell with its position and initial value.
     *
     * @param row   row index of the cell (0-5)
     * @param col   column index of the cell (0-5)
     * @param value initial value (0 if empty, 1-6 if fixed)
     * @param fixed true if this cell is given by the game and cannot be edited
     */
    public Cell(int row, int col, int value, boolean fixed) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.fixed = fixed;
    }

    /**
     * Returns the row index of this cell.
     * @return row index (0-5)
     */
    public int getRow() { return row; }

    /**
     * Returns the column index of this cell.
     * @return column index (0-5)
     */
    public int getCol() { return col; }

    /**
     * Returns the current value of this cell.
     * @return value from 1 to 6, or 0 if empty
     */
    public int getValue() { return value; }

    /**
     * Returns whether this cell is fixed and cannot be edited.
     * @return true if the cell is fixed
     */
    public boolean isFixed() { return fixed; }

    /**
     * Sets a new value for this cell. No effect if the cell is fixed.
     * @param value value to assign (0 to clear, 1-6 for a number)
     */
    public void setValue(int value) {
        if (!fixed) {
            this.value = value;
        }
    }

    /**
     * Returns whether this cell has no value assigned.
     * @return true if value == 0
     */
    public boolean isEmpty() { return value == 0; }

    /**
     * Returns the linear index of this cell in the board list.
     * Formula: row * 6 + col.
     * @return index from 0 to 35
     */
    public int getIndex() { return row * 6 + col; }

    /**
     * Returns the 2x3 block number this cell belongs to.
     * Blocks are numbered 0-5, left to right, top to bottom.
     * @return block number from 0 to 5
     */
    public int getBlock() { return (row / 2) * 3 + (col / 3); }

    @Override
    public String toString() {
        return "Cell[" + row + "," + col + "]=" + value + (fixed ? "(fixed)" : "");
    }
}
