package top.jolyoulu.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/16 16:27
 * @Version 1.0
 */
public class Provider {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-provide.xml");
        context.start();
        System.in.read();
    }
}
