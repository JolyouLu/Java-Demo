package retry;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/7 14:13
 * @Version 1.0
 * 同步消息发送
 */
public class SyncProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("test");
        producer.setNamesrvAddr("192.168.100.101:9876");
        //设置同步发送失败时重试发送的次数，默认2
        producer.setRetryTimesWhenSendFailed(3);
        //设置发送超时限制，默认3s
        producer.setSendMsgTimeout(5000);
        producer.start();

        //生产并且发送100条消息
        for (int i = 0; i < 100; i++) {
            byte[] body = ("msgBody," + i).getBytes();
            Message msg = new Message("msgTopic", "msgTag", body);
            //发送消息
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
        }
        producer.shutdown();
    }
}
