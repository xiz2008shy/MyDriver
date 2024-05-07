package com.tom;

import com.tom.config.MySetting;
import com.tom.job.ScanWithRemoteJob;
import com.tom.utils.JDBCUtil;

public class JobDemo {

    public static void main(String[] args) {
        MySetting.initSetting(MySetting.mockParam());
        JDBCUtil.createStableConnection();
        ScanWithRemoteJob scanJob = new ScanWithRemoteJob();
        scanJob.scanBasePathCompareWithRemote();
    }
}
