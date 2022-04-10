package top.jolyoulu.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 11:50
 * @Version 1.0
 * ReentrantLock 锁超时
 */
@Slf4j
public class TimeOutTest {
    private static final ReentrantLock lock = new ReentrantLock();
    //立刻失败
//    public static void main(String[] args) throws InterruptedException {
//        Thread t1 = new Thread(() -> {
//            //如果没有竞争次方法会获取lock对象，tryLock返回true
//            //如果存在竞争，tryLock立刻返回false，不进入阻塞队列
//            log.debug("尝试到锁了");
//            if (!lock.tryLock()){
//                log.debug("获取锁失败了");
//                return;
//            }
//            try {
//                log.debug("获取到了锁");
//            } finally {
//                lock.unlock();
//            }
//        }, "t1");
//        lock.lock(); //主线程先获取锁
//        t1.start();  //再启动t1线程（这时t1由于获取不到锁会立刻失败）
//    }
    //带超时时长可打断
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            //如果没有竞争次方法会获取lock对象，tryLock返回true
            //如果存在竞争，tryLock立刻返回false，不进入阻塞队列
            log.debug("尝试到锁了");
            try {
                if (!lock.tryLock(5,TimeUnit.SECONDS)){
                    log.debug("获取锁失败了");
                    return;
                }
            } catch (InterruptedException e) {
                log.debug("尝试锁时，被打断了");
                return;
            }
            try {
                log.debug("获取到了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");
        lock.lock(); //主线程先获取锁
        t1.start();  //再启动t1线程（这时t1尝试5秒获取锁）
        TimeUnit.SECONDS.sleep(1);
        t1.interrupt(); //主线程打断t1
    }

}
