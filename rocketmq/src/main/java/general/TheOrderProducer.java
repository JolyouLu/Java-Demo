package general;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/7 21:33
 * @Version 1.0
 * 顺序消息发送
 */
public class TheOrderProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //创建一个producer，参数是Producer Group名称，多个Producer设置相同的Producer Group名称着是集群
        DefaultMQProducer producer = new DefaultMQProducer("test");
        //指定nameServer地址
        producer.setNamesrvAddr("192.168.100.101:9876");
        //开启生产者
        producer.start();

        for (int i = 0; i < 100; i++) {
            int orderId = i;
            byte[] body = ("msgBody," + i).getBytes();
            Message msg = new Message("msgTopic", "msgTag", body);
            msg.setKeys(String.valueOf(orderId));
            //使用自定义的队列选择器发送消息
            SendResult sendResult = producer.send(msg, new MyMsgQueueSelector(), orderId);
            System.out.println(sendResult);
        }
    }

    //自定义队列选择器
    private static class MyMsgQueueSelector implements MessageQueueSelector {

        //发送消息给Queue的选择算法
        @Override
        public MessageQueue select(List<MessageQueue> list, Message message, Object arg) {
            //获取key 方式1 ： producer.send(msg,new MyMsgQueueSelector(),orderId); orderId 与 arg 对应
            //int orderId = (int) arg;
            //获取key 方式2(推荐) ：msg.setKeys(String.valueOf(orderId)); 从msg中获取
            Integer orderId = Integer.valueOf(message.getKeys());
            //将订单号取模当前Topic中的Queue数量
            int index = orderId % list.size();
            return list.get(index);
        }
    }
}
