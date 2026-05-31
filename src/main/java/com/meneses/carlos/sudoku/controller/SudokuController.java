package com.meneses.carlos.sudoku.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class SudokuController {


    /**
     * Stores references to all visual cells displayed on the Sudoku board.
     */
    private TextField[][] cells = new TextField[6][6];


    /**
     * Represents the row index of the currently selected cell.
     */
    private int selectedRow = -1;

    /**
     * Represents the column index of the currently selected cell.
     */
    private int selectedCol = -1;

    /**
     * Container used to display and center the Sudoku board.
     */
    @FXML
    private StackPane boardContainer;

    /**
     * Grid that contains all visual cells of the Sudoku board.
     */
    @FXML
    private GridPane boardGrid;

    /**
     * Button used to request a hint during the game.
     */
    @FXML
    private Button hintButton;

    /**
     * Button used to start a new Sudoku game.
     */
    @FXML
    private Button newGameButton;

    /**
     * Label used to display status and feedback messages to the user.
     */
    @FXML
    private Label statusLabel;

    /**
     * Label that displays the title of the application.
     */
    @FXML
    private Label titleLabel;

    /**
     * Button used to undo the last move made by the player.
     */
    @FXML
    private Button undoButton;

    /**
     * Handles the hint button action.
     *
     * @param event Action event triggered by the user.
     */
    @FXML
    void handleHint(ActionEvent event) {

    }

    /**
     * Handles the new game button action.
     *
     * @param event Action event triggered by the user.
     */
    @FXML
    void handleNewGame(ActionEvent event) {

    }

    /**
     * Handles the undo button action.
     *
     * @param event Action event triggered by the user.
     */
    @FXML
    void handleUndo(ActionEvent event) {

    }

    /**
     * Initializes the Sudoku board, creates all visual cells,
     * configures event handlers, and sets up input validation.
     */
    @FXML
    public void initialize() {

        boardGrid.getChildren().clear();

        boardGrid.setHgap(2);
        boardGrid.setVgap(2);

        for (int row = 0; row < 6; row++) {

            for (int col = 0; col < 6; col++) {

                TextField cell = new TextField();

                cell.textProperty().addListener((obs, oldValue, newValue) -> {

                    if (!newValue.matches("[1-6]?")) {

                        cell.setText(oldValue);

                        statusLabel.setText(
                                "Solo se permiten numeros del 1 al 6"
                        );

                    } else {

                        statusLabel.setText(
                                "Valor ingresado correctamente"
                        );
                    }
                });

                cells[row][col] = cell;

                cell.setPrefSize(50,50);
                cell.setMinSize(50,50);
                cell.setMaxSize(50,50);

                cell.setAlignment(Pos.CENTER);

                final int currentRow = row;
                final int currentCol = col;

                cell.setOnMouseClicked(event -> {

                    for (int r = 0; r < 6; r++) {
                        for (int c = 0; c < 6; c++) {
                            cells[r][c].setStyle("");
                        }
                    }

                    selectedRow = currentRow;
                    selectedCol = currentCol;

                    statusLabel.setText(
                            "Celda seleccionada: ("
                                    + selectedRow
                                    + ", "
                                    + selectedCol
                                    + ")"
                    );

                    cell.setStyle(
                            "-fx-background-color: #ffcccc;"
                    );

                    System.out.println(
                            "Seleccionada: "
                                    + selectedRow + ", "
                                    + selectedCol
                    );
                });

                boardGrid.add(cell,col,row);
            }
        }
    }


    /**
     * Updates the visual board according to the current state
     * of the Sudoku model.
     */
    private void refreshBoard() {

        for (int row = 0; row < 6; row++) {

            for (int col = 0; col < 6; col++) {

                TextField cell = cells[row][col];

                // Model information

            }
        }
    }
}
