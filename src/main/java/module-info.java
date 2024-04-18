module my_driver {
    opens com.tom to java.desktop;
    requires javafx.controls;
    requires javafx.swing;
    requires java.desktop;
    requires cn.hutool.core;
    requires javafx.fxml;
    requires javafx.graphics;
    requires lombok;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.slf4j2.impl;
    requires org.apache.logging.log4j.core;
    exports com.tom;
    exports com.tom.component.setting;
    exports com.tom.controller;
    exports com.tom.model;
    exports com.tom.general;
    opens com.tom.controller;
    opens com.tom.model;
}