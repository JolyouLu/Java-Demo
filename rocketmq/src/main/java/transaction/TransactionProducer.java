package transaction;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import sun.nio.cs.ext.MS874;

import java.util.concurrent.*;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/9 23:17
 * @Version 1.0
 */
public class TransactionProducer {
    public static void main(String[] args) throws MQClientException {
        TransactionMQProducer producer = new TransactionMQProducer("transaction");
        producer.setNamesrvAddr("192.168.100.101:9876");

        //定义一个线程池
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        //为生产者指定一个线程池
        producer.setExecutorService(executorService);
        //为生产者添加事务监听器
        producer.setTransactionListener(new ICBCTransactionListener());
        producer.start();

        //发送3种不同的消息
        String[] tags = {"msgTagCommit","msgTagRollback","msgTagUnknow"};
        for (int i = 0; i < 3; i++) {
            byte[] body = ("msgBody," + i).getBytes();
            Message msg = new Message("msgTopic", tags[i], body);
            SendResult sendResult = producer.sendMessageInTransaction(msg, null);
        }

    }

    //事务监听器
    private static class ICBCTransactionListener implements TransactionListener{

        //消息预提交成功后就会触发该回调
        @Override
        public LocalTransactionState executeLocalTransaction(Message message, Object o) {
            System.out.println("预提交消息成功："+message);
            switch (message.getTags()){
                //假如收到msgTagCommit消息，表示执行扣款业务成功
                case "msgTagCommit":
                    System.out.println("扣款业务执行成功");
                    return LocalTransactionState.COMMIT_MESSAGE;
                //假如收到msgTagRollback消息，表示执行扣款业务失败
                case "msgTagRollback":
                    System.out.println("扣款业务执行失败");
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                //假如收到msgTagUnknow消息，表示执行扣款业务未有执行结果
                case "msgTagUnknow":
                    System.out.println("扣款业务执行中，未有结果");
                    return LocalTransactionState.UNKNOW;
                default:
                    return LocalTransactionState.UNKNOW;
            }
        }

        //消息回查
        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
            System.out.println("执行消息回查："+messageExt.getTags());
            System.out.println("正在查询数据库，获取预扣款结果");
            System.out.println("预扣款结果为成功");
            return LocalTransactionState.COMMIT_MESSAGE;
        }
    }
}
