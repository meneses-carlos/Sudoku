package com.meneses.carlos.sudoku;

import javafx.application.Application;

/**
 * Plain entry point used to launch the JavaFX application.
 *
 * <p>Kept separate from {@link SudokuApplication} so the executable
 * jar has a regular {@code main} method that does not itself extend
 * {@link Application}, avoiding classpath issues some JavaFX launchers
 * have with a module-less main class.
 *
 * @author Jorge Navia
 * @author Carlos Meneses
 * @version 1.1
 */
public class Launcher {

    /**
     * Launches the Sudoku JavaFX application.
     *
     * @param args Command-line arguments passed to the JavaFX runtime.
     */
    public static void main(String[] args) {
        Application.launch(SudokuApplication.class, args);
    }
}
