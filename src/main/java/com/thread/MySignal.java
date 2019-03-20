package com.thread;

/**
 * @Description
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 11:33 2018/8/3
 */
public class MySignal {
    private boolean hasDataToProcess;

    public synchronized void setHasDataToProcess(boolean hasData){
        this.hasDataToProcess=hasData;
    }
    public synchronized boolean hasDataToProcess(){
        return this.hasDataToProcess;
    }
}
