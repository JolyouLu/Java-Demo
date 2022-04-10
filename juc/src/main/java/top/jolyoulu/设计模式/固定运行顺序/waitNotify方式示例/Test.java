package top.jolyoulu.设计模式.固定运行顺序.waitNotify方式示例;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 13:15
 * @Version 1.0
 */
@Slf4j
public class Test {
    private static final Object lock = new Object(); //创建锁对象
    private static volatile boolean t2Runed = false; //标记t2是否已运行过

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (!t2Runed) {  //判断t2是否已经执行
                    try {
                        lock.wait(); //如果t2未执行，t1进入entrySet挂起
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.debug("1"); //等t2执行完毕后，执行t1业务代码
        }, "t1");
        Thread t2 = new Thread(() -> {
            log.debug("2"); //执行t2业务代码
            synchronized (lock) {
                t2Runed = true; //设置t2已运行
                lock.notifyAll(); //唤醒entrySet中的线程
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
