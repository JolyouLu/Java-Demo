package filter;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/14 12:35
 * @Version 1.0
 */
public class FillerSqlProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("test");
        producer.setNamesrvAddr("192.168.100.101:9876");
        producer.start();

        //生产并且发送100条消息
        for (int i = 0; i < 100; i++) {
            byte[] body = ("msgBody," + i).getBytes();
            Message msg = new Message("msgTopic", "msgTag", body);
            //为消息传入用户属性
            msg.putUserProperty("age",i+"");
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
        }
        producer.shutdown();
    }
}
