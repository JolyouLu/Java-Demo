package top.jolyoulu;

import com.alibaba.dubbo.rpc.RpcContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import top.jolyoulu.Bean.User;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/3 16:46
 * @Version 1.0
 */
public class SpringSimpleClient {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-consumer.xml");
        context.start();
        UserService userService = context.getBean(UserService.class);
        long begin = System.currentTimeMillis();
        User user1 = userService.getUser("123");
        Future<User> f1 = RpcContext.getContext().getFuture();
        User user2 = userService.getUser("123");
        Future<User> f2 = RpcContext.getContext().getFuture();
        User user3 = userService.getUser("123");
        Future<User> f3 = RpcContext.getContext().getFuture();

        user1 = f1.get();
        user2 = f2.get();
        user3 = f3.get();

        System.out.println("执行时间："+ (System.currentTimeMillis() - begin) + "ms");
        System.in.read();
    }
}
