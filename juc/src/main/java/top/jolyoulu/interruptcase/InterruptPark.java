package top.jolyoulu.interruptcase;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: JolyouLu
 * @Date: 2022/3/6 0:48
 * @Version 1.0
 */
@Slf4j
public class InterruptPark {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("t1线程 park");
            LockSupport.park(); //调用park后线程会wait
            log.debug("t1线程 unpark");
            log.debug("t1线程打断标记={}",Thread.currentThread().isInterrupted());
        },"t1");
        t1.start();

        TimeUnit.SECONDS.sleep(3);
        log.debug("打断t1线程");
        t1.interrupt();
    }
}
