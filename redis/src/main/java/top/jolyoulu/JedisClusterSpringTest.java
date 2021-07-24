package top.jolyoulu;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/18 20:24
 * @Version 1.0
 */
//Redis 集群客户端基础Spring案例
public class JedisClusterSpringTest {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        context.start();
        JedisCluster jedisCluster = (JedisCluster) context.getBean("redisCluster");
        System.out.println(jedisCluster.set("test1", "123"));
        System.out.println(jedisCluster.set("test2", "123"));
        System.out.println(jedisCluster.get("test2"));
        System.out.println(jedisCluster.get("test2"));
    }
}
