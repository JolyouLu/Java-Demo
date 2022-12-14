package top.jolyoulu.exportAllKey;

import redis.clients.jedis.*;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author LuZhouJin
 * @Date 2022/12/14
 * 导出redis中的所有key
 */
public class ExportAllKeys {
    private static ExecutorService executorService = Executors.newFixedThreadPool(8);
    private static final Map<String, AtomicInteger> outMap = new ConcurrentHashMap<>();

    private static volatile String coursor = ScanParams.SCAN_POINTER_START;

    private static final Jedis jedis = new Jedis(new HostAndPort("120.77.156.109",6381),
            DefaultJedisClientConfig.builder().password("IUF3nnq9jbz").build());

    public static void main(String[] args) throws IOException {
        //匹配规则与分页个数
        ScanParams scanParams = new ScanParams();
        scanParams.match("*");
        scanParams.count(10000);
        while (true){
            long star = System.currentTimeMillis();
            ScanResult<String> scan = jedis.scan(coursor, scanParams);
            coursor = scan.getCursor();
            List<String> key = scan.getResult();
            key.forEach(item -> {
                String[] split = item.split(":");
                for (int i = 0; i < split.length; i++) {
                    if (split[i].length() == 32){
                        split[i] = "*";
                    }
                    Pattern pattern = Pattern.compile("[1-9]");
                    Matcher matcher = pattern.matcher(split[i]);
                    if (matcher.find()){
                        split[i] = "*";
                    }
                }
                String k = String.join(":",split);
                if (outMap.containsKey(k)) {
                    outMap.get(k).incrementAndGet();
                }else {
                    outMap.put(k,new AtomicInteger(1));
                }
            });
            long end = System.currentTimeMillis();
            System.out.println("SCAN "+coursor+" MATCH * COUNT 10000 耗时："+(end-star)+"ms");
            if ("0".equals(coursor)){ //0表示没有数据了
                break;
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\samson\\Desktop\\Rediskey统计\\6381.txt")));
        for (Map.Entry<String, AtomicInteger> entry : outMap.entrySet()) {
            writer.write("sum："+entry.getValue()+"\tkey："+entry.getKey()+"\n");
        }
        writer.flush();
    }
}
