package top.jolyoulu.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author: JolyouLu
 * @Date: 2022/7/17 17:56
 * @Version 1.0
 */
@Slf4j
public class TestAqs{
    //测试代码
    public static void main(String[] args) {
        MyLock lock = new MyLock();
        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking...");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }finally {
                log.debug("unlock...");
                lock.unlock();
            }
        },"t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking...");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }finally {
                log.debug("unlock...");
                lock.unlock();
            }
        },"t2").start();
    }
}
class MyLock implements Lock{

    private MySync sync = new MySync();

    @Override //加锁
    public void lock() {
        sync.acquire(1);
    }

    @Override //加锁，可打断
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override //尝试加锁
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override //尝试加锁带时间
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override //解锁
    public void unlock() {
        sync.release(1);
    }

    @Override //创建条件变量
    public Condition newCondition() {
        return sync.newCondition();
    }

    //独占锁需要重写 state作为标记 0未加锁 1加锁
    //tryAcquire（该方法被并发调用） 获取锁
    //tryRelease 释放锁
    //isHeldExclusively 是否持有锁
    class MySync extends AbstractQueuedSynchronizer{

        @Override //多个线程调用该方法尝试获取锁
        protected boolean tryAcquire(int arg) {
            //尝试修改state标记未1
            if (compareAndSetState(0,1)){
                //加锁成功后将当前线程setExclusiveOwnerThread中
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false; //否则返回false
        }

        @Override //持有锁的线程释放锁
        protected boolean tryRelease(int arg) {
            //将state标记未0
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        @Override //当前线程是否持有锁
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition(){
            return new ConditionObject();
        }
    }
}
