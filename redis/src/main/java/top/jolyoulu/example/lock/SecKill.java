package top.jolyoulu.example.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Random;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/22 12:48
 * @Version 1.0
 * 秒杀案例(会有库存剩余情况)
 */
public class SecKill {

    public static boolean doSecKill(String uid,String prodId){
        //判断以下uid和prodid非空
        if (uid == null || prodId == null){
            return false;
        }
        //连接redis
        Jedis jedis = new Jedis("localhost",6379);
        //拼接库存key
        //商品在redis中的key
        String kcKey = "sk:"+prodId+":qt";
        //秒杀成功user在redis中的set集合
        String userKey = "sk:"+prodId+":user";
        //监视库存
        jedis.watch(kcKey);
        //获取库存，如果null，秒杀还没开始
        String kc = jedis.get(kcKey);
        if (kc == null){
            System.out.println("秒杀还没开始，请等待");
            jedis.close();
            return false;
        }
        //判断用户如果已经在集合中表示他已经表示成功了
        if (jedis.sismember(userKey,uid)){
            System.out.println("已经秒杀成功，不能再秒杀了");
            jedis.close();
            return false;
        }
        //判断商品数量，库存小于1，秒杀结束
        if(Integer.parseInt(kc) <= 0){
            System.out.println("秒杀已经结束了");
            jedis.close();
            return false;
        }
        //秒杀业务逻辑,乐观锁+事务
        Transaction transaction = jedis.multi();
        //库存-1
        transaction.decr(kcKey);
        //将秒杀成功用户加入到set集合中
        transaction.sadd(userKey,uid);
        //结果
        List<Object> results = transaction.exec();
        if (results == null || results.size() == 0){
            System.out.println("秒杀失败了");
            jedis.close();
            return false;
        }else {
            System.out.println("秒杀成功了");
            jedis.close();
            return true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //1元秒杀ps5
        String prodId = "PlayStation5";
        //初始化ps5的库存数量10个
        Jedis jedis = new Jedis("localhost",6379);
        jedis.flushDB();
        jedis.set("sk:"+prodId+":qt","500");
        //模拟1000人同时参与秒杀
        for (int i = 0; i < 500; i++) {
            //生成随机用户ID
            new Thread(()->{
                String userid = new Random().nextInt(100)+"";
                SecKill.doSecKill(userid,prodId);
            }).start();
        }
        Thread.sleep(5000);
    }
}
