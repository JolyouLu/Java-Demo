package top.jolyoulu.dlq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jolyoulu.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/25 14:14
 * @Version 1.0
 * 死信队列的消费者
 */
public class Consumer02 {

    //死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //接收死信队列消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02收到消息："+new String(message.getBody()));
        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumerTag -> {});
    }
}
