# Sudoku 6x6

A 6x6 Sudoku game built with Java and JavaFX (FXML/Scene Builder) for the course
*750014C Fundamentos de Programación Orientada a Eventos* (Universidad del Valle).

## Features

- 6x6 board divided into six 2x3 blocks.
- New game with confirmation dialog before resetting progress.
- Real-time validation of row, column, and 2x3 block rules, with visual feedback.
- Undo the last move.
- Hint button that reveals a valid value for a random empty cell, blocked once
  only the last empty cell remains so the player must finish the board manually.

## Architecture

- `model` — `Cell`, `Move`, `SudokuBoard`, `SudokuGame`, `GameEventListener`,
  `GameEventAdapter`: game state, rules, and the observer contract used to
  notify the controller of game events without coupling the model to JavaFX.
- `controller` — `SudokuController`: wires the FXML view to the model and
  reacts to game events.
- `view` — `CellStyler`: builds the CSS style strings used to render cell state.
- FXML/CSS resources under `src/main/resources`.

## Running the project

```bash
mvn clean javafx:run
```

Requires JDK 17+ and Maven (or the bundled `mvnw`/`mvnw.cmd` wrapper).
