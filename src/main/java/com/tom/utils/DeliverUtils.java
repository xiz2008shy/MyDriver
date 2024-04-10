package com.tom.utils;

import com.tom.menu.BaseMenu;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.Map;

public class DeliverUtils {

    private final static ObjectProperty<Map<String,File>> pathIndexP = new SimpleObjectProperty<>();
    private final static ObjectProperty<File> curFile =new SimpleObjectProperty<>();
    private final static ObjectProperty<AnchorPane> lastSelectFileP =new SimpleObjectProperty<>();

    private final static ObjectProperty<BaseMenu> baseMenuP = new SimpleObjectProperty<>();

    public static void setPathIndex(Map<String, File> pathIndex){
        pathIndexP.set(pathIndex);
    }

    public static Map<String, File> getPathIndex(){
        return pathIndexP.get();
    }


    public static void setCurPath(File file){
        curFile.set(file);
    }

    public static void clearCurPath(){
        curFile.set(null);
    }

    public static File getCurPath(){
        return curFile.get();
    }

    public static void setLastSelectFile(AnchorPane lastSelectFile) {
        lastSelectFileP.set(lastSelectFile);
    }

    public static AnchorPane getLastSelectedFileNode(){
        return lastSelectFileP.get();
    }

    public static void clearLastSelectFile() {
        lastSelectFileP.set(null);
    }

    public static ObjectProperty<AnchorPane> getLastSelectFileP() {
        return lastSelectFileP;
    }

    public static BaseMenu getBaseMenu(){
        return baseMenuP.get();
    }

    public void setBaseMenu(BaseMenu baseMenu){
        baseMenuP.set(baseMenu);
    }
}
