package com.meneses.carlos.sudoku.controller;

import com.meneses.carlos.sudoku.model.Cell;
import com.meneses.carlos.sudoku.model.GameEventAdapter;
import com.meneses.carlos.sudoku.model.Move;
import com.meneses.carlos.sudoku.model.SudokuGame;
import com.meneses.carlos.sudoku.view.CellStyler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import java.util.Optional;

/**
 * Controller for the main Sudoku view.
 * Connects the JavaFX UI with the {@link SudokuGame} model.
 *
 * <p>Extends {@link GameEventAdapter} instead of implementing
 * {@link com.meneses.carlos.sudoku.model.GameEventListener} directly,
 * so all game-reaction logic (styling, status messages, win/hint
 * feedback) is centralized in the overridden callbacks below rather
 * than duplicated inline next to the raw UI event handlers.
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 */
public class SudokuController extends GameEventAdapter {

    // ── Model ────────────────────────────────────────────────────────────────
    /**
     * Main game model that contains the Sudoku logic.
     */
    private SudokuGame game;

    // ── Visual cell grid ─────────────────────────────────────────────────────
    /**
     * Visual representation of the Sudoku board.
     * Each TextField corresponds to one cell in the model.
     */
    private final TextField[][] cells = new TextField[6][6];

    /** Currently selected row. */
    private int selectedRow = -1;

    /** Currently selected column. */
    private int selectedCol = -1;

    // ── FXML nodes ───────────────────────────────────────────────────────────
    /**
     * Main container that holds the Sudoku board.
     */
    @FXML private StackPane boardContainer;

    /**
     * Grid that displays the 6x6 Sudoku cells.
     */
    @FXML private GridPane  boardGrid;

    @FXML private Button    hintButton;
    @FXML private Button    newGameButton;
    @FXML private Button    undoButton;
    @FXML private Label     statusLabel;
    @FXML private Label     titleLabel;

    // ── Lifecycle ────────────────────────────────────────────────────────────

    /**
     * Called automatically by JavaFX after the FXML is loaded.
     * Builds the visual grid and starts the first game immediately.
     *
     * @author Carlos Meneses
     */
    @FXML
    public void initialize() {
        game = new SudokuGame();
        game.setGameEventListener(this);
        buildGrid();
        game.startNewGame();
        refreshBoard();
        statusLabel.setText("¡Nuevo juego iniciado! Completa el tablero.");
    }

    // ── Button handlers ──────────────────────────────────────────────────────

    /**
     * Handles the "Nuevo Juego" button.
     * Shows a confirmation alert before resetting the board.
     *
     * @param ignoredEvent The action event.
     * @author Carlos Meneses
     */

    @FXML
    void handleNewGame(ActionEvent ignoredEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Nuevo Juego");
        alert.setHeaderText("¿Iniciar una nueva partida?");
        alert.setContentText("Se perderá el progreso actual.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            game.startNewGame();
            refreshBoard();
            statusLabel.setText("¡Nuevo juego iniciado!");
        }
    }

    /**
     * Handles the "Ayuda" button.
     * Delegates to {@link SudokuGame#getHint()}. Hints are unlimited
     * except when only one empty cell remains, in which case the model
     * blocks the hint so the player must complete the last move manually.
     * The resulting UI feedback (cell reveal or exhaustion message) is
     * applied by the {@code onHintUsed}/{@code onHintsExhausted} callbacks.
     *
     * @param ignoredEvent The action event.
     * @author Carlos Meneses
     * @author Jorge Navia
     */
    @FXML
    void handleHint(ActionEvent ignoredEvent) {
        game.getHint();
    }

    /**
     * Handles the "Deshacer" button.
     * Reverts the last player move and refreshes only that cell.
     *
     * @param ignoredEvent The action event.
     * @author Carlos Meneses
     */
    @SuppressWarnings("unused")
    @FXML
    void handleUndo(ActionEvent ignoredEvent) {
        Move undone = game.undo();
        if (undone == null) {
            statusLabel.setText("Nada que deshacer.");
            return;
        }
        refreshCell(undone.getRow(), undone.getCol());
        statusLabel.setText("Movimiento deshecho en ("
                + undone.getRow() + ", " + undone.getCol() + ")");
    }

    // ── Grid construction ────────────────────────────────────────────────────

    /**
     * Creates all 36 TextField cells, configures their style, input validation,
     * and selection listeners, then adds them to the GridPane.
     *
     * @author Carlos Meneses
     */
    private void buildGrid() {
        boardGrid.getChildren().clear();
        boardGrid.setHgap(3);
        boardGrid.setVgap(3);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {

                TextField tf = new TextField();
                tf.getStyleClass().add("sudoku-cell");
                tf.setPrefSize(60, 60);
                tf.setMinSize(60, 60);
                tf.setMaxSize(60, 60);
                tf.setAlignment(Pos.CENTER);
                tf.setStyle(CellStyler.baseStyle(row, col));

                final int r = row;
                final int c = col;

                // ── Input validation ────────────────────────────────────────
                // Only raw-text restrictions live here (empty/clear, range
                // 1-6). Game-rule feedback (valid/invalid styling, status
                // messages, win detection) is handled exclusively by the
                // onValidMove/onInvalidMove/onGameWon callbacks below, which
                // SudokuGame#setValue triggers after updating the model.
                tf.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal.isEmpty()) {
                        game.setValue(r, c, 0);
                        tf.setStyle(CellStyler.baseStyle(r, c));
                        statusLabel.setText("Celda borrada.");
                        return;
                    }
                    if (!newVal.matches("[1-6]")) {
                        tf.setText(oldVal);
                        statusLabel.setText("Solo números del 1 al 6.");
                        return;
                    }

                    int value = Integer.parseInt(newVal);
                    game.setValue(r, c, value);
                });

                // ── Cell selection ──────────────────────────────────────────
                tf.setOnMouseClicked(e -> {
                    clearSelectionHighlight();
                    selectedRow = r;
                    selectedCol = c;
                    tf.setStyle(CellStyler.selectedStyle(r, c));
                    statusLabel.setText("Celda seleccionada: ("
                            + r + ", " + c + ")");
                });

                cells[row][col] = tf;
                boardGrid.add(tf, col, row);
            }
        }
    }

    // ── Board refresh ─────────────────────────────────────────────────────────

    /**
     * Reads every cell from the model and updates all 36 TextFields.
     * Fixed cells are shown in dark gray and made non-editable.
     *
     * @author Carlos Meneses
     */
    private void refreshBoard() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                refreshCell(row, col);
            }
        }
    }

    /**
     * Refreshes a single TextField to match the model cell's current state.
     *
     * @param row Row index (0-5).
     * @param col Column index (0-5).
     * @author Carlos Meneses
     */
    private void refreshCell(int row, int col) {
        Cell modelCell = game.getBoard().getCell(row, col);
        TextField tf = cells[row][col];

        // Temporarily remove the listener to avoid feedback loops
        tf.textProperty().unbind();

        int val = modelCell.getValue();
        tf.setText(val == 0 ? "" : String.valueOf(val));

        if (modelCell.isFixed()) {
            tf.setEditable(false);
            tf.setStyle(CellStyler.fixedStyle(row, col));
        } else {
            tf.setEditable(true);
            tf.setStyle(CellStyler.baseStyle(row, col));
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Removes the selection highlight from the previously selected cell. */
    private void clearSelectionHighlight() {
        if (selectedRow >= 0 && selectedCol >= 0) {
            refreshCell(selectedRow, selectedCol);
        }
    }

    /**
     * Called when the player has used all available hints.
     */
    @Override
    public void onHintsExhausted() {
        statusLabel.setText("¡Solo queda una celda! Debes completarla tú mismo. 🏆");
    }

    /**
     * Called when a valid move is entered.
     *
     * @param row Row index.
     * @param col Column index.
     * @param value Entered value.
     */
    @Override
    public void onValidMove(int row, int col, int value) {
        cells[row][col].setStyle(CellStyler.baseStyle(row, col));
        statusLabel.setText("Número válido.");
    }


    /**
     * Called when a move violates Sudoku rules.
     *
     * @param row Row index.
     * @param col Column index.
     * @param value Invalid value entered.
     */
    @Override
    public void onInvalidMove(int row, int col, int value) {
        cells[row][col].setStyle(CellStyler.invalidStyle(row, col));
        statusLabel.setText("Movimiento inválido");
    }

    /**
     * Called when the Sudoku puzzle has been completed successfully.
     */
    @Override
    public void onGameWon() {
        statusLabel.setText("🎉 ¡Felicidades! ¡Resolviste el Sudoku!");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Ganaste!");
        alert.setHeaderText("🎉 ¡Sudoku completado!");
        alert.setContentText("¡Excelente trabajo! ¿Quieres jugar de nuevo?");
        alert.showAndWait();
    }

    /**
     * Called after a hint is provided to the player.
     *
     * @param cell Cell that received the hint.
     * @param remainingHints Number of hints still available.
     */
    @Override
    public void onHintUsed(Cell cell, int remainingHints) {
        TextField tf = cells[cell.getRow()][cell.getCol()];
        tf.setText(String.valueOf(cell.getValue()));
        tf.setStyle(CellStyler.hintStyle());
        tf.setEditable(false);
        statusLabel.setText("Ayuda usada. Quedan: " + remainingHints);
    }

}