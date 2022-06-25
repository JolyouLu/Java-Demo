package top.jolyoulu.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Author: JolyouLu
 * @Date: 2022/6/25 11:35
 * @Version 1.0
 */
@Slf4j
public class AtomicStampedReferenceDemo {
    //AtomicStampedReferenceDemo 解决aba问题
    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A",0);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start..");
        String prev = ref.getReference(); //获取变量
        int stamp = ref.getStamp();       //获取版本号
        other(); //先执行一个other
        TimeUnit.SECONDS.sleep(1);
        //比较替换时，主线程的版本号还是旧的所以替换失败
        log.debug("version {} change A->C {}",stamp,ref.compareAndSet(prev,"C",stamp,stamp+1));
    }

    private static void other() throws InterruptedException {
        //将A替换成B
        new Thread(()->{
            int stamp = ref.getStamp();
            //比较替换时，需要比较版本号，并且更新成功后版本号加1
            log.debug("version {} change A->B {}",stamp,ref.compareAndSet(ref.getReference(),"B",stamp,stamp+1));
        },"t1").start();
        TimeUnit.MILLISECONDS.sleep(1);
        //将B替换成A
        new Thread(()->{
            int stamp = ref.getStamp();
            //比较替换时，需要比较版本号，并且更新成功后版本号加1
            log.debug("version {} change B->A {}",stamp,ref.compareAndSet(ref.getReference(),"A",stamp,stamp+1));
        },"t2").start();
    }
}
