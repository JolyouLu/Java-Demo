package top.jolyoulu.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jolyoulu.utils.RabbitMqUtils;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/23 10:36
 * @Version 1.0
 */
public class ReceiveLogsDirect02 {
    
    private static final String EXCHANGE_NAME = "direct_logs";
    
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare("disk",false,false,false,null);
        //交换机绑定队列
        channel.queueBind("disk",EXCHANGE_NAME,"error");
        System.out.println("等待接收消息...");
        //接收消息回调
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogsDirect02接收到的消息："+new String(message.getBody()));
        };
        channel.basicConsume("disk",true,deliverCallback,consumerTag -> {});
    }
}
