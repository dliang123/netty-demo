package com.thread;

/**
 * @Description
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 11:41 2018/8/3
 */
public class ThreadD extends Thread {
    int count;
    MonitorObject mySignal;

    public ThreadD(MonitorObject mySignal) {
        this.mySignal = mySignal;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            count = count + 1;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (mySignal) {//必须获得对象锁后才能唤醒线程
            mySignal.notify();//计算完成调用对象的notify()方法，唤醒因调用这个对象wait()方法而挂起的线程
        }

    }
}
