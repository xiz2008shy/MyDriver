package com.tom.handler.icon;

import com.tom.component.MainFlowContentPart;
import javafx.beans.property.ObjectProperty;
import javafx.event.Event;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.Set;

/**
 * 图标事件的工厂方法
 */
public class IconHandlerFactoryBuilder<T extends Event> {

    private ObjectProperty<AnchorPane> os;

    private Set<AnchorPane> selectedSet;


    public static <T extends Event>IconHandlerFactoryBuilder<T> getInstance(ObjectProperty<AnchorPane> os, Set<AnchorPane> selectedSet){
        return new IconHandlerFactoryBuilder<>(os,selectedSet);
    }

    private IconHandlerFactoryBuilder(ObjectProperty<AnchorPane> os, Set<AnchorPane> selectedSet) {
        this.os = os;
        this.selectedSet = selectedSet;
    }


    public IconHandlerFactory<T> createFactory(File file, MainFlowContentPart mainFlowContentPart){
        IconHandlerFactory<T> factory = new IconHandlerFactory<>(os, selectedSet);
        factory.makeHandleInstance(file, mainFlowContentPart);
        return factory;
    }


}
