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

                cell.setPrefSize(50,50);
                cell.setMinSize(50,50);
                cell.setMaxSize(50,50);


                cell.setAlignment(Pos.CENTER);
                cell.setFont(Font.font(18));
                boardGrid.setAlignment(Pos.CENTER);
                boardGrid.add(cell, col, row);

            }
        }
    }
}
