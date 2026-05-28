module com.meneses.carlos.sudoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.meneses.carlos.sudoku to javafx.fxml;
    exports com.meneses.carlos.sudoku;
}