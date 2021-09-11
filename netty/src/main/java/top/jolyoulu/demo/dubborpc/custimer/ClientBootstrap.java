package top.jolyoulu.demo.dubborpc.custimer;

import top.jolyoulu.demo.dubborpc.netty.NettyClient;
import top.jolyoulu.demo.dubborpc.publicinterface.HelloService;

/**
 * @Author: JolyouLu
 * @Date: 2020/11/29 13:43
 * @Version 1.0
 */
public class ClientBootstrap {

    //定义协议头
    public static final String providerName="HelloService#hello#";

    public static void main(String[] args) {
        //创建一个消费者
        NettyClient customer = new NettyClient();
        //创建代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class, providerName);
        //通过代理对象调用服务提供者的方法
//        for (int i = 0; i < 10; i++) {
            String res = service.hello("你好 dubbo~~");
            System.out.println("调用的结果 res="+res);
//        }

    }
}
