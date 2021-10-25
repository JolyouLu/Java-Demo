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
public class TTLProducer {

    //普通交换
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //指定消息过期时间
        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder().expiration("10000").build();
        //发送ttl消息
        for (int i = 0; i < 10; i++) {
            String msg = "info"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"normal",properties,msg.getBytes());
        }
    }
}
