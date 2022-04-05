package top.jolyoulu.lockcase;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/5 22:02
 * @Version 1.0
 * 死锁例子
 */
@Slf4j
public class DeadLockTest {

    private static final Object A = new Object();
    private static final Object B = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (A){
                log.debug("lock A");
                synchronized (B){
                    log.debug("lock B");
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (B){
                log.debug("lock B");
                synchronized (A){
                    log.debug("lock A");
                }
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
