package general;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/7 23:09
 * @Version 1.0
 * 生产延时消息
 */
public class DelayProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //创建一个producer，参数是Producer Group名称，多个Producer设置相同的Producer Group名称着是集群
        DefaultMQProducer producer = new DefaultMQProducer("test");
        //指定nameServer地址
        producer.setNamesrvAddr("192.168.100.101:9876");
        //开启生产者
        producer.start();

        //生产并且发送10条消息
        for (int i = 0; i < 10; i++) {
            byte[] body = ("msgBody," + i).getBytes();
            Message msg = new Message("msgTopic", "msgTag", body);
            //延时等级3级，即延时10s后才被消费
            msg.setDelayTimeLevel(3);
            //延时消息
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
        }
        producer.shutdown();
    }
}
