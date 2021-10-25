package top.jolyoulu.dlq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import top.jolyoulu.utils.RabbitMqUtils;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/25 14:37
 * @Version 1.0
 * 普通消息生产者
 */
public class MaxLenProducer {

    //普通交换
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //发送ttl消息
        for (int i = 0; i < 10; i++) {
            String msg = "info"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"normal",null,msg.getBytes());
        }
    }
}
