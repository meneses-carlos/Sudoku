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
 * @version 2.1
 */
public class SudokuGame {


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
        if (listener == null) return;

        if (value == 0) return; // clearing a cell, no event needed

        if (board.isValidMove(row, col, value)) {
            listener.onValidMove(row, col, value);
        } else {
            listener.onInvalidMove(row, col, value);
        }

        if (board.isBoardComplete()) {
            listener.onGameWon();
        }
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
     * chosen empty cell. Hints are unlimited, but cannot be used when only
     * one empty cell remains — the player must complete the last move manually
     * to win the game.
     *
     * @return The {@link Cell} that was filled with the hint,
     *         or null if only one (or zero) empty cells remain.
     * @author Jorge Navia
     * @author Carlos Meneses
     */
    public Cell getHint() {
        List<Cell> emptyCells = new ArrayList<>();
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                Cell cell = board.getCell(r, c);
                if (cell.getValue() == 0 && !cell.isFixed()) {
                    emptyCells.add(cell);
                }
            }
        }

        // Block hint if only one empty cell remains: the player must win manually
        if (emptyCells.size() <= 1) {
            if (listener != null) listener.onHintsExhausted();
            return null;
        }

        Collections.shuffle(emptyCells, new Random());
        Cell chosen = emptyCells.get(0);
        int correctValue = solution[chosen.getRow()][chosen.getCol()];

        board.setValue(chosen.getRow(), chosen.getCol(), correctValue);

        if (listener != null) {
            listener.onHintUsed(chosen, emptyCells.size() - 2);
        }

        return chosen;
    }
    /**
     * Returns the number of empty cells that can still receive a hint.
     * The last empty cell is always excluded — the player must fill it manually.
     *
     * @return Number of cells available for hints (empty cells minus 1).
     * @author Jorge Navia
     * @author Carlos Meneses
     */
    public int getRemainingHints() {
        int count = 0;
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                if (board.getCell(r, c).getValue() == 0) count++;
            }
        }
        return Math.max(0, count - 1);
    }
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

        // Advance past the last column - move to next row
        if (col == 6) {
            col = 0;
            row++;
        }

        // All 6 rows filled - solution is complete
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

        // Top-left corner of each of the six 2x3 blocks
        BlockOrigin[] blockOrigins = {
                new BlockOrigin(0, 0), new BlockOrigin(0, 3),
                new BlockOrigin(2, 0), new BlockOrigin(2, 3),
                new BlockOrigin(4, 0), new BlockOrigin(4, 3)
        };

        Random random = new Random();

        for (BlockOrigin origin : blockOrigins) {
            int startRow = origin.row();
            int startCol = origin.col();

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

    /**
     * Top-left coordinate of one of the six 2x3 blocks, used to distribute
     * the initial clues evenly across the board in {@link #applyInitialClues()}.
     *
     * @author Jorge Navia
     */
    private static final class BlockOrigin {

        /** Row of the block's top-left cell. */
        private final int row;

        /** Column of the block's top-left cell. */
        private final int col;

        /**
         * Constructs a new block origin.
         *
         * @param row Row of the block's top-left cell.
         * @param col Column of the block's top-left cell.
         */
        private BlockOrigin(int row, int col) {
            this.row = row;
            this.col = col;
        }

        /**
         * Returns the row of this block's top-left cell.
         *
         * @return Row index.
         */
        private int row() {
            return row;
        }

        /**
         * Returns the column of this block's top-left cell.
         *
         * @return Column index.
         */
        private int col() {
            return col;
        }
    }

    /**
     * Listener used to notify the controller about
     * game-related events such as valid moves,
     * invalid moves, hints and victory conditions.
     */
    private GameEventListener listener;

    /**
     * Registers a listener to receive game event callbacks.
     *
     * @param listener The {@link GameEventListener} implementation to notify.
     * @author Jorge Navia
     */
    public void setGameEventListener(GameEventListener listener) {
        this.listener = listener;
    }

}
