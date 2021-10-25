package top.jolyoulu.dlq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jolyoulu.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/25 14:14
 * @Version 1.0
 * 普通队列的消费者
 */
public class Consumer01 {

    //普通交换
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //声明死信交换机和普通交换机类型未direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //普通队列参数
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE); //设置队列消息过期后转发到的死信交换机
        arguments.put("x-dead-letter-routing-key","dead"); //死信交换机RoutingKey
        //arguments.put("x-max-length",6); //设置队列长度
        //声明普通队列
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //将普通交换机与普通队列绑定
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"normal");
        //将死信交换机与死信队列绑定
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"dead");

        //接收普通队列消息
        //声明接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody());
            if (msg.equals("info5")){ //如果收到消息为info5那么拒绝掉
                //拒绝该消息，并且不重试该消息(成为死信消息)
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("Consumer01收到消息："+new String(message.getBody()));
                //返回成功
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        //开启手动应答
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,consumerTag -> {});
    }
}
