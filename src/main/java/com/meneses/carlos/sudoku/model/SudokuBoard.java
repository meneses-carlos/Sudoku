package com.meneses.carlos.sudoku.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Represents the complete 6x6 Sudoku board.
 * Contains the two main data structures of the project:
 * <ul>
 *   <li>{@code List<Cell>} - all 36 cells of the board</li>
 *   <li>{@code Stack<Move>} - move history to support undo functionality</li>
 * </ul>
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 * @version 1.0
 */
public class SudokuBoard {

    /** The 36 cells of the board. Cell at (row,col) is at index row*6+col. */
    private final List<Cell> cells;

    /** History of player moves. Supports undo via pop(). */
    private final Stack<Move> moveHistory;

    /**
     * Constructs an empty 6x6 board.
     * All cells are initialized with value 0 and marked as non-fixed.
     */
    public SudokuBoard() {
        cells = new ArrayList<>();
        moveHistory = new Stack<>();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                cells.add(new Cell(row, col, 0, false));
            }
        }
    }

    /**
     * Returns the cell at the given position.
     *
     * @param row row index (0-5)
     * @param col column index (0-5)
     * @return the cell at that position
     */
    public Cell getCell(int row, int col) {
        return cells.get(row * 6 + col);
    }

    /**
     * Sets a value in an editable cell and records the move in history.
     *
     * @param row   row index (0-5)
     * @param col   column index (0-5)
     * @param value value to set (1-6, or 0 to clear)
     */
    public void setValue(int row, int col, int value) {
        Cell cell = getCell(row, col);
        if (!cell.isFixed()) {
            moveHistory.push(new Move(row, col, cell.getValue()));
            cell.setValue(value);
        }
    }

    /**
     * Undoes the last player move using the move history Stack.
     *
     * @return true if a move was undone, false if history is empty
     */
    public boolean undoLastMove() {
        if (moveHistory.isEmpty()) return false;
        Move last = moveHistory.pop();
        getCell(last.getRow(), last.getCol()).setValue(last.getPreviousValue());
        return true;
    }

    /**
     * Returns all cells in the specified row.
     *
     * @param row row index (0-5)
     * @return list of 6 cells in that row
     */
    public List<Cell> getRow(int row) {
        return cells.stream()
                .filter(c -> c.getRow() == row)
                .collect(Collectors.toList());
    }

    /**
     * Returns all cells in the specified column.
     *
     * @param col column index (0-5)
     * @return list of 6 cells in that column
     */
    public List<Cell> getCol(int col) {
        return cells.stream()
                .filter(c -> c.getCol() == col)
                .collect(Collectors.toList());
    }

    /**
     * Returns all cells in the specified 2x3 block.
     *
     * @param block block number (0-5)
     * @return list of 6 cells in that block
     */
    public List<Cell> getBlock(int block) {
        return cells.stream()
                .filter(c -> c.getBlock() == block)
                .collect(Collectors.toList());
    }

    /**
     * Returns the full list of all 36 cells on the board.
     *
     * @return list of all cells
     */
    public List<Cell> getCells() {
        return cells;
    }

    /**
     * Resets all editable cells to 0 and clears the move history.
     */
    public void clear() {
        cells.forEach(c -> {
            if (!c.isFixed()) c.setValue(0);
        });
        moveHistory.clear();
    }

    /**
     * Returns whether there are moves available to undo.
     *
     * @return true if move history is not empty
     */
    public boolean canUndo() {
        return !moveHistory.isEmpty();
    }
}
