package top.jolyoulu.readwritelock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: JolyouLu
 * @Date: 2022/7/23 13:35
 * @Version 1.0
 */
public class TestReadWriteLock {
    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();

        //测试并发读取，结果：多线程并发读不互斥
//        new Thread(() -> {
//            dataContainer.read();
//        },"t1").start();
//        new Thread(() -> {
//            dataContainer.read();
//        },"t2").start();

        //测试并发读写，结果：多线程并发互斥
        new Thread(() -> {
            dataContainer.read();
        },"t1").start();
        new Thread(() -> {
            dataContainer.write();
        },"t2").start();
    }
}
@Slf4j
class DataContainer{
    private Object data;
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock r = rw.readLock();
    private ReentrantReadWriteLock.WriteLock w = rw.writeLock();

    public Object read(){
        log.debug("获取读锁");
        r.lock();
        try {
            log.debug("read");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return data;
        }finally {
            log.debug("释放读锁");
            r.unlock();
        }
    }

    public void write(){
        log.debug("获取写锁");
        w.lock();
        try {
            log.debug("写入");
            this.data = data;
        }finally {
            w.unlock();
            log.debug("释放写锁");
        }
    }
}
