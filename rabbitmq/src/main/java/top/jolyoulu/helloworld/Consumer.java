package top.jolyoulu.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/20 21:31
 * @Version 1.0
 */
public class Consumer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置rabbitMq连接地址
        factory.setHost("192.168.100.101");
        //设置用户名与密码
        factory.setUsername("admin");
        factory.setPassword("123");

        //创建连接
        Connection connection = factory.newConnection();
        //获取通道
        Channel channel = connection.createChannel();
        //声明接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };
        //声明消息被取消的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断");
        };
        //消息消费
        channel.basicConsume(QUEUE_NAME, //消费的队列
                true, //是否自动应答
                deliverCallback, //成功消费的回调
                cancelCallback); //消费者取消消费的回调
    }
}
