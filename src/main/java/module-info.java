module com.programming.advanced.wordle {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.ibm.icu;

    opens com.programming.advanced.wordle to javafx.fxml;
    opens com.programming.advanced.wordle.controller to javafx.fxml;

    exports com.programming.advanced.wordle;
}
