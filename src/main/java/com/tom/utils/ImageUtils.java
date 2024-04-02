package com.tom.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sun.awt.shell.ShellFolder;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

public class ImageUtils {

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
}
