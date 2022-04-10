package top.jolyoulu.设计模式.交替运行.reentrantlock;

import ch.qos.logback.core.util.TimeUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 18:37
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();
        new Thread(() -> {
            awaitSignal.print("a", a, b);
        }, "t1").start();
        new Thread(() -> {
            awaitSignal.print("b", b, c);
        }, "t2").start();
        new Thread(() -> {
            awaitSignal.print("c", c, a);
        }, "t3").start();

        //休息1秒等待对象准备完毕，主线程发起开始命令
        TimeUnit.SECONDS.sleep(1);
        awaitSignal.lock();
        try {
            System.out.println("开始....");
            a.signal();
        }finally {
            awaitSignal.unlock();
        }
    }
}
