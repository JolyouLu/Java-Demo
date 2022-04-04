package top.jolyoulu.WaitAndNotify;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/4 12:38
 * @Version 1.0
 */
@Slf4j
public class SimpTest2 {
    private final static Object lock = new Object();
    private volatile static boolean hasCigarette = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock) {
                while (!Thread.currentThread().isInterrupted()){
                    log.debug("现在有烟?{}",hasCigarette);
                    if (!hasCigarette){
                        try {
                            log.debug("没有烟先挂起");
                            lock.wait();
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
            }
        }, "小南").start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (lock) {
                    log.debug("可以开始干活了");
                }
            }, "其它线程").start();
        }

        TimeUnit.SECONDS.sleep(1); //3秒后有个送烟的进来了
        new Thread(() -> {
            synchronized (lock) {
                while (!Thread.currentThread().isInterrupted()){
                    if (!hasCigarette){
                        log.debug("烟来啦");
                        hasCigarette = true;
                        lock.notifyAll(); //唤醒小南
                        break;
                    }
                }
            }
        }, "送烟的").start();
    }
}
