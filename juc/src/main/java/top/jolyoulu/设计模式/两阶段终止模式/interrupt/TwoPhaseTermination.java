package top.jolyoulu.设计模式.两阶段终止模式.interrupt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/30 16:53
 * @Version 1.0
 */
@Slf4j(topic = "TwoPhaseTermination")
public class TwoPhaseTermination {
    private Thread monitor;

    //启动监控线程
    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    log.debug("处理线程关闭前工作");
                    break;//结束循环
                }
                //睡眠2s后执行业务代码
                try {
                    TimeUnit.SECONDS.sleep(2);
                    log.debug("执行监控记录");
                } catch (InterruptedException e) {
                    //如果在sleep中被打断了，进入到异常时打断标记会被重置
                    //需要重新设置打断标记为true
                    current.interrupt();
                }
            }
        });
        monitor.start();
    }

    //停止监控线程
    public void stop() {
        monitor.interrupt();
    }
}
