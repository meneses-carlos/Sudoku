package com.meneses.carlos.sudoku.model;

/**
 * Interface that defines the contract for listening to Sudoku game events.
 * Implemented by the controller to react to model state changes,
 * keeping the Model layer decoupled from the View layer.
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 * @version 1.1
 */
public interface GameEventListener {

    /**
     * Called when the player enters a valid number in a cell.
     *
     * @param row   Row index of the modified cell (0-5).
     * @param col   Column index of the modified cell (0-5).
     * @param value The valid value that was entered.
     */
    void onValidMove(int row, int col, int value);

    /**
     * Called when the player enters a number that violates Sudoku rules.
     *
     * @param row   Row index of the modified cell (0-5).
     * @param col   Column index of the modified cell (0-5).
     * @param value The invalid value that was entered.
     */
    void onInvalidMove(int row, int col, int value);

    /**
     * Called when the player successfully completes the board.
     */
    void onGameWon();

    /**
     * Called when a hint is successfully provided to the player.
     *
     * @param cell           The cell that received the hint value.
     * @param remainingHints Number of hints still available.
     */
    void onHintUsed(Cell cell, int remainingHints);

    /**
     * Called when no more hints are available.
     */
    void onHintsExhausted();
}