module MyDriver {
    opens com.tom to java.desktop;
    requires javafx.controls;
    requires javafx.swing;
    requires java.desktop;
    requires cn.hutool.core;
    requires javafx.fxml;
    exports com.tom;

}