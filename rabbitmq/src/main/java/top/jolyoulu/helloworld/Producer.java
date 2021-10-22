package top.jolyoulu.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/20 21:13
 * @Version 1.0
 */
public class Producer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发送消息
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
        //创建队列
        channel.queueDeclare(QUEUE_NAME, //队列名称
                true, //是否持久化到磁盘
                false, //该队列是否只能由一个消费者消费，是否进行消息共享
                false, //是否自动删除，最后一个消费者消费完毕后该队列是否自动删除
                null);//其它参数
        //消息发送
        String message = "hello world";
        channel.basicPublish("", //发送到那个交换机，不写使用默认交换机
                QUEUE_NAME, //发送到那个队列
                null, //附加参数
                message.getBytes()); //消息体
        System.out.println("消息发送完毕");
    }
}
