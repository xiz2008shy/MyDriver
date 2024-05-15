package com.tom;

import com.tom.config.MySetting;
import com.tom.job.ScanWithLocalJob;
import com.tom.job.ScanWithRemoteJob;
import com.tom.utils.JDBCUtil;

public class JobDemo {

    public static void main(String[] args) throws Exception {
        MySetting.initSetting(MySetting.mockParam());
        JDBCUtil.createStableConnection();

        localSync();
        remoteSync();
    }

    private static void remoteSync() {
        ScanWithRemoteJob scanJob = new ScanWithRemoteJob();
        scanJob.scanBasePathCompareWithRemote();
    }

    private static void localSync() {
        ScanWithLocalJob scanJob = new ScanWithLocalJob();
        scanJob.scanBasePathCompareWithLocal();
    }
}
