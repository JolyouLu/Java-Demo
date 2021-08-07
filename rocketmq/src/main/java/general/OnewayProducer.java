package general;

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
 * 单向消息发送
 */
public class OnewayProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //创建一个producer，参数是Producer Group名称，多个Producer设置相同的Producer Group名称着是集群
        DefaultMQProducer producer = new DefaultMQProducer("OnewayProducer");
        //指定nameServer地址
        producer.setNamesrvAddr("192.168.100.101:9876");
        //开启生产者
        producer.start();

        //生产并且发送100条消息
        for (int i = 0; i < 100; i++) {
            byte[] body = ("msgBody," + i).getBytes();
            Message msg = new Message("msgTopic", "msgTag", body);
            //发送消息
            producer.sendOneway(msg);
        }
        producer.shutdown();
    }
}
