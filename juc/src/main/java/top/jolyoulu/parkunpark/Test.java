package top.jolyoulu.parkunpark;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/5 15:25
 * @Version 1.0
 */
@Slf4j
public class Test {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("start...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("park...");
            LockSupport.park();
            log.debug("resume...");
        }, "t1");
        t1.start();

        //主线程先调用了park
        TimeUnit.SECONDS.sleep(1);
        log.debug("unpark...");
        LockSupport.unpark(t1);
    }
}
