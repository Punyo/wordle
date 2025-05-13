module com.programming.advanced.wordle {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.programming.advanced.wordle to javafx.fxml;
    exports com.programming.advanced.wordle;
}
