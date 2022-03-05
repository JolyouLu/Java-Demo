package top.jolyoulu.newthreadcase;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/3/6 1:03
 * @Version 1.0
 */
@Slf4j
public class DaemonThread {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true){
            }
        },"t1");
        t1.setDaemon(true);
        t1.start();
        Thread.sleep(500);
        log.debug("运行结束...");
    }
}
