package com.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description ReentrantLock锁原理
 * 重入锁：每个锁关联一个线程持有者和计数器，当计数器为0时表示该锁没有被任何线程持有，那么任何线程都可能获得该锁而调用相应的方法；
 * 当某一线程请求成功后，JVM会记下锁的持有线程，并且将计数器置为1；此时其它线程请求该锁，则必须等待；
 * 而该持有锁的线程如果再次请求这个锁，就可以再次拿到这个锁，同时计数器会递增；当线程退出同步代码块时，计数器会递减，如果计数器为0，则释放该锁
 * 重入性是在递归调用锁定方法表现最明显，循环调用时只有当前线程和锁的持有线程相同才能再次获取锁
 * <p>
 * 公平锁相对于非公平锁在于获取锁时多判断hasQueuedPredecessors()，加入了同步队列中当前节点是否有前驱节点的判断，
 * 如果该方法返回了true，则表示有线程比当前线程更早地请求获取锁
 * <p>
 * 非公平锁是默认实现：非公平性锁可能使线程“饥饿”，但是极少的线程切换，可以保证其更大的吞吐量。
 * 而公平性锁，保证了锁的获取按照FIFO原则，代价是进行大量的线程切换。
 * <p>
 * 相对于synchronized
 * 可中断响应、锁申请等待限时、公平锁。另外可以结合Condition来使用
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 11:13 2018/8/6
 */
public class ReentrantLockA {

//    private static ReentrantLock reentrantLock = new ReentrantLock(false);
    // 静态变量是类锁，非静态变量是对象类
    private  ReentrantLock reentrantLock = new ReentrantLock(true);

    public void printA(int i) {
        try {
            reentrantLock.lock();
            System.out.println(Thread.currentThread().getName() + " -> print A" + i);
            this.printB(i);
        } finally {
            reentrantLock.unlock();
        }

    }

    public void printB(int i) {
        try {
            reentrantLock.lock();
            System.out.println(Thread.currentThread().getName() + " -> print B" + i);
        } finally {
            reentrantLock.unlock();
        }

    }

    private static Integer count = 1;

//    public static void printC() {
//        try {
//            reentrantLock.lock();
//            System.out.println(Thread.currentThread().getName() + " -> print count : " + count++);
//        } finally {
//            reentrantLock.unlock();
//        }
//
//    }

    public void printD() {
        try {
            reentrantLock.lock();
            System.out.println(Thread.currentThread().getName() + " -> print count : " + count++);
        } finally {
            reentrantLock.unlock();
        }

    }

    public static void main(String[] args) {
        ReentrantLockA reentrantLockA = new ReentrantLockA();
        for (int i = 1; i < 51; i++) {
            new Thread(() -> {
                for (int j = 1; j < 10; j++) {
//                    reentrantLockA.printA(j);
//                    ReentrantLockA reentrantLockA = new ReentrantLockA();
                    reentrantLockA.printD();
                }
            } ).start();
        }

        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        for (int i = 1; i < 11; i++) {
//            new Thread(() -> {
//                for (int j = 1; j < 3; j++) {
//                    ReentrantLockA.printC();
//                }
//            }, "Thread-" + i).start();
//        }


    }
}
