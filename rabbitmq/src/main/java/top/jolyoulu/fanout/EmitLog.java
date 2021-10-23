package top.jolyoulu.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import top.jolyoulu.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/23 10:35
 * @Version 1.0
 */
public class EmitLog {

    private static final String EXCHANGE_NAME = "log";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String msg = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());
            System.out.println("生产者消息发送成功");
        }
    }

}
