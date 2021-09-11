package top.jolyoulu.demo.dubborpc.provider;

import top.jolyoulu.demo.dubborpc.publicinterface.HelloService;

/**
 * @Author: JolyouLu
 * @Date: 2020/11/29 12:48
 * @Version 1.0
 */
public class HelloServiceImp implements HelloService {

    private static int count = 0;

    //当有消费方调用该方法时就返回一个结果
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息="+msg);
        //根据msg返回不同的结果
        if (msg != null){
            return "你好客户端，我已经收到你的消息["+msg+"] 第"+(++count)+"次";
        }else {
            return "你好客户端，我已经收到你的消息 ";
        }
    }
}
