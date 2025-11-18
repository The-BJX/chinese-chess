module org.example.fxtest2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;

    opens org.example.fxtest2 to javafx.fxml;
    exports chinese_chess;
    opens chinese_chess to javafx.fxml;
}