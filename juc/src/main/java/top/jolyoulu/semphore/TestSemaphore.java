package top.jolyoulu.semphore;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/7/23 20:23
 * @Version 1.0
 */
@Slf4j
public class TestSemaphore {
    public static void main(String[] args) {
        //设置3个许可证
        Semaphore semaphore = new Semaphore(3);
        //创建10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire(); //获取一个许可证，
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                try {
                    log.debug("running...");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    log.debug("end");
                }finally {
                    semaphore.release(); //释放许可证
                }
            }).start();
        }
    }
}
