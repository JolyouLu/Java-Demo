package top.jolyoulu.newthreadcase;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: JolyouLu
 * @Date: 2022/2/19 17:47
 * @Version 1.0
 */
@Slf4j
public class NewRunnable {
    public static void main(String[] args) {
        //创建Runnable类，实现run方法
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //需要执行的任务
                log.debug("Runnable task");
            }
        };
        //创建Thread类，将Runnable传入到Thread中
        Thread thread = new Thread(runnable, "my-Thread");
        //调用thread.start()方法启动线程
        thread.start();
        log.debug("main task");
    }
}
