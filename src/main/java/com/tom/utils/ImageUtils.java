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

    private static final Map<String, SoftReference<Image>> imageViewCache = new HashMap<>(256);

    public static ImageView getImageViewFromResources(String path,double width,double height){
        Image image = getImageFromResources(path, width, height);
        return new ImageView(image);
    }

    public static Image getImageFromResources(String path,double width,double height){
        SoftReference<Image> imageSoftReference = imageViewCache.get(STR."\{path}-\{width}-\{height}");
        Image image = null;
        if (imageSoftReference == null) {
            InputStream inputStream = ImageUtils.class.getResourceAsStream(path);
            if(inputStream != null){
                image = new Image(inputStream, width, height, true, true);
                imageViewCache.put(path,new SoftReference<>(image));
            }
        }else {
            image = imageSoftReference.get();
        }
        return image;
    }

    public static ImageView getImageView(String image, int size, int resize) {
        ImageView imageView = getImageViewFromResources(image, size, size);
        ImageUtils.resize(imageView, resize, resize);
        return imageView;
    }

    /**
     * 获取文件大图标
     * @param file 文件对象
     * @return 返回文件icon的图片视图
     */
    public static ImageView getBigIcon (FileSystemView fileSystemView,File file) {
        SoftReference<Image> sr = imageViewCache.get(file.getName());
        Image image = sr != null ? sr.get() : null;
        ImageView imageView;
        if (image == null) {
            ImageIcon iconImage = (ImageIcon)fileSystemView.getSystemIcon(file, 48, 48);
            BufferedImage bufferedImage = toBufferedImage(iconImage,true);
            image = SwingFXUtils.toFXImage(bufferedImage,null);
            imageView = new ImageView(image);
            imageViewCache.put(file.getName(),new SoftReference<>(image));
        }else {
            imageView = new ImageView(image);
        }
        return imageView;
    }

    public static Image getBigIconImage (FileSystemView fileSystemView,File file) {
        SoftReference<Image> sr = imageViewCache.get(file.getName());
        Image image = sr != null ? sr.get() : null;
        if (image == null) {
            ImageIcon iconImage = (ImageIcon)fileSystemView.getSystemIcon(file, 48, 48);
            BufferedImage bufferedImage = toBufferedImage(iconImage,true);
            image = SwingFXUtils.toFXImage(bufferedImage,null);
            imageViewCache.put(file.getName(),new SoftReference<>(image));
        }
        return image;
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
     * 创建一个svgPath构建的region
     * @param content
     * @param color
     * @return
     */
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
