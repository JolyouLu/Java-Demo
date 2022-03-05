package top.jolyoulu.interruptcase;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: JolyouLu
 * @Date: 2022/3/5 22:32
 * @Version 1.0
 * 请不要这样做这样的打断程序是没有意义的
 */
@Slf4j
public class DoNotInterrupt {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                log.debug("thread doing");
            }
        },"t1");
        t1.start();
        log.debug("打断t1线程");
        t1.interrupt();
    }
}
