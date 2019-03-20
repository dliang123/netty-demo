package com.thread;

/**
 * @Description
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 10:56 2018/8/9
 */
public class ProductQueueSync<T> {

    private final T[] items;


    //
    private int head, tail, count;

    public ProductQueueSync(int maxSize) {
        items = (T[]) new Object[maxSize];
    }

    public ProductQueueSync() {
        this(10);
    }

    public synchronized void put(T t) throws InterruptedException {
        try {
            if (count == getCapacity()) {
                System.out.println("It is full");
                wait();
            }
            items[tail] = t;
            if (++tail == getCapacity()) {
                tail = 0;
            }
            ++count;

            notifyAll();
        } finally {
        }
    }

    public synchronized T take() throws InterruptedException {
        try {
            /**
             * 因为当线程wait之后，又被唤醒的时候，是从wait后面开始执行，而不是又从头开始执行的，
             * 所以如果用if的话，被唤醒之后就不会在判断if中的条件，而是继续往下执行了，如果list只是添加了一个数据，而存在两个消费者被唤醒的话，
             * 就会出现溢出的问题了，因为不会在判断size是否==0就直接执行remove了。
             * 但是如果使用while的话，从wait下面继续执行，还会返回执行while的条件判断，size>0了才会执行remove操作，
             * 所以这个必须使用while，而不能使用if来作为判断。
             */
            if (count == 0) {
                System.out.println(Thread.currentThread().getName()+": It is empty");
                wait();
            }
            T ret = items[head];
            items[head] = null;//GC
            //
            if (++head == getCapacity()) {
                head = 0;
            }
            --count;
            notifyAll();
            return ret;
        } finally {
        }
    }

    public int getCapacity() {
        return items.length;
    }

    public static void main(String[] args) {

        ProductQueueSync<A> productQueue = new ProductQueueSync<>(1);
        new Thread(() -> {
            int num = 1;
            while (true) {
                A a = new A("A" + num);
                try {
                    productQueue.put(a);
                    System.out.println(Thread.currentThread().getName() + ": produce A" + num);
                    Thread.sleep(200);
                } catch (Exception ex) {

                }
                num++;
            }
        }).start();
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
