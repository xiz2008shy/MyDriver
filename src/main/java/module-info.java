module MyDriver {
    opens com.tom to java.desktop,sun.awt.shell;
    requires javafx.controls;
    requires javafx.swing;
    requires java.desktop;
    requires cn.hutool.core;
    exports com.tom;

}