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
    private List<Cell> board;
    private Stack<Move> history;

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
     * Loads an initial puzzle into the board, marking given cells as fixed.
     * Each entry in the array is {row, col, value}.
     * Resets move history.
     *
     * @param clues A 2D array where each row is {row, col, value} of a fixed cell.
     */
    public void loadPuzzle(int[][] clues) {
        // Reset the board
        for (Cell cell : board) {
            cell.setValue(0);
        }
        history.clear();

        for (int[] clue : clues) {
            int row = clue[0], col = clue[1], value = clue[2];
            Cell cell = getCell(row, col);
            cell.setValue(value);
            // Re-create the cell as fixed (replace in list)
            board.set(getIndex(row, col), new Cell(row, col, value, true));
        }
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
     * Undoes the last player move, restoring the previous cell value.
     * Does nothing if there is no move history.
     */
    public void undo() {
        if (!history.isEmpty()) {
            Move lastMove = history.pop();
            Cell cell = getCell(lastMove.getRow(), lastMove.getCol());
            cell.setValue(lastMove.getPreviousValue());
        }
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
     * Checks if the board is fully and correctly completed.
     * Every cell must be non-empty and its value must pass validation.
     *
     * @return True if all 36 cells are filled with valid values.
     */
    public boolean isBoardComplete() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                Cell cell = getCell(row, col);
                if (cell.isEmpty() || !isValidMove(row, col, cell.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Finds the first empty cell and returns a valid number that can be placed there.
     * Returns null if no hint is available (board is full or no valid number exists).
     *
     * @return An int array {row, col, value} representing the hint, or null if unavailable.
     */
    public int[] getHint() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                Cell cell = getCell(row, col);
                if (cell.isEmpty()) {
                    for (int value = 1; value <= 6; value++) {
                        if (isValidMove(row, col, value)) {
                            return new int[]{row, col, value};
                        }
                    }
                }
            }
        }
        return null;
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
     * Generates a new valid starting board according to the game rules.
     * Fills each 2x3 block with exactly 2 valid numbers and marks them as fixed.
     */
    public void generateStartingBoard() {
        Random random = new Random();
        boolean validBoardGenerated = false;

        while (!validBoardGenerated) {
            clearBoard();
            validBoardGenerated = true;

            // Iterate over the 6 blocks (3 block rows, 2 block cols)
            for (int blockRow = 0; blockRow < 3; blockRow++) {
                for (int blockCol = 0; blockCol < 2; blockCol++) {
                    int numbersPlaced = 0;
                    int attempts = 0; // Guard against infinite loops

                    // Try to place exactly 2 numbers in the current block
                    while (numbersPlaced < 2 && attempts < 50) {
                        // Calculate random row (0-1) and col (0-2) within the block
                        int r = (blockRow * 2) + random.nextInt(2);
                        int c = (blockCol * 3) + random.nextInt(3);

                        Cell cell = getCell(r, c);

                        // If the cell is empty, try a random number
                        if (cell.getValue() == 0) {
                            int num = random.nextInt(6) + 1; // 1 to 6

                            // Check if the random number is valid in this position
                            if (isValidMove(r, c, num)) {
                                // Replace the cell with a fixed one
                                board.set(getIndex(r, c), new Cell(r, c, num, true));
                                numbersPlaced++;
                            }
                        }
                        attempts++;
                    }

                    // If we couldn't place 2 numbers, the board is stuck. Restart.
                    if (numbersPlaced < 2) {
                        validBoardGenerated = false;
                        break;
                    }
                }
                if (!validBoardGenerated) break; // Break outer loop to restart
            }
        }
    }

}
