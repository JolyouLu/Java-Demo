package top.jolyoulu.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 11:35
 * @Version 1.0
 * ReentrantLock 可打断测试
 */
@Slf4j
public class CanBeInterruptTest {
    private static final ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                //如果没有竞争次方法会获取lock对象
                //如果存在竞争就会进入阻塞队列，但是其它线程使用interrupt可以打断该线程的等待
                log.debug("尝试到锁了");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                //打断后执行的内容
                log.debug("再阻塞队列中，被打断了");
                return;
            }
            try {
                log.debug("获取到锁了");
            } finally {
                lock.unlock();
            }
        }, "t1");
        lock.lock(); //主线程先获取锁
        t1.start();  //再启动t1线程（这时t1由于获取不到锁会进入阻塞队列）
        TimeUnit.SECONDS.sleep(1); //主线程休眠1秒
        t1.interrupt(); //主线程将t1线程打断（t1执行catch方法）
        lock.unlock();
    }
}
