package top.jolyoulu.workqueue;

import com.rabbitmq.client.*;
import top.jolyoulu.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/22 21:31
 * @Version 1.0
 * 消费者多实例
 */
public class Consumer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取通道
        Channel channel = RabbitMqUtils.getChannel();
        //声明接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("收到消息："+new String(message.getBody()));
            //message.getEnvelope().getDeliveryTag()：消息标记
            //false：是否批量
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //声明消息被取消的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消费者取消消费结果回调逻辑");
        };
        //消息消费
        System.out.println("Consumer等待接收消息...");
        channel.basicConsume(QUEUE_NAME, //消费的队列
                false, //是否自动应答
                deliverCallback, //成功消费的回调
                cancelCallback); //消费者取消消费的回调
    }
}
