package com.thread;

/**
 * @Description 线程本地变量
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 9:31 2018/8/7
 */
public class ThreadLocalA {

//    private static final A a = new A();
    private static final ThreadLocal<A> threadLocal = new ThreadLocal<A>() {
        @Override
        protected A initialValue() {
            /**
             * 此处必须创建一个新对象，否则无法为每个新线程分配一个独立的局部变量
             */
            return new A();
        }
    };

    public static void main(String[] args) {
        for (int i = 1; i < 51; i++) {
            new Thread(() -> {
                A a = threadLocal.get();
                a.setNum(a.getNum() + 5);
                System.out.println(Thread.currentThread().getName() + " -> " + a.getNum());
            }, "Thread-" + i).start();
        }


    }
}
