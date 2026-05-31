package com.meneses.carlos.sudoku.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Random;

/**
 * Represents the 6x6 Sudoku board.
 * Manages the grid of cells, validates moves, checks completion,
 * provides hints, and maintains the move history for undo operations.
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 */
public class SudokuBoard {
    private final List<Cell> board;
    private final Stack<Move> history;

    /**
     * Constructs an empty 6x6 Sudoku board and initializes the move history.
     */
    public SudokuBoard() {
        board = new ArrayList<>(36);
        history = new Stack<>();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                board.add(new Cell(row, col, 0, false));
            }
        }
    }

    /**
     * Calculates the 1D index from 2D coordinates.
     *
     * @param row Row index (0–5).
     * @param col Column index (0–5).
     * @return The corresponding index in the flat list.
     */
    private int getIndex(int row, int col) {
        return row * 6 + col;
    }

    /**
     * Returns the cell at the given row and column.
     *
     * @param row Row index (0–5).
     * @param col Column index (0–5).
     * @return The Cell at that position.
     */
    public Cell getCell(int row, int col) {
        return board.get(getIndex(row, col));
    }


    /**
     * Attempts to set a value in a non-fixed cell and saves the move to history.
     *
     * @param row   Row index (0–5).
     * @param col   Column index (0–5).
     * @param value New value (1–6, or 0 to clear).
     * @return True if the value was set, false if the cell is fixed.
     */
    public boolean setValue(int row, int col, int value) {
        Cell cell = getCell(row, col);
        if (cell.isFixed()) {
            return false;
        }
        if (cell.getValue() != value) {
            history.push(new Move(row, col, cell.getValue()));
            cell.setValue(value);
        }
        return true;
    }

    /**
     * Undoes the last move if the history is not empty.
     *
     * @return The Move that was undone, or null if there was nothing to undo.
     * @author Jorge Navia
     * @author Carlos Meneses
     */
    public Move undo() {
        if (!history.isEmpty()) {
            Move lastMove = history.pop();
            Cell cell = getCell(lastMove.getRow(), lastMove.getCol());
            cell.setValue(lastMove.getPreviousValue());
            return lastMove;
        }
        return null;
    }
    /**
     * Validates whether placing a value at the given position obeys Sudoku rules.
     * Checks the row, column, and 2x3 block for duplicates.
     *
     * @param row   Row index (0–5).
     * @param col   Column index (0–5).
     * @param value Value to check (1–6).
     * @return True if the move is valid, false if it violates a rule.
     */
    public boolean isValidMove(int row, int col, int value) {
        for (int c = 0; c < 6; c++) {
            if (c != col && getCell(row, c).getValue() == value) return false;
        }
        for (int r = 0; r < 6; r++) {
            if (r != row && getCell(r, col).getValue() == value) return false;
        }
        int startRow = (row / 2) * 2;
        int startCol = (col / 3) * 3;
        for (int r = startRow; r < startRow + 2; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if ((r != row || c != col) && getCell(r, c).getValue() == value) return false;
            }
        }
        return true;
    }

    /**
     * Clears the board completely, removing all numbers and history.
     * Sets all cells back to empty and not fixed.
     */
    public void clearBoard() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                board.set(getIndex(row, col), new Cell(row, col, 0, false));
            }
        }
        history.clear();
    }
    /**
     * Checks whether the entire board is correctly and completely filled.
     * A board is complete when all 36 cells have a non-zero value and
     * every value is valid according to Sudoku rules.
     *
     * @return True if the board is fully and correctly solved, false otherwise.
     * @author Jorge Navia
     * @author Carlos Meneses
     */
    public boolean isBoardComplete() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int val = getCell(row, col).getValue();
                if (val == 0) return false;
                if (!isValidMove(row, col, val)) return false;
            }
        }
        return true;
    }

}
