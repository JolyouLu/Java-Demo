package top.jolyoulu.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jolyoulu.utils.RabbitMqUtils;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/23 10:36
 * @Version 1.0
 */
public class ReceiveLogsTopic02 {
    
    private static final String EXCHANGE_NAME = "topic_logs";
    
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明一个队列
        channel.queueDeclare("Q2",false,false,false,null);
        //交换机绑定队列
        channel.queueBind("Q2",EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind("Q2",EXCHANGE_NAME,"lazy.#");
        System.out.println("等待接收消息...");
        //接收消息回调
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogsTopic02接收到的消息："+new String(message.getBody())+
                    "绑定键："+message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume("Q2",true,deliverCallback,consumerTag -> {});
    }
}
