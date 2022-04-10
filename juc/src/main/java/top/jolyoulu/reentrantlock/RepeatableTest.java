package top.jolyoulu.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 11:18
 * @Version 1.0
 * ReentrantLock 可重复测试
 */
@Slf4j
public class RepeatableTest {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        lock.lock();
        try {
            method1();
        } finally {
            lock.unlock();
        }
    }

    public static void method1(){
        lock.lock();
        try {
            log.debug("method1");
            method2();
        } finally {
            lock.unlock();
        }
    }

    public static void method2(){
        lock.lock();
        try {
            log.debug("method2");
        } finally {
            lock.unlock();
        }
    }
}
