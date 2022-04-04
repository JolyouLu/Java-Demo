package top.jolyoulu.WaitAndNotify;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/4 12:38
 * @Version 1.0
 * 线程sleep与wait的区别
 */
@Slf4j
public class SimpTest1 {
    private final static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock) {
                log.debug("t1线程获取锁");
                try {
                    lock.wait(); //t1线程获取锁后挂起
//                    TimeUnit.DAYS.sleep(1);  //t1线程获取锁后休眠
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        TimeUnit.SECONDS.sleep(2);
        synchronized (lock) {
            log.debug("主线程获取锁");
        }
    }
}
