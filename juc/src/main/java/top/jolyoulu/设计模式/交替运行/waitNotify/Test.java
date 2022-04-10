package top.jolyoulu.设计模式.交替运行.waitNotify;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 18:00
 * @Version 1.0
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
        WaitNotify notify = new WaitNotify(1, 5);
        Thread t1 = new Thread(() -> {
            notify.print("a",1,2);
        }, "t1");

        Thread t2 = new Thread(() -> {
            notify.print("b",2,3);
        }, "t2");

        Thread t3 = new Thread(() -> {
            notify.print("c",3,1);
        }, "t3");

        t1.start();
        t2.start();
        t3.start();
    }
}
