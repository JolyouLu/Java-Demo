package top.jolyoulu.zookeepr_curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/21 0:27
 * @Version 1.0
 */
public class CuratorLock implements Runnable{

    final static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.100.101:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 10))
            .build();
    static int i = 0;
    final InterProcessMutex lock = new InterProcessMutex(client,"/lock");

    public static void main(String[] args) throws InterruptedException {
        client.start();
        Thread thread1 = new Thread(new CuratorLock());
        Thread thread2 = new Thread(new CuratorLock());
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(i);
    }

    @Override
    public void run() {
        try {
            for (int j = 0; j < 300; j++) {
                lock.acquire();
                i++;
                lock.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
