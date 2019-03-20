package com.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description Condition条件变量用法
 * 条件变量很大一个程度上是为了解决Object.wait/notify/notifyAll难以使用的问题（只能和Synchoronized组合使用，见ThreadD类）。
 * Condition接口定义的方法，await*对应于Object.wait，signal对应于Object.notify，signalAll对应于Object.notifyAll
 * Lock.newCondition()，与Lock组合使用
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 15:12 2018/8/8
 */
public class ProductQueue<T> {

    private final T[] items;

    private final Lock lock = new ReentrantLock();

    private Condition notFull = lock.newCondition();

    private Condition notEmpty = lock.newCondition();

    //
    private int head, tail, count;

    public ProductQueue(int maxSize) {
        items = (T[]) new Object[maxSize];
    }

    public ProductQueue() {
        this(10);
    }

    public void put(T t) throws InterruptedException {
        lock.lock();
        try {
            if (count == getCapacity()) {
                System.out.println("It is full");
                notFull.await();
            }
            items[tail] = t;
            if (++tail == getCapacity()) {
                tail = 0;
            }
            ++count;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            /**
             * 因为当线程wait之后，又被唤醒的时候，是从wait后面开始执行，而不是又从头开始执行的，
             * 所以如果用if的话，被唤醒之后就不会在判断if中的条件，而是继续往下执行了，如果list只是添加了一个数据，而存在两个消费者被唤醒的话，
             * 就会出现溢出的问题了，因为不会在判断size是否==0就直接执行remove了。
             * 但是如果使用while的话，从wait下面继续执行，还会返回执行while的条件判断，size>0了才会执行remove操作，
             * 所以这个必须使用while，而不能使用if来作为判断。
             */
            if (count == 0) {
                System.out.println("It is empty");
                notEmpty.await();
            }
            T ret = items[head];
            items[head] = null;//GC
            //
            if (++head == getCapacity()) {
                head = 0;
            }
            --count;
            notFull.signalAll();
            return ret;
        } finally {
            lock.unlock();
        }
    }

    public int getCapacity() {
        return items.length;
    }

    public int size() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }

    }


    public static void main(String[] args) {

        ProductQueue<A> productQueue = new ProductQueue<>(1);
        Thread thread1 = new Thread(() -> {
            int num = 1;
            while (true) {
                A a = new A("A" + num);
                try {
                    productQueue.put(a);
                    Thread.sleep(200);
//                    System.out.println(Thread.currentThread().getName() + ": produce A" + num);
                } catch (Exception ex) {

                }
                num++;
            }
        });
        thread1.start();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        A a = productQueue.take();
                        System.out.println(Thread.currentThread().getName() + ": consume " + a.getName());
                    } catch (Exception ex) {

                    }
                }
            }).start();
        }


    }

}
