package top.jolyoulu.cas;

import ch.qos.logback.core.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: JolyouLu
 * @Date: 2022/6/26 15:49
 * @Version 1.0
 */
@Slf4j
public class LockCas {
    private AtomicInteger state = new AtomicInteger(0);

    public static void main(String[] args) {
        LockCas lock = new LockCas();
        new Thread(() -> {
            log.debug("begin..");
            lock.lock();
            try {
                log.debug("lock..");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        },"t1").start();
        new Thread(() -> {
            log.debug("begin..");
            lock.lock();
            try {
                log.debug("lock..");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        },"t2").start();
    }

    public void lock(){
        while (true){
            if (state.compareAndSet(0,1)){
                break;
            }
        }
    }

    public void unlock(){
        state.set(0);
    }
}
