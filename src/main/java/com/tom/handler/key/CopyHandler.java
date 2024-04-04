package com.tom.handler.key;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.tom.component.center.MainFlowContentPart;
import javafx.event.EventHandler;
import javafx.scene.input.*;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author TOMQI
 * @Description 处理ctrl+v复制文件
 * @Date 2024/4/2 22:00
 */
public class CopyHandler implements EventHandler<KeyEvent> {
    private final KeyCodeCombination codeCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);

    private MainFlowContentPart content;

    public CopyHandler(MainFlowContentPart mainFlowContentPart) {
        this.content = mainFlowContentPart;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (codeCombination.match(keyEvent)){
            Clipboard systemClipboard = Clipboard.getSystemClipboard();
            List<File> files = systemClipboard.getFiles();
            Set<String> set = null;
            if (files != null) {
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
    }
}
