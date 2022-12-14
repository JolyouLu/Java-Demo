package top.jolyoulu.readwritelock;

import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;

import java.security.PublicKey;
import java.util.concurrent.locks.StampedLock;

/**
 * @Author: JolyouLu
 * @Date: 2022/7/23 15:57
 * @Version 1.0
 */
public class TestStampedLock {
    public static void main(String[] args) {
        DataContainerStamped dataContainerStamped = new DataContainerStamped();
        //测试并发读取，结果：多线程并发读不互斥
//        new Thread(() -> {
//            dataContainerStamped.read();
//        },"t1").start();
//        new Thread(() -> {
//            dataContainerStamped.read();
//        },"t2").start();

        //测试并发读写，结果：多线程并发互斥
        new Thread(() -> {
            dataContainerStamped.read();
        },"t1").start();
        new Thread(() -> {
            dataContainerStamped.write(1);
        },"t2").start();
    }
}
@Slf4j
class DataContainerStamped{
    private final StampedLock lock = new StampedLock();
    private int data;

    public int read(){
        //乐观读，获取一个戳
        long stamp = lock.tryOptimisticRead();
        log.debug("optimistic to read lock...{}",stamp);
        if (lock.validate(stamp)){ //验证戳，如果返回false表示被修改过
            //如果未被修改返回数据
            log.debug("read finish...{}",stamp);
            return data;
        }
        log.debug("updating to read lock...{}",stamp);
        //锁设计
        try {
            stamp = lock.readLock();
            log.debug("read lock {}",stamp);
            log.debug("read finish...{}",stamp);
            return data;
        }finally {
            log.debug("read unlock {}",stamp);
            lock.unlockRead(stamp);
        }
    }

    public void write(int newData){
        //尝试加写锁，成功后返回戳(stamp)
        long stamp = lock.writeLock();
        log.debug("write lock {}",stamp);
        try {
            this.data = newData;
        }finally {
            log.debug("write unlock {}",stamp);
            //释放写锁，传入戳
            lock.unlockWrite(stamp);
        }
    }
}
