package top.jolyoulu.cas;

import ch.qos.logback.core.util.TimeUtil;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import sun.awt.windows.ThemeReader;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: JolyouLu
 * @Date: 2022/6/25 10:55
 * @Version 1.0
 */
@Slf4j
public class ABATest {
    //初始化一个原子引用string
    static AtomicReference<String> ref = new AtomicReference<>("A");

    public static void main(String[] args) throws InterruptedException {
      log.debug("main start..");
        String prev = ref.get();
        other(); //先执行一个other
        TimeUnit.SECONDS.sleep(1);
        //main 并没有发现A已经不是原来的A了，最终还是替换成功了
        log.debug("change A->C {}",ref.compareAndSet(prev,"C"));
    }

    private static void other() throws InterruptedException {
        //将A替换成B
        new Thread(()->{
            log.debug("change A->B {}",ref.compareAndSet(ref.get(),"B"));
        },"t1").start();
        TimeUnit.MILLISECONDS.sleep(1);
        //将B替换成A
        new Thread(()->{
            log.debug("change B->A {}",ref.compareAndSet(ref.get(),"A"));
        },"t2").start();
    }
}
