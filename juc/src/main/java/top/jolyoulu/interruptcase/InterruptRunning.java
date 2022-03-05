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
public class InterruptRunning {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true){
                //判断线程是否被打断Thread.currentThread().isInterrupted()
                //也可以调用Thread.interrupted()判断，但是注意Thread.interrupted()调用后会将打断标记重置为false
                if (Thread.currentThread().isInterrupted()){
                    log.debug("t1线程被打断");
                    //打断标记不会被清理，如果需要清理着可以调用
                    log.debug("t1线程打断标记={}",Thread.currentThread().isInterrupted());
                    break;//退出while循环
                }
                log.debug("t1线程 doing");
            }
        },"t1");
        t1.start();
        Thread.sleep(500);
        log.debug("打断t1线程");
        t1.interrupt();

    }
}
