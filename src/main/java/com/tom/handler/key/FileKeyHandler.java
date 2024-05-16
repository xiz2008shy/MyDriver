package com.tom.handler.key;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import com.tom.general.RecWindows;
import com.tom.model.ModelData;
import com.tom.utils.FileNameUtil;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * @author TOMQI
 * @Description 处理ctrl+v复制文件
 * @Date 2024/4/2 22:00
 */
@Slf4j
public class FileKeyHandler implements EventHandler<KeyEvent> {

    private final KeyCodeCombination PASTE = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_ANY);
    private final KeyCodeCombination COPY = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

    private RecWindows mainWindows;

    public FileKeyHandler(RecWindows mainWindows) {
        this.mainWindows = mainWindows;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        System.out.println("copy handler");
        if (PASTE.match(keyEvent)){
            copyFromSystemClipboard(mainWindows);
        }else if (keyEvent.getCode() == KeyCode.DELETE){
            ModelData activeModelData = mainWindows.getActiveModelData();
            File realSelectedFile = activeModelData.getRealSelectedFile();

            if (realSelectedFile != null){
                if (realSelectedFile.exists()) {
                    FileUtil.del(realSelectedFile);
                }
            }
        }else if (COPY.match(keyEvent)){
            ModelData activeModelData = mainWindows.getActiveModelData();
            File realSelectedFile = activeModelData.getRealSelectedFile();
            if (realSelectedFile != null){
                setToSystemClipboard(realSelectedFile);
            }
        }

        mainWindows.freshPage();
    }

    public static void copyFromSystemClipboard(RecWindows windows) {
        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        List<File> files = systemClipboard.getFiles();
        if (files != null) {
            for (File file : files) {
                ModelData activeModelData = windows.getActiveModelData();
                copyFile(file, activeModelData.getCurDir().getAbsolutePath());
            }
            systemClipboard.clear();
        }
    }

    public static void setToSystemClipboard(File realSelectedFile) {
        if (realSelectedFile.exists()) {
            Clipboard systemClipboard = Clipboard.getSystemClipboard();
            Map<DataFormat, Object> map = Collections.singletonMap(DataFormat.FILES, List.of(realSelectedFile));
            systemClipboard.setContent(map);
        }
    }

    /**
     * 复制文件或者目录，同名的将以副本形式被复制
     * @param file
     * @param targetPathStr
     */
    public static void copyFile(File file, String targetPathStr) {
        String targetFullPath = targetPathStr + File.separator + file.getName();
        File targetFile = new File(targetFullPath);
        while (file.equals(targetFile) || targetFile.exists()){
            Pair<String, String> filename = FileNameUtil.parseFilename(targetFullPath);
            targetFullPath = filename.getKey() + "_副本" + filename.getValue();
            targetFile = new File(targetFullPath);
        }
        if (file.isDirectory()){
            FileUtil.mkdir(targetFile);
            File[] files = file.listFiles();
            if (files != null){
                for (File subFile : files) {
                    copyFile(subFile,targetFullPath);
                }
            }
        }else {
            Path sourcePath = file.toPath();
            Path targetPath = targetFile.toPath();
            try (FileChannel source = FileChannel.open(sourcePath, StandardOpenOption.READ);
                 FileChannel target = FileChannel.open(targetPath,CREATE,WRITE)) {
                target.transferFrom(source, 0, source.size());
            } catch (Exception e) {
                log.error("CopyHandler.copyFile occurred an error,cause:", e);
            }
        }
    }
}
