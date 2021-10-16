package top.jolyoulu.dubbo;


import com.alibaba.dubbo.rpc.RpcContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import top.jolyoulu.service.User;
import top.jolyoulu.service.UserService;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/16 16:27
 * @Version 1.0
 */
public class Consumer {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-consumer.xml");
        context.start();
        UserService userService = context.getBean(UserService.class);

        boolean flag = true;
        while (flag){
            System.out.println("请输入需要查询的Id，输入exit表示结束程序");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            switch (s){
                case "exit":
                    flag = false;
                    break;
                default:
                    User user = userService.getUser(s);
                    System.out.println("查询结果 => "+user);
                    break;
            }
        }
        System.out.println("程序结束~");
    }
}
