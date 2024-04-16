package com.tom.handler.key;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * @author TOMQI
 * @Description 处理ctrl+v复制文件
 * @Date 2024/4/2 22:00
 */
public class CopyHandler implements EventHandler<KeyEvent> {
    @Override
    public void handle(KeyEvent event) {

    }
    /*private final KeyCodeCombination codeCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_ANY);

    private MainFlowContentPart content;

    public CopyHandler(MainFlowContentPart mainFlowContentPart) {
        this.content = mainFlowContentPart;
    }


    @Override
    public void handle(KeyEvent keyEvent) {
        System.out.println("copy handler");
        if (codeCombination.match(keyEvent)){
            Clipboard systemClipboard = Clipboard.getSystemClipboard();
            List<File> files = systemClipboard.getFiles();
            Set<String> set = null;
            if (files != null) {
                //TODO 待优化，发现存在同名文件的场合需要在复制文件后添加副本名称
                for (File file : files) {
                    copyFile(file,content.getFile().getAbsolutePath());
                }
                set = files.stream().map(File::getName).collect(Collectors.toSet());
            }
            content.refreshFileNode(set);
        }
    }

    private void copyFile(File file,String targetPath) {
        String targetFullPath = targetPath + File.separator + file.getName();
        File targetFile = new File(targetFullPath);
        if (file.isDirectory()){
            FileUtil.mkdir(targetFile);
            File[] files = file.listFiles();
            if (files != null){
                for (File subFile : files) {
                    copyFile(subFile,targetFullPath);
                }
            }
        }else {
            try (FileInputStream inputStream = new FileInputStream(file);
                 FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                IoUtil.copy(inputStream,outputStream);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }*/
}
