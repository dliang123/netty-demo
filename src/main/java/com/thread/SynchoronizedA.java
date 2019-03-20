package com.thread;

/**
 * @Description
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 10:22 2018/8/8
 */
public class SynchoronizedA {
    private synchronized void printA(int i) {
        System.out.println(Thread.currentThread().getName() + " -> print A" + i);
        printB(i);
    }

    private synchronized void printB(int i) {
        System.out.println(Thread.currentThread().getName() + " -> print B" + i);
    }

    public static void main(String[] args) {
        SynchoronizedA synchoronizedA = new SynchoronizedA();
        for (int i = 1; i < 11; i++) {
            new Thread(() -> {

                for (int j = 1; j < 3; j++) {
                    synchoronizedA.printA(j);
//                    reentrantLockA.printB(j);
                }
            }, "Thread-" + i).start();
        }


    }
}
