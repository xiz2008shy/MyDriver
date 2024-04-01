module com.tom {
    opens com.tom to java.desktop,sun.awt.shell;
    requires javafx.controls;
    requires javafx.swing;
    requires java.desktop;
    exports com.tom;

}