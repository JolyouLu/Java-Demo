package top.jolyoulu.manualack;

import com.rabbitmq.client.Channel;
import top.jolyoulu.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/22 14:10
 * @Version 1.0
 * 生产者 发送大量消息
 */
public class Producer {
    //队列名称
    public static final String QUEUE_NAME = "ack_queue";
    //发送消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取通道
        Channel channel = RabbitMqUtils.getChannel();
        //队列声明
        channel.queueDeclare(QUEUE_NAME, //队列名称
                false, //是否持久化到磁盘
                false, //该队列是否只能由一个消费者消费，是否进行消息共享
                false, //是否自动删除，最后一个消费者消费完毕后该队列是否自动删除
                null);//其它参数
        //消息发送（从控制台获取）
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("", //发送到那个交换机，不写使用默认交换机
                    QUEUE_NAME, //发送到那个队列
                    null, //附加参数
                    message.getBytes()); //消息体
            System.out.println("消息发送完毕："+ message);
        }
    }
}
