package top.jolyoulu.设计模式.固定运行顺序.parkUnpark;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 13:33
 * @Version 1.0
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            LockSupport.park(); //挂起t1线程
            log.debug("1");
        }, "t1");

        Thread t2 = new Thread(() -> {
            log.debug("2");
            LockSupport.unpark(t1); //执行完毕后取消挂起t1
        }, "t2");

        t1.start();
        t2.start();
    }
}
