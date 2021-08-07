package general;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/7 15:33
 * @Version 1.0
 *
 */
public class ConsumerTest {
    public static void main(String[] args) throws MQClientException {
        //定义一个pull消费者
        //DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("test");
        //定义一个push消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test");
        //nameServer
        consumer.setNamesrvAddr("192.168.100.101:9876");
        //指定从第一条消息开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //指定消费topic与tag
        consumer.subscribe("msgTopic","*");
        //注册消息监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //逐条消费消息
                for (MessageExt messageExt : list) {
                    System.out.println(messageExt);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();

    }
}
