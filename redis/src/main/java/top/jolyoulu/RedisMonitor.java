package top.jolyoulu;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisMonitor;
import top.jolyoulu.myrediscli.api.Client;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/24 2:04
 * @Version 1.0
 * Redis 监控简单实现,监控其它客户端的所有操作
 */
public class RedisMonitor {

    static class MonitorTask implements Runnable{
        private Jedis client;

        public MonitorTask(Jedis client) {
            this.client = client;
        }

        @Override
        public void run() {
            client.monitor(new JedisMonitor() {
                @Override
                public void onCommand(String command) {
                    System.out.println(command);
                }
            });
        }
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        Thread thread = new Thread(new MonitorTask(jedis));
        thread.start();
    }

}
