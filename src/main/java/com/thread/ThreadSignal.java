package com.thread;

/**
 * @Description
 * 实现线程之间通信
 * 共享对象的变量中设置信号值。线程B在一个同步块中设置一个成员变量hasDataToProcess值为true，而线程A同样在一个同步块中读取这个成员变量
 * 线程A可以等待线程B的信号，这个信号可以是线程B已经处理完成的信号。
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 11:34 2018/8/3
 */
public class ThreadSignal extends Thread{
    MySignal mySignal;
    ThreadB threadB;
    public ThreadSignal(MySignal mySignal, ThreadB threadB){
        this.mySignal=mySignal;
        this.threadB=threadB;
    }
    @Override
    public void run(){
        /**
         * 线程A一直在等待数据就绪，或者说线程A一直在等待线程B设置hasDataToProcess的信号值为true
         * 忙等意味着线程还处于运行状态，一直在消耗CPU资源
         * 使用wait/notify方案解决忙等问题
         */
        while (true){
            if(mySignal.hasDataToProcess()){
                System.out.println("线程B计算结果为:"+threadB.count);
                break;
            }
        }
    }
    public static void main(String[] args) {
        MySignal mySignal=new MySignal();
        ThreadB threadB=new ThreadB(mySignal);
        ThreadSignal threadSignal =new ThreadSignal(mySignal,threadB);
        threadB.start();
        threadSignal.start();
    }
}
