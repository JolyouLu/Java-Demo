package top.jolyoulu.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import top.jolyoulu.utils.RabbitMqUtils;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/23 10:36
 * @Version 1.0
 */
public class ReceiveLogs01 {
    
    private static final String EXCHANGE_NAME = "log";
    
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //声明一个队列临时队列
        String queueName = channel.queueDeclare().getQueue();
        //交换机绑定队列
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("等待接收消息...");
        //接收消息回调
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogs01接收到的消息："+new String(message.getBody()));
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }
}
