package top.jolyoulu.lockcase;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/5 22:37
 * @Version 1.0
 * 活锁
 */
@Slf4j
public class LiveLockTest {
    private final static Object lock = new Object();
    private static volatile int count = 10;

    public static void main(String[] args) {
        new Thread(() -> {
            //期望减到0 退出循环
            while (count > 0){
                synchronized (lock){ count--; } //获取锁并且对count-1
                try {
                    TimeUnit.MILLISECONDS.sleep(100); //休眠100毫秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("count {}",count);
            }

        }, "t1").start();
        new Thread(() -> {
            //期望加到20 退出循环
            while (count < 20){
                synchronized (lock){ count++; } //获取锁并且对count+1
                try {
                    TimeUnit.MILLISECONDS.sleep(100); //休眠100毫秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("count {}",count);
            }
        }, "t2").start();
    }
}
