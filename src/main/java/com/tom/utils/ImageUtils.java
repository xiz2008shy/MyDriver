package com.tom.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import sun.awt.shell.ShellFolder;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static ImageView getImageFromResources(String path){
        InputStream inputStream = ImageUtils.class.getClassLoader().getResourceAsStream(path);
        Image image = new Image(inputStream);
        return new ImageView(image);
    }

    /**
     * 获取文件大图标
     * @param file
     * @return
     */
    public static ImageView getBigIcon (File file) {
        BufferedImage bi = null;
        try {
            ShellFolder shellFolder = ShellFolder.getShellFolder(file);
            ImageIcon imageIcon = new ImageIcon(shellFolder.getIcon(true));
            bi = toBufferedImage(imageIcon);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = SwingFXUtils.toFXImage(bi,null);
        return new ImageView(image);
    }

    /**
     * 获取文件更小一号的图标
     * @param fileSystemView
     * @param file
     * @return
     */
    public static ImageView getSmallIcon (FileSystemView fileSystemView, File file) {
        ImageIcon iconImage = (ImageIcon)fileSystemView.getSystemIcon(file);
        BufferedImage bufferedImage = toBufferedImage(iconImage);
        Image image = SwingFXUtils.toFXImage(bufferedImage,null);
        return new ImageView(image);
    }

    public static BufferedImage toBufferedImage(ImageIcon iconImage){
        boolean hasAlpha = true;
        //java.awt.Image instance = iconImage.getImage();
        java.awt.Image instance = iconImage.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        BufferedImage bi = null;
        try {
            int transparency = Transparency.OPAQUE;
            if(hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            bi = gc.createCompatibleImage(instance.getWidth(null), instance.getHeight(null), transparency);
        }catch (HeadlessException e) {

        }
        if (bi == null) {
            int type =BufferedImage.TYPE_INT_RGB;
            if(hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bi = new BufferedImage(instance.getWidth(null), instance.getHeight(null),type);
        }
        Graphics graphics = bi.createGraphics();
        graphics.drawImage(instance,0,0,null);
        graphics.dispose();
        return bi;
    }


    public static Region getSvgFromResources(ClassLoader classLoader, String name){
        try {
            SVGPath svg1 = new SVGPath();
            svg1.setContent("M512 0c281.6 0 512 230.4 512 512s-230.4 512-512 512-512-230.4-512-512 230.4-512 512-512z m0 960c249.6 0 448-198.4 448-448s-198.4-448-448-448-448 198.4-448 448 198.4 448 448 448z " +
                    "M588.8 262.4c6.4-6.4 32-6.4 44.8 0 6.4 12.8 6.4 38.4 0 44.8L428.8 512l204.8 204.8c12.8 12.8 12.8 32 0 44.8-12.8 12.8-32 12.8-44.8 0L364.8 537.6c-12.8-12.8-12.8-32 0-44.8l224-230.4z");

            Region region = new Region();
            region.setShape(svg1);
            return region;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
