package com.tom;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sun.awt.shell.ShellFolder;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileManagementApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    private String curPath = "C:\\Users\\TOMQI\\Desktop";

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("MyDriver");

        FlowPane flowPane = new FlowPane();
        addFileNode(flowPane,curPath);
        ScrollPane scrollPane = genScrollPane(flowPane);
        BorderPane borderPane = new BorderPane();
        BorderPane.setMargin(flowPane,new Insets(0,10,0,10));
        borderPane.setCenter(scrollPane);
        AnchorPane addressPane = genAddressPane();
        borderPane.setTop(addressPane);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(700);
        stage.show();
    }


    private AnchorPane genAddressPane(){
        AnchorPane anchorPane = new AnchorPane();
        HBox hBox = new HBox();
        Text text = new Text(curPath);
        Font font = new Font(13);
        text.setFont(font);
        hBox.getChildren().add(text);
        HBox.setMargin(text,new Insets(10,0,10,15));
        hBox.setPrefHeight(25);
        hBox.setAlignment(Pos.CENTER_LEFT);

        HBox searchTab = new HBox();
        searchTab.setAlignment(Pos.CENTER);
        TextField textField = new TextField();
        File file = new File(curPath);
        textField.setPromptText("在 " +file.getName()+" 中搜索");
        textField.setFocusTraversable(false);
        textField.setPrefWidth(200);
        InputStream iconInputStream = this.getClass().getClassLoader().getResourceAsStream("searchIcon.png");
        ImageView searchIcon = new ImageView(new Image(iconInputStream));
        searchIcon.setFitWidth(20);
        searchIcon.setFitHeight(20);
        searchTab.getChildren().addAll(textField,searchIcon);
        HBox.setMargin(textField,new Insets(10,0,10,15));
        HBox.setMargin(searchIcon,new Insets(10,15,10,10));


        anchorPane.getChildren().addAll(hBox,searchTab);
        AnchorPane.setLeftAnchor(hBox,0.0);
        AnchorPane.setTopAnchor(hBox,0.0);
        AnchorPane.setBottomAnchor(hBox,0.0);

        AnchorPane.setRightAnchor(searchTab,0.0);
        AnchorPane.setTopAnchor(searchTab,0.0);
        AnchorPane.setBottomAnchor(searchTab,0.0);
        return anchorPane;
    }


    private ScrollPane genScrollPane(FlowPane flowPane) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;
    }


    private void addFileNode(FlowPane flowPane,String path) {
        ObservableList<Node> children = flowPane.getChildren();
        File baseDir = new File(path);
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File[] files = baseDir.listFiles();
        for (File file : files) {
            AnchorPane anchorPane = genFileNode(fileSystemView, file);
            children.add(anchorPane);
        }
    }


    private AnchorPane genFileNode(FileSystemView fileSystemView, File file) {
        AnchorPane anchorPane = new AnchorPane();
        FlowPane.setMargin(anchorPane,new Insets(10));
        anchorPane.setPrefWidth(90);
        anchorPane.setPrefHeight(90);
        //anchorPane.setStyle("-fx-background-color: #9b1dd3");
        VBox imageBox = new VBox();
        imageBox.setPrefHeight(60);
        imageBox.setAlignment(Pos.CENTER);
        //imageBox.setStyle("-fx-background-color: red");
        ImageView imageView;
        if (file.isDirectory()){
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("fileDir.png");
            Image image = new Image(inputStream);
            imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            imageBox.getChildren().add(imageView);
        }else {
            imageView = getBigIcon( file);
            imageBox.getChildren().add(imageView);
        }

        Label label = new Label(file.getName());
        label.setMaxWidth(90);
        label.setStyle("-fx-text-overrun: ellipsis");
        HBox hBox = new HBox(label);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefWidth(90);

        anchorPane.getChildren().addAll(imageBox,hBox);
        AnchorPane.setTopAnchor(imageBox,5.0);
        AnchorPane.setLeftAnchor(imageBox,15.0);
        AnchorPane.setRightAnchor(imageBox,15.0);
        AnchorPane.setTopAnchor(hBox,65.0);
        AnchorPane.setLeftAnchor(hBox,0.0);
        AnchorPane.setRightAnchor(hBox,0.0);
        return anchorPane;
    }


    /**
     * 获取文件大图标
     * @param file
     * @return
     */
    public ImageView getBigIcon (File file) {
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
    public ImageView getSmallIcon (FileSystemView fileSystemView,File file) {
        ImageIcon iconImage = (ImageIcon)fileSystemView.getSystemIcon(file);
        BufferedImage bufferedImage = toBufferedImage(iconImage);
        Image image = SwingFXUtils.toFXImage(bufferedImage,null);
        return new ImageView(image);
    }

    public BufferedImage toBufferedImage(ImageIcon iconImage){
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
