package com.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Description 使用wait/notify实现线程间通信，需要设置共享内存变量，通过对象锁保证线程安全
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 11:41 2018/8/3
 */
public class ThreadMonitor extends Thread {
    MonitorObject mySignal;
    ThreadD threadD;

    public ThreadMonitor(MonitorObject mySignal, ThreadD threadD) {
        this.mySignal = mySignal;
        this.threadD = threadD;
    }

    @Override
    public void run() {
        synchronized (mySignal) {
            try {
                mySignal.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程B计算结果为:" + threadD.count);
        }
    }

    public static void main(String[] args) {
        MonitorObject mySignal = new MonitorObject();
        ThreadD threadD = new ThreadD(mySignal);
        ThreadMonitor threadMonitor = new ThreadMonitor(mySignal, threadD);
        threadMonitor.start();
        threadD.start();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Map<String, String> data = new HashMap<>();
        Future<Map<String, String>> future = executorService.submit(new Task(data), data);
        try {
            Map<String, String> map = future.get();
            System.out.println(map);
        } catch (Exception ex) {

        }

        System.out.println(future);
    }

    private static class Task implements Runnable {

        private Map<String, String> data;

        public Task(Map<String, String> data) {
            this.data = data;
        }

        @Override
        public void run() {
            data.put("ss", "dd");
        }
    }

}
