package retry;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/7 15:33
 * @Version 1.0
 *
 */
public class RetryConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test");
        consumer.setNamesrvAddr("192.168.100.101:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("msgTopic","*");

        //注册消息监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

                try {
                    //逐条消费消息
                    for (MessageExt messageExt : list) {
                        System.out.println(messageExt);
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }catch (Throwable e){
                    //方法1：发送异常返回消息需要重试(官方推荐)
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    //方法2：返回null
//                    return null;
                    //方法3：随意抛一个异常
//                    throw new RuntimeException("消费异常");
                }

            }
        });
        consumer.start();

    }
}
