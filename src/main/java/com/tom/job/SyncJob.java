package com.tom.job;

import com.tom.config.MySetting;
import com.tom.general.StatusBar;
import com.tom.general.TipBlock;
import com.tom.utils.JDBCUtil;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class SyncJob {

    public static final AtomicBoolean running = new AtomicBoolean(false);

    private StatusBar statusBar;

    public SyncJob(StatusBar statusBar) {
        this.statusBar = statusBar;
    }

    public static boolean isRunning(){
        return running.get();
    }

    public static void start(Stage stage, StatusBar statusBar){
        statusBar.switchSyncIcon();
        try {
            if (!running.get()) {
                SyncJob syncJob = new SyncJob(statusBar);
                Thread.startVirtualThread(syncJob::doSyncJob);
            }else {
                TipBlock.showDialog("同步任务正在执行中，请勿重复执行！","Sync Job is Running!",stage);
            }
        }catch (Exception e){
            statusBar.switchLastStatus();
            TipBlock.showDialog(e.getMessage(),"Something wrong!",stage);
        }
    }

    private void doSyncJob() {
        if (!MySetting.isInitFactory()){
            try{
                JDBCUtil.createStableConnection();
            }catch (Exception e){
                Platform.runLater(()->{
                    TipBlock.showDialog(e.getMessage(),"database connect failed!",null);
                });
                return;
            }
        }
        boolean flag = running.compareAndSet(false, true);
        if (flag){
            try {
                ScanWithLocalJob scanLocal = new ScanWithLocalJob();
                scanLocal.scanBasePathCompareWithLocal();
                ScanWithRemoteJob scanRemote = new ScanWithRemoteJob();
                scanRemote.scanBasePathCompareWithRemote();
            }catch (Exception e){
                log.error("SyncJob.doSyncJob occurred an error,cause:",e);
            }
            running.compareAndSet(true,false);
        }
        Platform.runLater(statusBar::switchLastStatus);
    }
}
