package top.jolyoulu.WaitAndNotify;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/3/27 19:56
 * @Version 1.0
 */
@Slf4j
public class SimpTest {
    private static final Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            log.debug("t1 执行...");
            synchronized (lock){ //首先获得到lock对象锁才能进入到该Monitor的WaitSet
                try {
                    log.debug("t1 被挂起");
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("t1 继续完成以下工作");
        },"t1").start();

        new Thread(()->{
            log.debug("t2 执行...");
            synchronized (lock){ //首先获得到lock对象锁才能进入到该Monitor的WaitSet
                try {
                    log.debug("t2 被挂起");
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("t2 继续完成以下工作");
        },"t2").start();
        //主线程休息2秒
        TimeUnit.SECONDS.sleep(2);
        synchronized (lock){
//            lock.notify(); //唤醒其中一个线程
            lock.notifyAll(); //唤醒所有线程
        }

    }
}
