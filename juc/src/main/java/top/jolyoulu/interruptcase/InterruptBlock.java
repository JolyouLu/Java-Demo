package top.jolyoulu.interruptcase;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/3/5 22:45
 * @Version 1.0
 * 打断阻塞中的线程
 */
@Slf4j
public class InterruptBlock {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true){
                log.debug("t1线程 doing");
                try {
                    TimeUnit.SECONDS.sleep(1);
                    //这是一个必须处理的打断异常
                } catch (InterruptedException e) {
                    //进入到该异常表示该线程在阻塞时被其它线程调用了interrupt
                    //在该方法中，你应该做点什么如：释放资源、退出线程、关闭打断状态
                    log.debug("t1线程被打断了...");
                    //进入异常后打断标记会自动设置为false，若要保持打断标记调用Thread.currentThread().interrupt();
                    log.debug("t1线程打断标记={}",Thread.currentThread().isInterrupted());
                    break;//退出while循环
                }
            }
        },"t1");
        t1.start();
        TimeUnit.SECONDS.sleep(3);
        log.debug("打断t1线程");
        t1.interrupt();

    }
}
