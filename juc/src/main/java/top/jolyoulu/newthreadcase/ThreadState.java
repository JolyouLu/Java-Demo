package top.jolyoulu.newthreadcase;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/3/6 10:46
 * @Version 1.0
 */
@Slf4j
public class ThreadState {

    public static void main(String[] args) {
        Object lock = new Object();

        //NEW状态
        Thread t1 = new Thread(() -> {}, "t1");

        //RUNNABLE状态
        Thread t2 = new Thread(() -> {
            while (true);
        }, "t2");
        t2.start();

        //TERMINATED状态
        Thread t3 = new Thread(() -> {
        }, "t3");
        t3.start();

        //TIMED_WAITING状态
        Thread t4 = new Thread(() -> {
            try {
                synchronized (lock){
                    TimeUnit.DAYS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t4");
        t4.start();

        //WAITING状态
        Thread t5 = new Thread(() -> {
            Thread current = Thread.currentThread();
            synchronized (current){
                try {
                    current.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t5");
        t5.start();

        //BLOCKED状态
        Thread t6 = new Thread(() -> {
            synchronized (lock){

            }
        }, "t6");
        t6.start();

        log.debug("t1 start {}",t1.getState());
        log.debug("t2 start {}",t2.getState());
        log.debug("t3 start {}",t3.getState());
        log.debug("t4 start {}",t4.getState());
        log.debug("t5 start {}",t5.getState());
        log.debug("t6 start {}",t6.getState());
    }
}
