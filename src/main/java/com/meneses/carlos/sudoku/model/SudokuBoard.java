package com.meneses.carlos.sudoku.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents the 6x6 Sudoku board.
 * Manages the grid of cells, validates moves according to Sudoku rules,
 * and keeps track of the move history.
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 */
public class SudokuBoard {
    private List<Cell> board;
    private Stack<Move> history;

    /**
     * Constructs an empty 6x6 Sudoku board and initializes the move history.
     */
    public SudokuBoard() {
        board = new ArrayList<>(36);
        history = new Stack<>();

        // Initialize 36 empty cells for a 6x6 grid
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                board.add(new Cell(row, col, 0, false));
            }
        }
    }

    /**
     * Calculates the 1D list index from 2D coordinates.
     *
     * @param row The row index (0 to 5).
     * @param col The column index (0 to 5).
     * @return The index in the 1D list.
     */
    private int getIndex(int row, int col) {
        return row * 6 + col;
    }

    /**
     * Gets the cell at the specified row and column.
     *
     * @param row The row index (0 to 5).
     * @param col The column index (0 to 5).
     * @return The Cell object.
     */
    public Cell getCell(int row, int col) {
        return board.get(getIndex(row, col));
    }

    /**
     * Sets a value in the board and saves the move to history.
     *
     * @param row   The row index.
     * @param col   The column index.
     * @param value The new value (1-6, or 0 to clear).
     */
    public void setValue(int row, int col, int value) {
        Cell cell = getCell(row, col);
        if (!cell.isFixed() && cell.getValue() != value) {
            // Save the current state to history before changing it
            history.push(new Move(row, col, cell.getValue()));
            cell.setValue(value);
        }
    }

    /**
     * Undoes the last move if the history is not empty.
     */
    public void undo() {
        if (!history.isEmpty()) {
            Move lastMove = history.pop();
            Cell cell = getCell(lastMove.getRow(), lastMove.getCol());
            cell.setValue(lastMove.getPreviousValue());
        }
    }

    /**
     * Validates if placing a specific value at a given position follows the Sudoku rules.
     * The grid is 6x6, so rules apply to rows, columns, and 2x3 blocks.
     *
     * @param row   The row index (0 to 5).
     * @param col   The column index (0 to 5).
     * @param value The value to check (1-6).
     * @return True if the move is valid, false otherwise.
     */
    public boolean isValidMove(int row, int col, int value) {
        // 1. Check row for duplicates
        for (int c = 0; c < 6; c++) {
            if (c != col && getCell(row, c).getValue() == value) {
                return false;
            }
        }

        // 2. Check column for duplicates
        for (int r = 0; r < 6; r++) {
            if (r != row && getCell(r, col).getValue() == value) {
                return false;
            }
        }

        // 3. Check 2x3 block for duplicates
        int startRow = (row / 2) * 2;
        int startCol = (col / 3) * 3;

        for (int r = startRow; r < startRow + 2; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if ((r != row || c != col) && getCell(r, c).getValue() == value) {
                    return false;
                }
            }
        }

        return true;
    }
}