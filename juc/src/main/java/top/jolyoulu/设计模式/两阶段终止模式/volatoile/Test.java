package top.jolyoulu.设计模式.两阶段终止模式.volatoile;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/30 16:52
 * @Version 1.0
 */
@Slf4j(topic = "Test")
public class Test {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();
        //主线程休眠5秒后
        TimeUnit.SECONDS.sleep(5);
        //停止监控
        tpt.stop();
    }
}
