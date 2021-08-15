package retry;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/7 14:13
 * @Version 1.0
 * 异步消息发送
 */
public class AsyncProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("test");
        producer.setNamesrvAddr("192.168.100.101:9876");
        //设置发送失败不重试
        producer.setRetryTimesWhenSendAsyncFailed(0);
        producer.start();

        //生产并且发送100条消息
        for (int i = 0; i < 100; i++) {
            byte[] body = ("msgBody," + i).getBytes();
            Message msg = new Message("msgTopic", "msgTag", body);
            //异步发送消息
            producer.send(msg, new SendCallback() {
                @Override
                //成功的回调事件
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult);
                }

                @Override
                public void onException(Throwable throwable) {

                }
            });
        }
        //生产环境下无需这样操作，如果不让线程挂起一下可能会消息未发送完毕后，主线程就退出了
        Thread.sleep(10);
        producer.shutdown();
    }
}
