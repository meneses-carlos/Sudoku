module com.meneses.carlos.sudoku {

    requires javafx.controls;
    requires javafx.fxml;

    opens com.meneses.carlos.sudoku to javafx.fxml;
    opens com.meneses.carlos.sudoku.controller to javafx.fxml;

    exports com.meneses.carlos.sudoku;
}