package com.gibbon.etc;

import java.util.HashMap;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-09
 */
public class UtMonitor {
    private HashMap<String, Long> pageTimeMonitorMap;

    public static UtMonitor getInstance() {
        return Inner.sInstance;
    }

    private UtMonitor() {
        pageTimeMonitorMap = new HashMap<>();
    }

    private static class Inner {
        private static UtMonitor sInstance = new UtMonitor();
    }

    public HashMap<String, Long> getPageTimeMonitorMap() {
        return this.pageTimeMonitorMap;
    }
}
