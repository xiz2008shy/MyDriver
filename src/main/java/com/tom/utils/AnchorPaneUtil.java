package com.tom.utils;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class AnchorPaneUtil {


    public static void setNode(Node child, Double top,Double right,Double bottom,Double left){
        AnchorPane.setTopAnchor(child,top);
        AnchorPane.setRightAnchor(child,right);
        AnchorPane.setBottomAnchor(child,bottom);
        AnchorPane.setLeftAnchor(child,left);
    }
}
