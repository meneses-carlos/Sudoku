package com.meneses.carlos.sudoku.view;

/**
 * Utility class that builds the CSS style strings applied to the
 * Sudoku board cells.
 *
 * <p>Centralizes all visual styling rules so the controller stays
 * focused on event handling instead of duplicating inline CSS.
 *
 * @author Jorge Navia
 */
public final class CellStyler {

    private CellStyler() {
    }

    /**
     * Returns the base style for a cell, adding a thicker border
     * on block boundaries to visually separate the 2x3 blocks.
     *
     * @param row Row index (0-5).
     * @param col Column index (0-5).
     * @return A CSS style string.
     */
    public static String baseStyle(int row, int col) {
        String top = "1";
        String left = "1";
        String bottom = "1";
        String right = "1";

        if (row == 0 || row == 2 || row == 4) top = "4";
        if (col == 0 || col == 3) left = "4";
        if (row == 5) bottom = "4";
        if (col == 5) right = "4";

        return "-fx-border-color: #ff6666; "
                + "-fx-border-width: "
                + top + " "
                + right + " "
                + bottom + " "
                + left + "; "
                + "-fx-font-size: 18px; "
                + "-fx-alignment: center;";
    }

    /**
     * Returns the style applied to the cell the player just selected.
     *
     * @param row Row index (0-5).
     * @param col Column index (0-5).
     * @return A CSS style string with a highlighted border.
     */
    public static String selectedStyle(int row, int col) {
        return baseStyle(row, col)
                + "-fx-background-color: #330000; "
                + "-fx-border-color: #ff5555; "
                + "-fx-border-width: 2;";
    }

    /**
     * Returns the style applied to a cell after an invalid move.
     *
     * @param row Row index (0-5).
     * @param col Column index (0-5).
     * @return A CSS style string with a warning border.
     */
    public static String invalidStyle(int row, int col) {
        return baseStyle(row, col)
                + "-fx-background-color: #140000; "
                + "-fx-text-fill: white; "
                + "-fx-border-color: #ffd700; "
                + "-fx-border-width: 3;";
    }

    /**
     * Returns the style applied to a fixed clue cell.
     *
     * @param row Row index (0-5).
     * @param col Column index (0-5).
     * @return A CSS style string for a non-editable clue.
     */
    public static String fixedStyle(int row, int col) {
        return "-fx-background-color: #2a0000; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: white; "
                + baseStyle(row, col);
    }

    /**
     * Returns the style applied to a cell that was just revealed by a hint.
     *
     * @return A CSS style string highlighting the hinted cell.
     */
    public static String hintStyle() {
        return "-fx-background-color: #550000; "
                + "-fx-text-fill: #ffcccc; "
                + "-fx-font-weight: bold;";
    }
}
