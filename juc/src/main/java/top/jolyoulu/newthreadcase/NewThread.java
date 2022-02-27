package top.jolyoulu.newthreadcase;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: JolyouLu
 * @Date: 2022/2/19 17:33
 * @Version 1.0
 */
@Slf4j
public class NewThread {
    public static void main(String[] args) {
        //创建Thread类，重写run方法
        Thread thread = new Thread() {
            @Override
            public void run() {
                //需要执行的任务
                log.debug("Thread task");
            }
        };
        //自定义创建的线程名字
        thread.setName("my-Thread");
        //调用thread.start()方法启动线程
        thread.start();
        log.debug("main task");
    }
}
