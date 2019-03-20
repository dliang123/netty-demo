package com.thread;

import java.util.concurrent.Semaphore;

/**
 * @Description
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 10:11 2018/8/6
 */
public class SemaphoreA {
    //信号量必须放方法外
    Semaphore semaphore = new Semaphore(1);

    public void print() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + "  print A");
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + "  print wake up");
        } catch (InterruptedException ex) {

        } finally {
            semaphore.release();
        }

    }

    public static void main(String[] args) {
        SemaphoreA semaphoreA = new SemaphoreA();
        for (int i = 1; i < 51; i++) {
            new Thread(() -> {
                semaphoreA.print();
            }, "Thread-" + i).start();
        }
    }
}
