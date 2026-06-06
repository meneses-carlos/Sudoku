package com.meneses.carlos.sudoku.controller;

import com.meneses.carlos.sudoku.model.Cell;
import com.meneses.carlos.sudoku.model.GameEventListener;
import com.meneses.carlos.sudoku.model.Move;
import com.meneses.carlos.sudoku.model.SudokuGame;
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
 * @author Jorge Navia
 * @author Carlos Meneses
 */
public class SudokuController implements GameEventListener {

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
     * Reveals the correct value in a random empty cell (max 3 times).
     *
     * @param ignoredEvent The action event.
     * @author Carlos Meneses
     */
    @SuppressWarnings("unused")
    @FXML
    void handleHint(ActionEvent ignoredEvent) {
        Cell hintCell = game.getHint();
        if (hintCell == null) {
            statusLabel.setText("No quedan ayudas disponibles.");
            return;
        }

        // Paint the hint cell in green so the player notices it
        TextField tf = cells[hintCell.getRow()][hintCell.getCol()];
        tf.setText(String.valueOf(hintCell.getValue()));
        tf.setStyle(
                "-fx-background-color: #550000; "
                        + "-fx-text-fill: #ffcccc; "
                        + "-fx-font-weight: bold;"
        );
        tf.setEditable(false);

        statusLabel.setText("Ayuda usada. Quedan: " + game.getRemainingHints());
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
                tf.setStyle(baseStyle(row, col));

                final int r = row;
                final int c = col;

                // ── Input validation ────────────────────────────────────────
                tf.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal.isEmpty()) {
                        game.setValue(r, c, 0);
                        tf.setStyle(baseStyle(r, c));
                        statusLabel.setText("Celda borrada.");
                        checkWin();
                        return;
                    }
                    if (!newVal.matches("[1-6]")) {
                        tf.setText(oldVal);
                        statusLabel.setText("Solo números del 1 al 6.");
                        return;
                    }

                    int value = Integer.parseInt(newVal);

                    if (game.isValidMove(r, c, value)) {
                        game.setValue(r, c, value);
                        tf.setStyle(baseStyle(r, c));   // valid - normal color
                        statusLabel.setText("Número válido.");
                        checkWin();
                    } else {
                        game.setValue(r, c, value);

                        tf.setStyle(
                                baseStyle(r, c)
                                        + "-fx-background-color: #140000; "
                                        + "-fx-text-fill: white; "
                                        + "-fx-border-color: #ffd700; "
                                        + "-fx-border-width: 3;"
                        );

                        statusLabel.setText("Movimiento inválido");
                    }
                });

                // ── Cell selection ──────────────────────────────────────────
                tf.setOnMouseClicked(e -> {
                    clearSelectionHighlight();
                    selectedRow = r;
                    selectedCol = c;
                    tf.setStyle(
                            baseStyle(r, c)
                                    + "-fx-background-color: #330000; "
                                    + "-fx-border-color: #ff5555; "
                                    + "-fx-border-width: 2;"
                    );
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
            tf.setStyle("-fx-background-color: #2a0000; "
                    + "-fx-font-weight: bold; "
                    + "-fx-text-fill: white; "
                    + baseStyle(row, col));
        } else {
            tf.setEditable(true);
            tf.setStyle(baseStyle(row, col));
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Returns the base CSS style for a cell, adding a thicker border
     * on block boundaries to visually separate the 2x3 blocks.
     *
     * @param row Row index.
     * @param col Column index.
     * @return A CSS style string.
     * @author Carlos Meneses
     */
    private String baseStyle(int row, int col) {

        String top = "1";
        String left = "1";
        String bottom = "1";
        String right = "1";

        if (row == 0 || row == 2 || row == 4)
            top = "4";

        if (col == 0 || col == 3)
            left = "4";

        if (row == 5)
            bottom = "4";

        if (col == 5)
            right = "4";

        return "-fx-border-color: #ff6666; "
                + "-fx-border-width: "
                + top + " "
                + right + " "
                + bottom + " "
                + left + "; "
                + "-fx-font-size: 18px; "
                + "-fx-alignment: center;";
    }

    /** Removes the blue selection highlight from all cells. */
    private void clearSelectionHighlight() {
        if (selectedRow >= 0 && selectedCol >= 0) {
            refreshCell(selectedRow, selectedCol);
        }
    }

    /**
     * Checks if the game has been won and shows a congratulation alert.
     *
     * @author Carlos Meneses
     */
    private void checkWin() {
        if (game.isGameWon()) {
            statusLabel.setText("🎉 ¡Felicidades! ¡Resolviste el Sudoku!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Ganaste!");
            alert.setHeaderText("🎉 ¡Sudoku completado!");
            alert.setContentText("¡Excelente trabajo! ¿Quieres jugar de nuevo?");
            alert.showAndWait();
        }

    }


    /**
     * Called when the player has used all available hints.
     */
    @Override
    public void onHintsExhausted() {
        statusLabel.setText("No quedan ayudas disponibles.");
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
        cells[row][col].setStyle(baseStyle(row, col));
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
        cells[row][col].setStyle(
                baseStyle(row, col)
                        + "-fx-background-color: #140000; "
                        + "-fx-text-fill: white; "
                        + "-fx-border-color: #ffd700; "
                        + "-fx-border-width: 3;"
        );

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
        tf.setStyle(
                "-fx-background-color: #550000; "
                        + "-fx-text-fill: #ffcccc; "
                        + "-fx-font-weight: bold;"
        );
        tf.setEditable(false);
        statusLabel.setText("Ayuda usada. Quedan: " + remainingHints);
    }

}