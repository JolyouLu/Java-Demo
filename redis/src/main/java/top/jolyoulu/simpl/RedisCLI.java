package top.jolyoulu.simpl;

import lombok.Data;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisMovedDataException;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Function;

/**
 * @Author LuZhouJin
 * @Date 2022/12/21
 */
public class RedisCLI {

    private static final String[] delKeys = {
            "FZD:USER:LEARN:CACHE:USERID:c7b5c15a043f42ee8a5814793f9574df",
            "FZD:USER:LEARN:CACHE:USERID:375cd710396c4c1b93e287dfc8e876cd",
            "FZD:USER:LEARN:CACHE:USERID:cbfba169c7b211ec97e300163e067b9d",
            "FZD:USER:LEARN:CACHE:USERID:de00af87beb345ec8865c200b84af7a4",
            "FZD:USER:LEARN:CACHE:USERID:feb3e2a90bbd4a9b818a3a887bd1a4e4",
            "FZD:USER:LEARN:CACHE:USERID:ebd2dadc96b84311ac5b95e17f9b4bf5",
            "FZD:USER:LEARN:CACHE:USERID:e25c4ec515854a5295c852275e0f25a9",
            "FZD:USER:LEARN:CACHE:USERID:b3dceea04d8345379851c72a6347b809",
            "FZD:USER:LEARN:CACHE:USERID:272e6667f3264db6b4dfedc6c9dd34ea",
            "FZD:USER:LEARN:CACHE:USERID:73b897208c6745298d19670484365bcd",
            "FZD:USER:LEARN:CACHE:USERID:48d85fde4c5811edb3cf00163e0cfb6a",
            "FZD:USER:LEARN:CACHE:USERID:e5db01c2cf58434e94181389565b04b5",
            "FZD:USER:LEARN:CACHE:USERID:ce195a60861040c5821084b6a618770e",
            "FZD:USER:LEARN:CACHE:USERID:3a11bc0962824241868aacbb77b67d4c",
            "FZD:USER:LEARN:CACHE:USERID:5b630e3b162e11eca34600163e10783a",
            "FZD:USER:LEARN:CACHE:USERID:74b1c9b053a140d3b2d5ae13a5da14c9",
            "FZD:USER:LEARN:CACHE:USERID:fe1a7132555b48878be3a7008fabc095",
            "FZD:USER:LEARN:CACHE:USERID:063eb0fbd6ab45d6bbec42f617c8fcee",
            "FZD:USER:LEARN:CACHE:USERID:bb314911ea414a86a0ff6dbfb2840bdf",
            "FZD:USER:LEARN:CACHE:USERID:f29cd32ba3764cd6b68cbf705c17949b",
            "FZD:USER:LEARN:CACHE:USERID:c12af4ac1f7f456dba7418f9808ea64c",
            "FZD:USER:LEARN:CACHE:USERID:f041d5ff2a30401591961c6ae8936eee",
            "FZD:USER:LEARN:CACHE:USERID:2ee42e7868064fa3acbc6b7493101d81",
            "FZD:USER:LEARN:CACHE:USERID:5cc45817233a46f7a97b4b822b9b5bbf",
            "FZD:USER:LEARN:CACHE:USERID:444c08174adc4e288cfbb7121ff1a140",
            "FZD:USER:LEARN:CACHE:USERID:203554",
            "FZD:USER:LEARN:CACHE:USERID:6ae15cddb6164981ac1001ae25734c0d",
            "FZD:USER:LEARN:CACHE:USERID:119034d9e0774a74a167279abe9e6df2",
            "FZD:USER:LEARN:CACHE:USERID:db0ff16812634850ba47e105e159f879",
            "FZD:USER:LEARN:CACHE:USERID:e6583be79dfb46b7a2542990b0a68c68",
            "FZD:USER:LEARN:CACHE:USERID:7b9a457dc7174bcb8b2e650c5dd97a4e",
            "FZD:USER:LEARN:CACHE:USERID:1c9097a61dd64ecca13ec1cd5b61d554",
            "FZD:USER:LEARN:CACHE:USERID:cd480765164f11eda3b100163e0cfb6a",
            "FZD:USER:LEARN:CACHE:USERID:ab87b0237a4111eba3ff0242ac110002",
            "FZD:USER:LEARN:CACHE:USERID:eb94fed852af4201a001f66eb5ff0ea3",
            "FZD:USER:LEARN:CACHE:USERID:3fb32a097786477f9bb2ac6ff9a5bede",
            "FZD:USER:LEARN:CACHE:USERID:8cf83ccb54264f26a8ae0ed7f0154949",
            "FZD:USER:LEARN:CACHE:USERID:5ac7e1a8df484b6e94737738794be1de",
            "FZD:USER:LEARN:CACHE:USERID:e6d35d5eee334e00a399064460d9cad6",
            "FZD:USER:LEARN:CACHE:USERID:e8e7508491ac4b5ab3ff6e3b5f205c4a",
            "FZD:USER:LEARN:CACHE:USERID:43889280dec74ff69e6473fe0b8a7dde",
    };

    public static void main(String[] args) {
        Function<Task<Object>, Object> run = new Function<Task<Object>, Object>() {
            @Override
            public Object apply(Task<Object> t) {
                Jedis jedis = t.getJedis();
                for (String key : delKeys) {
                    try {
                        System.out.println(t.getHost()+":"+t.getPort()+" 删除 =>" + key + " 状态 =>" + jedis.del(key));
                    } catch (JedisMovedDataException e) {
//                        System.out.println("异常 =>" + e.getMessage());
                    }
                }
                return null;
            }
        };
        Task<Object> jedis6381 = new Task<Object>(" ", 0, " ", run);
        Task<Object> jedis6383 = new Task<Object>(" ", 0, " ", run);
        Task<Object> jedis6385 = new Task<Object>(" ", 0, " ", run);
        Task<Object> jedis6387 = new Task<Object>(" ", 0, " ", run);
        FutureTask<Object> futureTask6381 = new FutureTask<>(jedis6381);
        FutureTask<Object> futureTask6383 = new FutureTask<>(jedis6383);
        FutureTask<Object> futureTask6385 = new FutureTask<>(jedis6385);
        FutureTask<Object> futureTask6387 = new FutureTask<>(jedis6387);
        new Thread(futureTask6381).start();
        new Thread(futureTask6383).start();
        new Thread(futureTask6385).start();
        new Thread(futureTask6387).start();
        try {
            futureTask6381.get();
            futureTask6383.get();
            futureTask6385.get();
            futureTask6387.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class Task<V> implements Callable<V> {
        private String host;
        private Integer port;
        private Jedis jedis;
        private Function<Task<V>, V> run;

        public Task(String host, Integer port, String pwd, Function<Task<V>, V> run) {
            this.host = host;
            this.port = port;
            this.run = run;
            jedis = new Jedis(new HostAndPort(host, port), DefaultJedisClientConfig.builder().password(pwd).build());
        }

        @Override
        public V call() {
            return this.run.apply(this);
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public Jedis getJedis() {
            return jedis;
        }

        public void setJedis(Jedis jedis) {
            this.jedis = jedis;
        }

        public Function<Task<V>, V> getRun() {
            return run;
        }

        public void setRun(Function<Task<V>, V> run) {
            this.run = run;
        }
    }
}
