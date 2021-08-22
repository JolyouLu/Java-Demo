package top.jolyoulu.example.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/22 12:15
 * @Version 1.0
 */
public class SimpOptimisticLock{
    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new Jedis("localhost",6379);
        //初始化2个key
        jedis.set("unlock_money","1000");
        jedis.set("lock_money","1000");
        //演示并发情况下的扣钱操作，50个redis同时操作key
        for (int i = 0; i < 50; i++) {
            //使用了watch+事务，实现乐观锁，操作lock_money扣钱
            new Thread(()->{
                Jedis j1 = new Jedis("localhost",6379);
                j1.watch("lock_money");
                Transaction transaction = j1.multi();
                transaction.incrBy("lock_money",-1000);
                transaction.exec();
            }).start();
            //无锁无事务的情况下，操作unlock_money扣钱操作
            new Thread(()->{
                Jedis j2 = new Jedis("localhost",6379);
                j2.incrBy("unlock_money",-1000);
            }).start();
        }
        //主线程等待t1与t2执行完毕后大于结果
        Thread.sleep(3000);
        System.out.println("未加锁并发情况下扣款结果："+jedis.get("unlock_money"));
        System.out.println("加锁并发情况下扣款结果："+jedis.get("lock_money"));
    }
}
