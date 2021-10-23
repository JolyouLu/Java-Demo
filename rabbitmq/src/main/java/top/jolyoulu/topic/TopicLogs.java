package top.jolyoulu.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import top.jolyoulu.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/23 10:35
 * @Version 1.0
 */
public class TopicLogs {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //发送消息
        Map<String,String> msgMap = new HashMap<>();
        msgMap.put("test.test.rabbit","被队列Q1收到");
        msgMap.put("lazy.test.test","被队列Q2收到");
        msgMap.put("test.orange.rabbit","被队列Q1、Q2收到");
        msgMap.put("lazy.orange.test","被队列Q1、Q2收到");
        for (Map.Entry<String, String> entry : msgMap.entrySet()) {
            channel.basicPublish(EXCHANGE_NAME,entry.getKey(),null,entry.getValue().getBytes());
            System.out.println("生产者消息发送成功");
        }
    }

}
