package com.tom.utils;

import com.tom.component.center.MainFlowContentPart;
import com.tom.general.menu.BaseMenu;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeliverUtils {

    private final static List<Map<String,File>> pathIndexP = new ArrayList<>();
    private final static ObjectProperty<File> curFile =new SimpleObjectProperty<>();
    private final static ObjectProperty<AnchorPane> lastSelectFileP =new SimpleObjectProperty<>();

    private final static ObjectProperty<BaseMenu> baseMenuP = new SimpleObjectProperty<>();

    /*private final static ObjectProperty<MainFlowContentPart> mainFlowPart = new SimpleObjectProperty<>();*/

    public static void setPathIndex(Map<String, File> pathIndex){
        pathIndexP.add(pathIndex);
    }

    public static Map<String, File> getPathIndex(int index){
        return pathIndexP.get(index);
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

    public static void setBaseMenu(BaseMenu baseMenu){
        baseMenuP.set(baseMenu);
    }

   /* public static void setMainFlowPart(MainFlowContentPart flowPart){
        mainFlowPart.set(flowPart);
    }

    public static MainFlowContentPart getMainFlowPart(){
        return mainFlowPart.get();
    }*/
}
