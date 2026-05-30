package com.meneses.carlos.sudoku.model;

/**
 * Represents a single cell in the 6x6 Sudoku grid.
 * This class stores the cell's position, its current value, and whether it is a fixed starting number.
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
     * @param row   The row index of the cell (0 to 5).
     * @param col   The column index of the cell (0 to 5).
     * @param value The initial value of the cell (1-6, or 0 if empty).
     * @param fixed True if the cell is part of the initial puzzle and cannot be modified.
     */
    public Cell(int row, int col, int value, boolean fixed) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.fixed = fixed;
    }

    /**
     * Gets the row index of the cell.
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index of the cell.
     * @return The column index.
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the current value of the cell.
     * @return The current value (0 if empty).
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets a new value for the cell.
     * @param value The new value to set (1-6, or 0 if cleared).
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Checks if the cell is a fixed starting number.
     * @return True if the cell is fixed, false otherwise.
     */
    public boolean isFixed() {
        return fixed;
    }
}