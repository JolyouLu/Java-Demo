package top.jolyoulu;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import javax.print.attribute.HashPrintServiceAttributeSet;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/18 20:24
 * @Version 1.0
 */
//Redis 集群客户端案例
public class JedisClusterTest {
    public static void main(String[] args) throws IOException {
        Set<HostAndPort> jedisClusterNode = new HashSet<>();
        jedisClusterNode.add(new HostAndPort("192.168.100.101",8001));
        jedisClusterNode.add(new HostAndPort("192.168.100.101",8002));
        jedisClusterNode.add(new HostAndPort("192.168.100.102",8003));
        jedisClusterNode.add(new HostAndPort("192.168.100.102",8004));
        jedisClusterNode.add(new HostAndPort("192.168.100.103",8005));
        jedisClusterNode.add(new HostAndPort("192.168.100.103",8006));

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(10);
        config.setTestOnBorrow(true);

        JedisCluster jedisCluster = new JedisCluster(jedisClusterNode,
                6000,
                5000,
                10,
                "123456",
                config);

        System.out.println(jedisCluster.set("test1", "123"));
        System.out.println(jedisCluster.set("test2", "123"));

        System.out.println(jedisCluster.get("test2"));
        System.out.println(jedisCluster.get("test2"));

        jedisCluster.close();
    }
}
