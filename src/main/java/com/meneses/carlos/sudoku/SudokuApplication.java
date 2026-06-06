package com.meneses.carlos.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main JavaFX application class.
 * Responsible for loading the main view,
 * applying the stylesheet and launching
 * the Sudoku user interface.
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 */

public class SudokuApplication extends Application {

    /**
     * Starts the JavaFX application and loads
     * the main Sudoku window.
     *
     * @param stage Primary application stage.
     * @throws IOException If the FXML file cannot be loaded.
     *
     * @author Jorge Navia
     * @author Carlos Meneses
     */

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
