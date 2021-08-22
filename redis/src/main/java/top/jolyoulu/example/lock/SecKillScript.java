package top.jolyoulu.example.lock;

import com.sun.org.apache.bcel.internal.generic.NEW;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Random;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/22 12:48
 * @Version 1.0
 * 秒杀案例
 */
public class SecKillScript {

    private static String LUA_SCRIPT;
    static {
        StringBuilder builder = new StringBuilder();
        builder.append("local userid=KEYS[1];\r\n");
        builder.append("local prodId=KEYS[2];\r\n");
        builder.append("local kcKey=\"sk:\"..prodId..\":qt\";\r\n");
        builder.append("local usersKey=\"sk:\"..prodId..\":usr\";\r\n");
        builder.append("local userExists=redis.call(\"sismember\",usersKey,userid);\r\n");
        builder.append("if tonumber(userExists)==1 then\r\n");
        builder.append("  return 2;\r\n");
        builder.append("end\r\n");
        builder.append("local num= redis.call(\"get\" ,kcKey);\r\n");
        builder.append("if tonumber(num)<=0 then\r\n");
        builder.append("  return 0;\r\n");
        builder.append("else\r\n");
        builder.append("  redis.call(\"decr\",kcKey);\r\n");
        builder.append("  redis.call(\"sadd\",usersKey,userid);\r\n");
        builder.append("end\r\n");
        builder.append("return 1;\r\n");
        LUA_SCRIPT = builder.toString();
    }

    public static boolean doSecKill(String uid,String prodId){
        //判断以下uid和prodid非空
        if (uid == null || prodId == null){
            return false;
        }
        //连接redis
        Jedis jedis = new Jedis("localhost",6379);
        String scriptLoad = jedis.scriptLoad(LUA_SCRIPT);
        Object result = jedis.evalsha(scriptLoad, 2, uid, prodId);
        String res = String.valueOf(result);
        switch (res){
            case "0":
                System.out.println("已抢空了");
                break;
            case "1":
                System.out.println("抢购成功");
                break;
            case "2":
                System.out.println("该用户已抢购过");
                break;
            default:
                System.out.println("抢购异常");
                break;
        }
        jedis.close();
        return true;
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
                String userid = new Random().nextInt(300)+"";
                SecKillScript.doSecKill(userid,prodId);
            }).start();
        }
        Thread.sleep(5000);
    }
}
