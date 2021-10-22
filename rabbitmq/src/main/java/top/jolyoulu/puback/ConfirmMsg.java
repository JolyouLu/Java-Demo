package top.jolyoulu.puback;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import top.jolyoulu.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/22 17:07
 * @Version 1.0
 * 发布确认速度测试
 */
public class ConfirmMsg {

    //批量发送的消息数量
    public static final int MSG_COUNT = 1000;

    public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {
        ConfirmMsg.synMsg();
        ConfirmMsg.batchMsg();
        ConfirmMsg.aSynMsg();
    }

    //同步消息发送
    public static void synMsg() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        //队列名称声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认模式
        channel.confirmSelect();
        //记录开始时间
        long being = System.currentTimeMillis();
        //批量发送消息
        for (int i = 0; i < MSG_COUNT; i++) {
            channel.basicPublish("",
                    queueName,
                    null,
                    (i+"").getBytes());
            //等后确认
            boolean flag = channel.waitForConfirms();
            if (flag){
                //System.out.println("消息发送成功");
            }
        }
        //记录结束时间
        long end = System.currentTimeMillis();
        System.out.println("同步消息发送耗时 = "+(end - being)+"ms");
    }

    //批量消息发送
    public static void batchMsg() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        //队列名称声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认模式
        channel.confirmSelect();
        //记录开始时间
        long being = System.currentTimeMillis();
        //批量确认消息的大小，每100条确认一次
        int batchSize = 100;
        //批量发送消息
        for (int i = 0; i < MSG_COUNT; i++) {
            channel.basicPublish("",
                    queueName,
                    null,
                    (i+"").getBytes());
            //取模每100条数据确认一次
            if (i % batchSize == 0){
                //等后确认
                boolean flag = channel.waitForConfirms();
                if (flag){
                    //System.out.println("消息发送成功");
                }
            }
        }
        //记录结束时间
        long end = System.currentTimeMillis();
        System.out.println("同步消息发送耗时 = "+(end - being)+"ms");
    }

    //异步消息发送
    public static void aSynMsg() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        //队列名称声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认模式
        channel.confirmSelect();
        //消息确认成功回调函数
        ConfirmCallback ackCallback = ( deliveryTag,  multiple) ->{
            //System.out.println("已确认的消息");
        };
        //消息确认失败回调函数
        ConfirmCallback nackCallback = ( deliveryTag,  multiple) ->{
            //System.out.println("未确认的消息");
        };
        //消息监听器，监听那些消息成功那些消息失败
        channel.addConfirmListener(ackCallback,nackCallback);
        //记录开始时间
        long being = System.currentTimeMillis();
        //批量发送消息
        for (int i = 0; i < MSG_COUNT; i++) {
            channel.basicPublish("",
                    queueName,
                    null,
                    (i+"").getBytes());
        }
        //记录结束时间
        long end = System.currentTimeMillis();
        System.out.println("异步消息发送耗时 = "+(end - being)+"ms");
    }
}
