module com.programming.advanced.wordle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
	requires javafx.graphics;

    opens com.programming.advanced.wordle to javafx.fxml;
    opens com.programming.advanced.wordle.controller to javafx.fxml;
    opens com.programming.advanced.wordle.model to javafx.base;

    exports com.programming.advanced.wordle;
    exports com.programming.advanced.wordle.model;
}
