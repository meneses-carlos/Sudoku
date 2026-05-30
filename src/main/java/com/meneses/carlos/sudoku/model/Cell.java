package com.meneses.carlos.sudoku.model;

/**
 * Represents a single cell in the 6x6 Sudoku grid.
 * Stores the cell's position, current value, and whether it is a fixed starting number.
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 */
public class Cell {
    private int row;
    private int col;
    private int value;
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

    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getValue() { return value; }
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
}