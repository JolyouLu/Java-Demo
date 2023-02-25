package top.jolyoulu.exportAllKey;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import redis.clients.jedis.*;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.io.*;
import java.time.LocalDateTime;
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

    private static final Map<String, AtomicInteger> outMap = new HashMap<>();

    private static String coursor = ScanParams.SCAN_POINTER_START;

    private static final Jedis jedis = new Jedis(new HostAndPort("120.77.156.109",6381),
            DefaultJedisClientConfig.builder().password("IUF3nnq9jbz").build());

    public static void main(String[] args){
        ExportAllKeys exportAllKeys = new ExportAllKeys();
        exportAllKeys.map();
        exportAllKeys.reduce();
    }

    private void map(){
        BufferedWriter writer = null;
        try {
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
                System.out.println(LocalDateTime.now()+" SCAN "+coursor+" MATCH * COUNT 10000 耗时："+(end-star)+"ms");
                if ("0".equals(coursor)){ //0表示没有数据了
                    break;
                }
            }
            writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\samson\\Desktop\\Rediskey统计\\6381.txt")));
            for (Map.Entry<String, AtomicInteger> entry : outMap.entrySet()) {
                OutObj outObj = new OutObj(entry.getKey(), entry.getValue().get());
                writer.write(JSONObject.toJSONString(outObj)+"\n");
            }
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (!Objects.isNull(writer)){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reduce(){
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            ArrayList<OutObj> list = new ArrayList<>();
            reader = new BufferedReader(new FileReader(new File("C:\\Users\\samson\\Desktop\\Rediskey统计\\6381.txt")));
            while (true){
                String line = reader.readLine();
                if (line == null){
                    break;
                }
                OutObj outObj = JSONObject.parseObject(line, OutObj.class);
                list.add(outObj);
            }
            list.sort(new Comparator<OutObj>() {
                @Override
                public int compare(OutObj o1, OutObj o2) {
                    if (o1.sum > o2.sum){
                        return -1;
                    }else if (o1.sum < o2.sum){
                        return 1;
                    }else {
                        return 0;
                    }
                }
            });
            writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\samson\\Desktop\\Rediskey统计\\6381_reduce.txt")));
            for (OutObj outObj : list) {
                writer.write(JSONObject.toJSONString(outObj)+"\n");
            }
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (!Objects.isNull(reader)){
                    reader.close();
                }
                if (!Objects.isNull(writer)){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class OutObj{

        public OutObj() {
        }

        public OutObj(String key, Integer sum) {
            this.key = key;
            this.sum = sum;
        }

        private String key;
        private Integer sum;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Integer getSum() {
            return sum;
        }

        public void setSum(Integer sum) {
            this.sum = sum;
        }
    }
}
