package top.jolyoulu.newthreadcase;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author: JolyouLu
 * @Date: 2022/2/19 18:50
 * @Version 1.0
 */
@Slf4j
public class NewFutureTask {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建FutureTask对象，FutureTask可以返回参数
        FutureTask<Integer> task = new FutureTask<>(() -> {
            log.debug("FutureTask task");
            return 100;
        });
        //创建Thread类，将FutureTask传入到Thread中
        new Thread(task,"my-Thread").start();
        //main线程阻塞等待task执行完毕获取结果
        Integer result = task.get();
        log.debug("结果：{}",result);
    }
}
