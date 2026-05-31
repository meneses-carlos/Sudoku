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

    private TextField[][] cells = new TextField[6][6];

    private int selectedRow = -1;
    private int selectedCol = -1;

    @FXML
    private StackPane boardContainer;

    @FXML
    private GridPane boardGrid;

    @FXML
    private Button hintButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Button undoButton;

    @FXML
    void handleHint(ActionEvent event) {

    }

    @FXML
    void handleNewGame(ActionEvent event) {

    }

    @FXML
    void handleUndo(ActionEvent event) {

    }

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


    private void refreshBoard() {

        for (int row = 0; row < 6; row++) {

            for (int col = 0; col < 6; col++) {

                TextField cell = cells[row][col];

                // Model information

            }
        }
    }
}
