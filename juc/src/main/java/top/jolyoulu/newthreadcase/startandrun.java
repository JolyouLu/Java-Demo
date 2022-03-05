package top.jolyoulu.newthreadcase;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: JolyouLu
 * @Date: 2022/2/27 14:07
 * @Version 1.0
 */
@Slf4j
public class startandrun {

    private static int count = 0;

    public static void main(String[] args) throws Exception{
        Thread t1 = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("t1 开始");
                Thread.sleep(100);
                log.debug("t1 赋值");
                count = 100;
                log.debug("t1 结束");
            }
        },"t1");
        t1.start();
        //等待t1结束
        t1.join();
        log.debug("结果为：{}",count);
    }
}
