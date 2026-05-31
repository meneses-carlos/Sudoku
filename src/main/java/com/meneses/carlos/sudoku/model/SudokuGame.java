package com.meneses.carlos.sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Facade class that manages the overall Sudoku game logic.
 *
 * <p>Workflow per new game:
 * <ol>
 *   <li>Generate a fully solved 6x6 board using backtracking.</li>
 *   <li>Store the complete solution.</li>
 *   <li>Build the playable board by revealing exactly 2 cells per 2x3 block.</li>
 * </ol>
 *
 * <p>The stored solution is also used to provide hints to the player.
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 */
public class SudokuGame {

    /** Maximum number of hints allowed per game. */
    private static final int MAX_HINTS = 3;

    /** The playable board shown to the player. */
    private SudokuBoard board;

    /**
     * The complete valid solution for the current game.
     * solution[row][col] holds the correct value for every cell.
     */
    private int[][] solution;

    /** Number of hints the player has already used this game. */
    private int hintsUsed;

    /**
     * Constructs a new SudokuGame. Does not start a game automatically;
     * call {@link #startNewGame()} to begin.
     */
    public SudokuGame() {
        this.board = new SudokuBoard();
        this.solution = new int[6][6];
        this.hintsUsed = 0;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Starts a new game: generates a fresh solved board, stores the solution,
     * and builds the playable board with 2 revealed cells per 2x3 block.
     *
     * @author Jorge Navia
     * @author Carlos Meneses
     */
    public void startNewGame() {
        hintsUsed = 0;

        // Step 1: fill solution[][] with a complete valid Sudoku
        solution = new int[6][6];
        generateSolution(0, 0);

        // Step 2: build the SudokuBoard from the solution,
        //         revealing only 2 cells per block
        board = new SudokuBoard();
        applyInitialClues();
    }

    /**
     * Returns the playable board for the controller to read and display.
     *
     * @return The current {@link SudokuBoard}.
     */
    public SudokuBoard getBoard() {
        return board;
    }

    /**
     * Places a player value on the board if the cell is not fixed.
     * Delegates to {@link SudokuBoard#setValue(int, int, int)}.
     *
     * @param row   Row index (0-5).
     * @param col   Column index (0-5).
     * @param value Value to place (1-6), or 0 to clear.
     */
    public void setValue(int row, int col, int value) {
        board.setValue(row, col, value);
    }

    /**
     * Validates whether a value can legally be placed at the given position.
     *
     * @param row   Row index (0-5).
     * @param col   Column index (0-5).
     * @param value Value to validate (1-6).
     * @return True if the move follows Sudoku rules, false otherwise.
     */
    public boolean isValidMove(int row, int col, int value) {
        return board.isValidMove(row, col, value);
    }

    /**
     * Undoes the last player move.
     *
     * @return The undone {@link Move}, or null if the history is empty.
     */
    public Move undo() {
        return board.undo();
    }

    /**
     * Checks whether the player has completely and correctly solved the board.
     *
     * @return True if all 36 cells are filled validly, false otherwise.
     */
    public boolean isGameWon() {
        return board.isBoardComplete();
    }

    /**
     * Provides a hint by revealing the correct solution value in a randomly
     * chosen empty cell. Limited to {@value #MAX_HINTS} uses per game.
     *
     * <p>The revealed cell is NOT marked as fixed, so the player can still
     * edit it, but the hint counter prevents solving the entire board this way.
     *
     * @return The {@link Cell} that was filled with the hint,
     *         or null if the limit has been reached or no empty cells remain.
     * @author Jorge Navia
     * @author Carlos Meneses
     */
    public Cell getHint() {
        if (hintsUsed >= MAX_HINTS) {
            return null;
        }

        // Collect all empty (value == 0), non-fixed cells
        List<Cell> emptyCells = new ArrayList<>();
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                Cell cell = board.getCell(r, c);
                if (cell.getValue() == 0 && !cell.isFixed()) {
                    emptyCells.add(cell);
                }
            }
        }

        if (emptyCells.isEmpty()) {
            return null;
        }

        // Pick a random empty cell and reveal its solution value
        Collections.shuffle(emptyCells, new Random());
        Cell chosen = emptyCells.get(0);
        int correctValue = solution[chosen.getRow()][chosen.getCol()];

        board.setValue(chosen.getRow(), chosen.getCol(), correctValue);
        hintsUsed++;

        return chosen;
    }

    /**
     * Returns how many hints the player has left this game.
     *
     * @return Remaining hints (MAX_HINTS - hintsUsed).
     */
    public int getRemainingHints() {
        return MAX_HINTS - hintsUsed;
    }

    // -------------------------------------------------------------------------
    // Private generation logic
    // -------------------------------------------------------------------------

    /**
     * Recursively fills {@code solution} using backtracking.
     * Cells are visited left-to-right, top-to-bottom.
     * For each cell a shuffled list of 1-6 is tried so every call
     * produces a different valid Sudoku.
     *
     * @param row Starting row for this recursive call.
     * @param col Starting column for this recursive call.
     * @return True if the board was fully and successfully filled.
     * @author Jorge Navia
     * @author Carlos Meneses
     */
    private boolean generateSolution(int row, int col) {

        // Advance past the last column → move to next row
        if (col == 6) {
            col = 0;
            row++;
        }

        // All 6 rows filled → solution is complete
        if (row == 6) {
            return true;
        }

        // Build a shuffled list of candidate values [1..6]
        List<Integer> candidates = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        Collections.shuffle(candidates, new Random());

        for (int value : candidates) {
            if (isSolutionMoveValid(row, col, value)) {
                solution[row][col] = value;

                // Recurse to the next cell
                if (generateSolution(row, col + 1)) {
                    return true; // Propagate success upward
                }

                // Backtrack: this value led to a dead end
                solution[row][col] = 0;
            }
        }

        return false; // Trigger backtracking in the caller
    }

    /**
     * Validates a candidate value in {@code solution} (not on the playable board).
     * Checks the row, column, and 2x3 block for duplicates.
     *
     * @param row   Row index (0-5).
     * @param col   Column index (0-5).
     * @param value Candidate value (1-6).
     * @return True if the value can legally be placed here.
     * @author Jorge Navia
     * @author Carlos Meneses
     */
    private boolean isSolutionMoveValid(int row, int col, int value) {

        // Check row
        for (int c = 0; c < 6; c++) {
            if (solution[row][c] == value) return false;
        }

        // Check column
        for (int r = 0; r < 6; r++) {
            if (solution[r][col] == value) return false;
        }

        // Check 2x3 block
        int startRow = (row / 2) * 2;
        int startCol = (col / 3) * 3;
        for (int r = startRow; r < startRow + 2; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (solution[r][c] == value) return false;
            }
        }

        return true;
    }

    /**
     * Builds the playable board from the stored solution.
     * For each of the six 2x3 blocks, 2 cells are chosen at random
     * and set as fixed clues; the remaining 4 cells stay empty (value 0).
     *
     * @author Jorge Navia
     * @author Carlos Meneses
     */
    private void applyInitialClues() {

        // Top-left corner (startRow, startCol) of each 2x3 block
        int[][] blockOrigins = {
                {0, 0}, {0, 3},
                {2, 0}, {2, 3},
                {4, 0}, {4, 3}
        };

        Random random = new Random();

        for (int[] origin : blockOrigins) {
            int startRow = origin[0];
            int startCol = origin[1];

            // List all 6 cell positions in this block
            List<int[]> positions = new ArrayList<>();
            for (int r = startRow; r < startRow + 2; r++) {
                for (int c = startCol; c < startCol + 3; c++) {
                    positions.add(new int[]{r, c});
                }
            }

            // Shuffle to pick 2 random positions as visible clues
            Collections.shuffle(positions, random);

            for (int i = 0; i < 2; i++) {
                int r = positions.get(i)[0];
                int c = positions.get(i)[1];
                Cell cell = board.getCell(r, c);
                cell.setValue(solution[r][c]);
                cell.setFixed(true);
            }
            // The other 4 positions remain value=0, fixed=false (playable)
        }
    }
}