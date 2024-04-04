package com.tom.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {

    private static final Map<String, SoftReference<ImageView>> imageViewCache = new HashMap<>(256);

    public static ImageView getImageViewFromResources(String path){
        Image image = getImageFromResources(path);
        return new ImageView(image);
    }

    public static Image getImageFromResources(String path){
        InputStream inputStream = ImageUtils.class.getClassLoader().getResourceAsStream(path);
        if(inputStream != null){
            return new Image(inputStream);
        }
        return null;
    }

    /**
     * 获取文件大图标
     * @param file
     * @return
     */
    public static ImageView getBigIcon (FileSystemView fileSystemView,File file) {
        String fileName = file.getName();
        SoftReference<ImageView> sr = imageViewCache.get(file.getName());
        ImageView imageView = sr != null ? sr.get() : null;
        if (imageView == null) {
            ImageIcon iconImage = (ImageIcon)fileSystemView.getSystemIcon(file, 48, 48);
            BufferedImage bufferedImage = toBufferedImage(iconImage,true);
            Image image = SwingFXUtils.toFXImage(bufferedImage,null);
            imageView = new ImageView(image);
            imageViewCache.put(file.getName(),new SoftReference<>(imageView));
        }
        return imageView;
    }

    /**
     * 获取文件更小一号的图标
     * @param fileSystemView
     * @param file
     * @return
     */
    public static ImageView getSmallIcon (FileSystemView fileSystemView, File file) {
        ImageIcon iconImage = (ImageIcon)fileSystemView.getSystemIcon(file);
        BufferedImage bufferedImage = toBufferedImage(iconImage,true);
        Image image = SwingFXUtils.toFXImage(bufferedImage,null);
        return new ImageView(image);
    }


    /**
     * 将swing的image转成图片缓冲流，用于后续SwingFXUtil转换
     * @param iconImage swing图片对象
     * @param hasAlpha 决定转化的图片是否为透明背景
     * @return
     */
    public static BufferedImage toBufferedImage(ImageIcon iconImage,boolean hasAlpha){
        java.awt.Image instance = iconImage.getImage().getScaledInstance(48, 48, java.awt.Image.SCALE_SMOOTH);
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
        }catch (HeadlessException ignored) {

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


    /**
     * 一个假装在加载svg的方法
     * svg在fx中的支持程度比较一般，下面这个方法会用固定svg代码创建图片
     * 这里的svg代码对应back.svg文件
     * @param name
     * @return
     */
    public static Region getSvgFromResources(String name){
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


    public static Region createSvg(String content,String color){
        SVGPath svg = new SVGPath();
        svg.setContent(content);
        Region region = new Region();
        region.setShape(svg);
        region.setStyle(STR."-fx-background-color: \{color}");
        return region;
    }

    public static void resize(ImageView view,double width,double height){
        view.setFitWidth(width);
        view.setFitHeight(height);
    }
}
