package com.tom.utils;

import com.tom.general.RecWindows;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DrawUtil {
    //窗体拉伸属性
    private static boolean isRight;// 是否处于右边界调整窗口状态
    private static boolean isBottomRight;// 是否处于右下角调整窗口状态
    private static boolean isBottom;// 是否处于下边界调整窗口状态
    private final static double RESIZE_WIDTH = 10;// 判定是否为调整窗口状态的范围与边界距离
    private final static double MIN_WIDTH = 10;// 窗口最小宽度
    private final static double MIN_HEIGHT = 10;// 窗口最小高度

    public static void addDrawFunc(Stage stage, RecWindows root) {

        //root.setOnMouseClicked(e -> System.out.println(STR."x-\{e.getSceneX()},y-\{e.getSceneY()},isRight-\{isRight},isBottom-\{isBottom},isBottomRight-\{isBottomRight}"));

        root.addEventHandler(MouseEvent.MOUSE_MOVED,(MouseEvent event) -> {
            //event.consume();
            double x = event.getSceneX();
            double y = event.getSceneY();
            //System.out.println(STR."x-\{x},y-\{y}");
            double width = root.getWidth();
            double height = root.getHeight();
            Cursor cursorType = Cursor.DEFAULT;// 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型
            // 先将所有调整窗口状态重置
            isRight = isBottomRight = isBottom = false;
            if (y >= height - RESIZE_WIDTH) {
                if (x <= RESIZE_WIDTH) {// 左下角调整窗口状态
                    cursorType = Cursor.W_RESIZE;
                } else if (x >= width - RESIZE_WIDTH) {// 右下角调整窗口状态
                    isBottomRight = true;
                    cursorType = Cursor.SE_RESIZE;
                } else {// 下边界调整窗口状态
                    isBottom = true;
                    cursorType = Cursor.S_RESIZE;
                }
            } else if (x >= width - RESIZE_WIDTH) {// 右边界调整窗口状态
                isRight = true;
                cursorType = Cursor.E_RESIZE;
            }
            // 最后改变鼠标光标
            root.setCursor(cursorType);
        });

        root.addEventHandler(MouseEvent.MOUSE_DRAGGED,(MouseEvent event) -> {
            double x = event.getSceneX();
            double y = event.getSceneY();
            // 保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
            double nextX = stage.getX();
            double nextY = stage.getY();
            //System.out.println(STR."x-\{x},y-\{y}nextX-\{nextX},nextY-\{nextY}");
            double nextWidth = root.getWidth();
            double nextHeight = root.getHeight();
            //System.out.println(STR."isR-\{isRight},isBottomR=\{isBottomRight}");
            if (isRight || isBottomRight) {// 所有右边调整窗口状态
                nextWidth = x;
            }
            if (isBottomRight || isBottom) {// 所有下边调整窗口状态
                nextHeight = y;
            }
            if (nextWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
                nextWidth = MIN_WIDTH;
            }
            if (nextHeight <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
                nextHeight = MIN_HEIGHT;
            }
            // 最后统一改变窗口的x、y坐标和宽度、高度，可以防止刷新频繁出现的屏闪情况
            stage.setX(nextX);
            stage.setY(nextY);
            root.myResize(nextWidth,nextHeight);
            //System.out.println(STR."nextWidth-\{nextWidth},nextHeight-\{nextHeight}");
            stage.setWidth(nextWidth);
            stage.setHeight(nextHeight);
        });
    }
}