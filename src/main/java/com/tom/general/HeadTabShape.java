package com.tom.general;

import javafx.scene.shape.*;

public class HeadTabShape {

    private double width;

    private double height;

    public static Shape headTabShape(double width,double height,double radius){
        double dr = 2 * radius;
        // 右下角的x坐标
        double w2 = width + radius;
        // 右上角的x点坐标
        double w3 = width - radius;
        // 左下角 圆弧终点的 y坐标
        double h2 = height - radius;
        Path path = new Path();
        //Moving to the starting point
        MoveTo moveTo = new MoveTo(radius * 2, 0);

        LineTo line1 = new LineTo(w3, 0);
        ArcTo arc1 = new ArcTo();
        arc1.setRadiusX(radius);
        arc1.setRadiusY(radius);
        arc1.setX(width);
        arc1.setY(radius);
        arc1.setSweepFlag(true);

        LineTo line2 = new LineTo(width,h2);
        ArcTo arc2 = new ArcTo();
        arc2.setRadiusX(radius);
        arc2.setRadiusY(radius);
        arc2.setX(w2);
        arc2.setY(height);

        LineTo line3 = new LineTo(0,height);
        ArcTo arc3 = new ArcTo();
        arc3.setRadiusX(radius);
        arc3.setRadiusY(radius);
        arc3.setX(radius);
        arc3.setY(h2);

        LineTo line4 = new LineTo(radius,radius);
        ArcTo arc4 = new ArcTo();
        arc4.setRadiusX(radius);
        arc4.setRadiusY(radius);
        arc4.setX(dr);
        arc4.setY(0);
        arc4.setSweepFlag(true);

        path.getElements().add(moveTo);
        path.getElements().addAll(line1, arc1,line2,arc2 ,line3 ,arc3,line4,arc4);
        return path;
    }

    public static Shape headTabSecShape(double width,double height,double radius){
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcWidth(radius);
        rectangle.setArcHeight(radius);
        return rectangle;
    }

}
