package com.meneses.carlos.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SudokuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SudokuApplication.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 700);

        scene.getStylesheets().add(
                getClass().getResource(
                        "/com/meneses/carlos/sudoku/style.css"
                ).toExternalForm()
        );

        stage.setTitle("SUDOKUU!");
        stage.setScene(scene);
        stage.show();
    }
}
