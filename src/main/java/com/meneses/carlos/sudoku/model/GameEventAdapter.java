package com.meneses.carlos.sudoku.model;

/**
 * Convenience adapter for {@link GameEventListener}.
 *
 * <p>Provides empty default implementations for every callback so that
 * concrete listeners only need to override the events they actually
 * care about, instead of implementing the full interface each time.
 *
 * @author Jorge Navia
 * @version 1.0
 */
public abstract class GameEventAdapter implements GameEventListener {

    /** {@inheritDoc} */
    @Override
    public void onValidMove(int row, int col, int value) {
    }

    /** {@inheritDoc} */
    @Override
    public void onInvalidMove(int row, int col, int value) {
    }

    /** {@inheritDoc} */
    @Override
    public void onGameWon() {
    }

    /** {@inheritDoc} */
    @Override
    public void onHintUsed(Cell cell, int remainingHints) {
    }

    /** {@inheritDoc} */
    @Override
    public void onHintsExhausted() {
    }
}
