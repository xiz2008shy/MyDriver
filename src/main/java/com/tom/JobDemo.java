package com.tom;

import com.tom.config.MySetting;
import com.tom.job.ScanJob;
import com.tom.utils.JDBCUtil;

public class JobDemo {

    public static void main(String[] args) {
        MySetting.initSetting(MySetting.mockParam());
        JDBCUtil.createStableConnection();
        ScanJob scanJob = new ScanJob();
        scanJob.scanBasePath();
    }
}
