package top.jolyoulu;

import com.sun.org.apache.xpath.internal.operations.NotEquals;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;
import sun.java2d.pipe.SpanIterator;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/20 22:41
 * @Version 1.0
 */
public class JRedisApiTest {
    private static Jedis jedis;

    @Before
    public void init(){
        jedis = new Jedis("localhost",6379);
    }

    //基础命令使用测试
    @Test
    public void test(){
        System.out.println("清空当前数据："+jedis.flushDB());
        System.out.println("判断某个键是否存在："+jedis.exists("username"));
        System.out.println("新增<username,lzj>的键值对："+jedis.set("username","lzj"));
        Set<String> keys = jedis.keys("*");
        System.out.println("当前Redis下所有数据："+keys);
        System.out.println("查看key所存储的值的类型："+jedis.type("username"));
        System.out.println("删除key："+jedis.del("username"));
    }

    //key的增删改查
    @Test
    public void testStringCRUD(){
        jedis.flushDB();
        System.out.println("新增1个键值对："+jedis.set("key1", "value1"));
        System.out.println("在key1后面追加内容："+jedis.append("key1", "append"));
        System.out.println("获取1个key的值："+jedis.get("key1"));
        jedis.flushDB();
        System.out.println("一次性设置多个键值对："+jedis.mset("key1","value1","key2","value2","key3","value3"));
        System.out.println("一次性获取多个键值对："+jedis.mget("key1","key2","key3"));
        System.out.println("一次性删除多个键值对："+jedis.del("key1","key2"));
        System.out.println("一次性获取多个键值对："+jedis.mget("key1","key2","key3"));
        jedis.flushDB();
        System.out.println("=========新增并检查key，不存在新增，存在不操作=========");
        System.out.println("新增1个键值对："+jedis.setnx("key4", "value4"));
        System.out.println("尝试修改key4："+jedis.setnx("key4", "newValue4"));
        System.out.println("获取key4的值："+jedis.get("key4"));
        System.out.println("================获取原值，并传入新值================");
        System.out.println("新增1个键值对："+jedis.set("key5", "value5"));
        System.out.println("获取key5并且传入新值："+jedis.getSet("key5", "newValue5"));
        System.out.println("获取key5的值："+jedis.get("key5"));
    }

    //key的有效期设置
    @Test
    public void testStringTimeOut(){
        jedis.flushDB();
        System.out.println("新增key并且设置60秒后过期："+jedis.setex("username",60L,"lzj"));
        System.out.println("查看key过期倒计时（秒）："+jedis.ttl("username"));
        System.out.println("查看key过期倒计时（毫秒）："+jedis.pttl("username"));
        System.out.println("修改key10秒后过期："+jedis.expire("username",10L));
        System.out.println("查看key过期倒计时（秒）："+jedis.ttl("username"));
    }

    //分布式下的原子操作 i++ 或 i--
    @Test
    public void testIncr() throws InterruptedException {
        System.out.println("=========多客户端同时操作num进行原子性=========");
        jedis.flushDB();
        //创建一个Runnable循环对指定num进行原子性+1操作
        class NumIncr implements Runnable{
            //创建一个Jedis客户端
            private final Jedis ThreadJedis = new Jedis("localhost",6379);
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                for (int i = 0; i < 5; i++) {
                    System.out.println(name + "原子性操作num+1："+ThreadJedis.incr("num"));
//                    System.out.println(name + "原子性操作num-1："+ThreadJedis.decr("num"));
                }
            }
        }
        Thread t1 = new Thread(new NumIncr(),"线程1");
        Thread t2 = new Thread(new NumIncr(),"线程2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("查看num的值："+jedis.get("num"));
    }

    //list操作
    @Test
    public void testArray(){
        jedis.flushDB();
        System.out.println("新增1个List："+jedis.rpush("list","3","6","2","7","1","2"));
        System.out.println("查看List内容："+jedis.lrange("list",0,-1));
        System.out.println("查看List长度："+jedis.llen("list"));
        System.out.println("对List内容排序并返回："+jedis.sort("list"));
        System.out.println("查看List内容："+jedis.lrange("list",0,-1));
        System.out.println("获取下标0的值："+jedis.lindex("list",0));
        System.out.println("替换下标0的值："+jedis.lset("list",0,"three"));
        System.out.println("查看List内容："+jedis.lrange("list",0,-1));
        System.out.println("在three前面插入值(before)："+jedis.linsert("list", ListPosition.BEFORE,"three","before"));
        System.out.println("在three后面插入值(after)："+jedis.linsert("list", ListPosition.AFTER,"three","after"));
        System.out.println("查看List内容："+jedis.lrange("list",0,-1));
        System.out.println("保留下标0-2元素其余丢弃："+jedis.ltrim("list",0,2));
        System.out.println("查看List内容："+jedis.lrange("list",0,-1));
    }

    //list操作-队列
    @Test
    public void testQueue(){
        jedis.flushDB();
        System.out.println("===============利用List完成队列入队操作===============");
        System.out.println("从queue左侧插入元素："+jedis.lpush("queue","1"));
        System.out.println("查看queue内容："+jedis.lrange("queue",0,-1));
        System.out.println("从queue左侧插入元素："+jedis.lpush("queue","2"));
        System.out.println("查看queue内容："+jedis.lrange("queue",0,-1));
        System.out.println("从queue左侧插入元素："+jedis.lpush("queue","3"));
        System.out.println("查看queue内容："+jedis.lrange("queue",0,-1));
        System.out.println("===============利用List完成队列出队操作===============");
        System.out.println("查看queue内容："+jedis.lrange("queue",0,-1));
        System.out.println("从queue右侧获取元素："+jedis.rpop("queue"));
        System.out.println("查看queue内容："+jedis.lrange("queue",0,-1));
        System.out.println("从queue右侧获取元素："+jedis.rpop("queue"));
        System.out.println("查看queue内容："+jedis.lrange("queue",0,-1));
        System.out.println("从queue右侧获取元素："+jedis.rpop("queue"));
    }

    //list操作-栈
    @Test
    public void testStack(){
        jedis.flushDB();
        System.out.println("===============利用List完成入栈操作===============");
        System.out.println("从stack左侧插入元素："+jedis.lpush("stack","1"));
        System.out.println("查看stack内容："+jedis.lrange("stack",0,-1));
        System.out.println("从stack左侧插入元素："+jedis.lpush("stack","2"));
        System.out.println("查看stack内容："+jedis.lrange("stack",0,-1));
        System.out.println("从stack左侧插入元素："+jedis.lpush("stack","3"));
        System.out.println("查看stack内容："+jedis.lrange("stack",0,-1));
        System.out.println("===============利用List完成出栈操作===============");
        System.out.println("查看stack内容："+jedis.lrange("stack",0,-1));
        System.out.println("从stack左侧获取元素："+jedis.lpop("stack"));
        System.out.println("查看stack内容："+jedis.lrange("stack",0,-1));
        System.out.println("从stack左侧获取元素："+jedis.lpop("stack"));
        System.out.println("查看queue内容："+jedis.lrange("stack",0,-1));
        System.out.println("从stack左侧获取元素："+jedis.lpop("stack"));
    }

    //set操作
    @Test
    public void testSet(){
        jedis.flushDB();
        System.out.println("新增1个set："+jedis.sadd("set","s1","s2","s3","s4","s5","s6"));
        System.out.println("查看set内容："+jedis.smembers("set"));
        System.out.println("往set添加重复内容："+jedis.sadd("set","s1"));
        System.out.println("往set添加重复内容："+jedis.sadd("set","s2"));
        System.out.println("查看set内容："+jedis.smembers("set"));
        System.out.println("删除s6元素："+jedis.srem("set","s6"));
        System.out.println("查看set大小："+jedis.scard("set"));
        System.out.println("查看set内容是否有s1元素："+jedis.sismember("set","s1"));
        System.out.println("随机获取set中1个元素："+jedis.srandmember("set",1));
        System.out.println("随机获取set中1个元素并且删除："+jedis.spop("set",1));
        System.out.println("查看set大小："+jedis.scard("set"));
        System.out.println("查看set内容："+jedis.smembers("set"));
        System.out.println("====================集合运算====================");
        System.out.println("新增1个set1："+jedis.sadd("set1","s1","s2","s3","s4","s5","s6"));
        System.out.println("新增1个set2："+jedis.sadd("set2","s4","s5","s6","s7","s8","s9"));
        System.out.println("查看set1内容："+jedis.smembers("set1"));
        System.out.println("查看set2内容："+jedis.smembers("set2"));
        System.out.println("set1和set2的交集："+jedis.sinter("set1","set2"));
        System.out.println("set1和set2的并集："+jedis.sunion("set1","set2"));
        System.out.println("set1和set2的差集："+jedis.sdiff("set1","set2"));
    }


    //HasMap操作
    @Test
    public void testMap(){
        jedis.flushDB();
        System.out.println("新增1个hasMap："+jedis.hset("map","key1","value1"));
        HashMap<String, String> map = new HashMap<>();
        map.put("key2","value2");
        map.put("key3","value3");
        System.out.println("对map一次性设置值："+jedis.hmset("map",map));
        System.out.println("获取map所有键值对："+jedis.hgetAll("map"));
        System.out.println("获取map所有键："+jedis.hkeys("map"));
        System.out.println("获取map所有值："+jedis.hvals("map"));
        System.out.println("获取map长度："+jedis.hlen("map"));
        System.out.println("删除map的中值："+jedis.hdel("map","key1"));
        System.out.println("获取map所有键值对："+jedis.hgetAll("map"));
        System.out.println("查看map内容是否有key2："+jedis.hexists("map","key2"));
        System.out.println("=========新增并检查key，不存在新增，存在不操作=========");
        System.out.println("往map新增key4："+jedis.hsetnx("map","key4","value4"));
        System.out.println("尝试修改key4值："+jedis.hsetnx("map","key4","newValue4"));
        System.out.println("获取key4值："+jedis.hget("map","key4"));
    }

    //ZSet操作-有序集合
    @Test
    public void testZSet(){
        jedis.flushDB();
        //以下map为一份成绩表
        HashMap<String, Double> transcript = new HashMap<>();
        transcript.put("zhangsan",60D);
        transcript.put("lisi",30D);
        transcript.put("wangwu",100D);
        transcript.put("zhaoliu",85D);
        System.out.println("新增1个ZSet："+jedis.zadd("transcript",transcript));
        System.out.println("从小到大排序："+jedis.zrangeByScore("transcript",0,-1));
        System.out.println("获取："+jedis.zrange("transcript",0,-1));
    }

    //geospatial操作-地理位置
    @Test
    public void testGeospatial(){
        jedis.flushDB();
        //以下map为一份城市经纬度表
        HashMap<String, GeoCoordinate> cityMap = new HashMap<>();
        cityMap.put("beijing", new GeoCoordinate(116.41667,39.91667));
        cityMap.put("shanghai",new GeoCoordinate(121.43333,34.50000));
        cityMap.put("guangzhou",new GeoCoordinate(113.23333,23.16667));
        cityMap.put("shenzhen",new GeoCoordinate(114.06667,22.61667));
        System.out.println("新增1个Geospatial："+jedis.geoadd("geospatial", cityMap));
        System.out.println("查看所有城市信息："+jedis.zrange("geospatial",0,-1));
        System.out.println("获取beijing的经纬度："+jedis.geopos("geospatial", "beijing"));
        System.out.println("计算beijing到shanghai直线距离(km)："+jedis.geodist("geospatial", "beijing","shanghai", GeoUnit.KM));
        System.out.println("========================获取经纬度(116,39)半径1000km的城市========================");
        List<GeoRadiusResponse> list1 = jedis.georadius("geospatial", 116,39, 1000, GeoUnit.KM);
        list1.forEach(g -> System.out.println(g.getMemberByString()));
        System.out.println("========================获取guangzhou半径1000km的城市========================");
        List<GeoRadiusResponse> list2 = jedis.georadiusByMember("geospatial", "guangzhou", 1000, GeoUnit.KM);
        list2.forEach(g -> System.out.println(g.getMemberByString()));
        System.out.println("删除指定城市："+jedis.zrem("geospatial","beijing"));
    }

    //Hyperloglog操作-基数统计(A={1,2,5,6,6,8}为例，基数=4 就是这给集合中不重复的数)
    @Test
    public void testHyperloglog(){
        jedis.flushDB();
        System.out.println("新增1个Hyperloglog："+jedis.pfadd("hyperloglog","1","2","5","6","6","8"));
        System.out.println("基数统计："+jedis.pfcount("hyperloglog"));
    }

    //Bitmap操作-位存储(只能存储0/1，消耗内存最小)
    @Test
    public void testBitmap(){
        jedis.flushDB();
        System.out.println("========================小明的考勤表========================");
        jedis.setbit("xiaoming",1,true);
        jedis.setbit("xiaoming",2,false);
        jedis.setbit("xiaoming",3,false);
        jedis.setbit("xiaoming",4,true);
        jedis.setbit("xiaoming",5,true);
        System.out.println("小明周一是否有上课："+jedis.getbit("xiaoming",1));
        System.out.println("小明周二是否有上课："+jedis.getbit("xiaoming",2));
        System.out.println("小明周三是否有上课："+jedis.getbit("xiaoming",3));
        System.out.println("小明周四是否有上课："+jedis.getbit("xiaoming",4));
        System.out.println("小明周五是否有上课："+jedis.getbit("xiaoming",5));
    }
}
