package top.jolyoulu;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Author: JolyouLu a
 * @Date: 2021/7/3 16:46
 * @Version 1.0
 */
public class SpringSimpleServer {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-provide.xml");
        context.start();
        System.in.read();
    }
}
