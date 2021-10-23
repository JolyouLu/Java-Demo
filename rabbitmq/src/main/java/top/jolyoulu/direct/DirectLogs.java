package top.jolyoulu.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import top.jolyoulu.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/23 10:35
 * @Version 1.0
 */
public class DirectLogs {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        boolean flag = true;
        while (flag){
            Scanner scanner = new Scanner(System.in);
            System.out.println("请选择发送消息类型 1.info 2.warning 3.error 4.退出");
            switch (scanner.nextLine()){
                case "1":
                    System.out.println("请输入发送内容");
                    channel.basicPublish(EXCHANGE_NAME,"info",null,scanner.nextLine().getBytes());
                    System.out.println("生产者消息发送成功");
                    break;
                case "2":
                    System.out.println("请输入发送内容");
                    channel.basicPublish(EXCHANGE_NAME,"warning",null,scanner.nextLine().getBytes());
                    System.out.println("生产者消息发送成功");
                    break;
                case "3":
                    System.out.println("请输入发送内容");
                    channel.basicPublish(EXCHANGE_NAME,"error",null,scanner.nextLine().getBytes());
                    System.out.println("生产者消息发送成功");
                    break;
                case "4":
                    flag = false;
                    break;
            }
        }
        System.out.println("程序退出~");
    }

}
