package batch;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/11 20:58
 * @Version 1.0
 */
public class BatchProducer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("test");
        producer.setNamesrvAddr("192.168.100.101:9876");
        //指定要发送的消息最大大小，默认4M
        //除了修改该属性，还需要同时修改broker的配置文件中的maxMessageSize属性才会生效
        producer.setMaxMessageSize(1024 * 1024 * 4);
        producer.start();

        //把要发送的消息都放入集合中
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            byte[] body = ("msgBody," + i).getBytes();
            Message msg = new Message("msgTopic", "msgTag", body);
            messageList.add(msg);
        }

        //需要自己手动拆分消息列表，每次发送不能超4M
        MessageListSplitter splitter = new MessageListSplitter(messageList);
        while (splitter.hasNext()){
            try {
                List<Message> list = splitter.next();
                producer.send(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        producer.shutdown();
    }
}
