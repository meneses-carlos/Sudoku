package com.meneses.carlos.sudoku.model;

/**
 * Represents a single cell in the 6x6 Sudoku grid.
 * Stores the cell's position, current value, and whether it is a fixed starting number.
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 * @version 1.2
 */
public class Cell {

    /** Row position of the cell (0-5). */
    private final int row;

    /** Column position of the cell (0-5). */
    private final int col;

    /** Current value stored in the cell. */
    private int value;

    /** Indicates whether the cell is a fixed clue. */
    private boolean fixed;

    /**
     * Constructs a new Cell with its position, initial value, and fixed status.
     *
     * @param row   The row index (0 to 5).
     * @param col   The column index (0 to 5).
     * @param value The initial value (1–6, or 0 if empty).
     * @param fixed True if the cell cannot be modified by the player.
     */
    public Cell(int row, int col, int value, boolean fixed) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.fixed = fixed;
    }

    /**
     * Returns the row index of this cell.
     *
     * @return Row position (0-5).
     */
    public int getRow() { return row; }

    /**
     * Returns the column index of this cell.
     *
     * @return Column position (0-5).
     */
    public int getCol() { return col; }

    /**
     * Returns the current value stored in the cell.
     *
     * @return Cell value.
     */
    public int getValue() { return value; }

    /**
     * Indicates whether the cell is fixed and cannot be edited.
     *
     * @return True if the cell is fixed.
     */
    public boolean isFixed() { return fixed; }

    /**
     * Returns true if the cell has no value assigned (value equals 0).
     *
     * @return True if the cell is empty.
     */
    public boolean isEmpty() {
        return value == 0;
    }

    /**
     * Sets a new value for the cell. Only accepts values from 0 (empty) to 6.
     * Throws an exception if the value is out of range.
     *
     * @param value The new value (0 to 6).
     * @throws IllegalArgumentException if value is outside the valid range.
     */
    public void setValue(int value) {
        if (value < 0 || value > 6) {
            throw new IllegalArgumentException("Cell value must be between 0 and 6, got: " + value);
        }
        this.value = value;
    }
    /**
     * Sets whether this cell is a fixed starting number.
     *
     * @param fixed True to mark as fixed, false otherwise.
     * @author Jorge Navia
     * @author Carlos Meneses
     */
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}