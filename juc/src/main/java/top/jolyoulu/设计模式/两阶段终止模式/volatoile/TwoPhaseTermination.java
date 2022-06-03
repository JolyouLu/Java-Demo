package top.jolyoulu.设计模式.两阶段终止模式.volatoile;

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
    //停止标记
    private volatile boolean stop = false;

    //启动监控线程
    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                if (stop) {
                    log.debug("处理线程关闭前工作");
                    break;//结束循环
                }
                //睡眠2s后执行业务代码
                try {
                    TimeUnit.SECONDS.sleep(2);
                    log.debug("执行监控记录");
                } catch (InterruptedException e) {
                    //当线程休眠时被打断会触发该
                    //这里不做如何处理进入下次循环后通过stop判断
                }
            }
        });
        monitor.start();
    }

    //停止监控线程
    public void stop() {
        stop = true;
        monitor.interrupt(); //设置打断标记
    }
}
