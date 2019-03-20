package com.thread;

/**
 * @Description
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 11:40 2018/8/3
 */
public class MonitorObject {
    private int count;

    public synchronized void setHasDataToProcess(int count) {
        this.count = count;
    }

    public synchronized int hasDataToProcess() {
        return this.count;
    }
}
