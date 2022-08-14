package top.jolyoulu.cyclicbarrier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Author: JolyouLu
 * @Date: 2022/7/23 22:41
 * @Version 1.0
 */
@Slf4j
public class TestCyclicBarrier {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        //与CountDownLatch不同之处 CyclicBarrier降到0后会重新恢复part
        //
        //
        // ies的值
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            //计数器0时会触发该方法
            log.debug("执行结束了");
        });

        service.submit(()->{
            log.debug("task1 begin...");
            try {
                barrier.await(); //计数器减一，挂起等待直到计数器0时才执行下面代码
                log.debug("task1 end...");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });

        service.submit(()->{
            log.debug("task2 begin...");
            try {
                barrier.await(); //计数器减一，挂起等待直到计数器0时才执行下面代码
                log.debug("task2 end...");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
