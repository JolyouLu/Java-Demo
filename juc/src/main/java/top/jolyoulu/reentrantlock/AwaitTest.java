package top.jolyoulu.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 12:52
 * @Version 1.0
 * ReentrantLock 支持多休息室
 */
@Slf4j
public class AwaitTest {
    private final static ReentrantLock lock = new ReentrantLock();
    private final static Condition waitCigarette = lock.newCondition(); //构建一个等待烟的休息室
    private volatile static boolean hasCigarette = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            lock.lock();
            try {
                while (!Thread.currentThread().isInterrupted()){
                    log.debug("现在有烟?{}",hasCigarette);
                    if (!hasCigarette){
                        try {
                            log.debug("没有烟先挂起");
                            waitCigarette.await(); //进入到指定的休息室等待
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    log.debug("现在有烟?{}",hasCigarette);
                    if (hasCigarette){
                        log.debug("可以开始干活了");
                        break;
                    }
                }
            }finally {
                lock.unlock();
            }
        }, "小南").start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    log.debug("可以开始干活了");
                }finally {
                    lock.unlock();
                }
            }, "其它线程").start();
        }

        TimeUnit.SECONDS.sleep(1); //3秒后有个送烟的进来了
        new Thread(() -> {
            lock.lock();
            try {
                while (!Thread.currentThread().isInterrupted()){
                    if (!hasCigarette){
                        log.debug("烟来啦");
                        hasCigarette = true;
                        waitCigarette.signalAll(); //唤醒指定休息室中的线程
                        break;
                    }
                }
            }finally {
                lock.unlock();
            }
        }, "送烟的").start();
    }
}
