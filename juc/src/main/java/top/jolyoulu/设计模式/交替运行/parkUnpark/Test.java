package top.jolyoulu.设计模式.交替运行.parkUnpark;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 18:53
 * @Version 1.0
 */
public class Test {
    private static Thread t1;
    private static Thread t2;
    private static Thread t3;

    public static void main(String[] args) {
        ParkUnpark parkUnpark = new ParkUnpark(5);
        t1 = new Thread(() -> {
            parkUnpark.print("a",t2);
        }, "t1");
        t2 = new Thread(() -> {
            parkUnpark.print("b",t3);
        }, "t2");
        t3 = new Thread(() -> {
            parkUnpark.print("c",t1);
        }, "t3");
        t1.start();
        t2.start();
        t3.start();
        System.out.println("开始");
        LockSupport.unpark(t1); //主线程唤醒t1开始执行
    }
}
