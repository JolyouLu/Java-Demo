package top.jolyoulu.jmmcase;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/5/8 21:38
 * @Version 1.0
 */
public class Test1 {
    static int x;
    public static void main(String[] args) {
        Thread t2 = new Thread(() -> {
            while (true){
                if (Thread.currentThread().isInterrupted()){
                    //t2线程被打断后，可读取到t1线程对变量的写
                    System.out.println(x);
                    break;
                }
            }
        }, "t2");
        t2.start();

        //t1线程在1秒后，对x赋值并且打断t2线程
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            x = 10;
            t2.interrupt();
        },"t1").start();
        while (!t2.isInterrupted()){
            Thread.yield();
        }
        //其它线程得知t2被打断后，可读取到t1线程对变量的写
        System.out.println(x);
    }
}
