package top.jolyoulu;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/21 13:58
 * @Version 1.0
 */
public class PhoneCode {

    //生成6位验证码
    public static String getCode(){
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    //记录每个手机每天只能发送3次，以及验证码放入到redis
    public static void verifyCode(String phone){
        Jedis jedis = new Jedis("localhost",6379);
        //拼接key
        String countKey = "verifyCode" + phone + ":count"; //手机发送次数key
        String codeKey = "verifyCode" + phone + ":code"; //验证码的key

        //记录每个手机每天只能发送3次
        String count = jedis.get(countKey);
        //没有发送次数
        if (count == null){
            //如果没有设置一个初始，并且设置过期时间
            jedis.setex(countKey,60*60*24,"1");
        } else if (Integer.parseInt(count) <= 2) {
            //发送次数+1
            jedis.incr(countKey);
        }else if (Integer.parseInt(count) > 2) {
            //发送了3次，不能再发送
            System.out.println("今天发送次数以及超过三次");
        }

        //将生成的验证码放到redis中
        String vcode = getCode();
        jedis.setex(codeKey,60*20,vcode);
        jedis.close();
    }

    //验证码校验功能
    public static void getRedisCode(String phone,String code){
        //从redis获取验证码
        Jedis jedis = new Jedis("localhost",6379);
        String codeKey = "verifyCode" + phone + ":code"; //验证码的key
        String redisCode = jedis.get(codeKey);
        //判断
        if (redisCode.equals(code)){
            System.out.println("成功");
        }else {
            System.out.println("失败");
        }
        jedis.close();
    }

    public static void main(String[] args) {
        //模拟验证码生成，与发送
        verifyCode("12345678910");
        //校验验证码
        getRedisCode("12345678910","796472");
    }
}
