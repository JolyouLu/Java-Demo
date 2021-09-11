package top.jolyoulu.demo.dubborpc.publicinterface;

/**
 * @Author: JolyouLu
 * @Date: 2020/11/29 12:46
 * @Version 1.0
 */
//服务提供方与服务消费方都需要的
public interface HelloService {

    String hello(String msg);
}
