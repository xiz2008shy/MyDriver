module com.tom {
    requires javafx.controls;
    requires java.desktop;
    requires javafx.swing;
    opens com.tom to com.sun.javafx.scene.layout, sun.awt.shell,java.desktop;
    exports com.tom;
    exports com.tom.client;
}
