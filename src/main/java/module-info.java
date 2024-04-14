module my_driver {
    opens com.tom to java.desktop;
    requires javafx.controls;
    requires javafx.swing;
    requires java.desktop;
    requires cn.hutool.core;
    requires javafx.fxml;
    requires lombok;
    requires com.fasterxml.jackson.databind;
    exports com.tom;
    exports com.tom.component.setting;

}