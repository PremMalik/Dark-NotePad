module org.darknotepad.darknotepad {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;

    opens org.darknotepad.darknotepad to javafx.fxml;
    exports org.darknotepad.darknotepad;
}