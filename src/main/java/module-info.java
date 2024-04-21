module my_driver {
    opens com.tom;
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
    requires org.mybatis;
    requires com.zaxxer.hikari;
  /*  requires dream.orm.drive;
    requires dream.orm.system;
    requires dream.orm.jdbc;
    requires dream.orm.template;*/
    requires dream.orm.util;
    requires dream.orm.system;
    exports com.tom;
    exports com.tom.controller;
    exports com.tom.model;
    exports com.tom.general;
    opens com.tom.controller;
    opens com.tom.model;
    opens com.tom.mapper;
    exports com.tom.entity;
    opens com.tom.entity;
    exports com.tom.config;
    opens com.tom.config.vo;
}