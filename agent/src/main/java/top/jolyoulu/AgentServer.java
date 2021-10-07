package top.jolyoulu;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/4 20:56
 * @Version 1.0
 */
public class AgentServer {
    public Integer sayHello(String name,String msg){
        System.out.println("sayHello!!!");
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void sayHello1(String name,String msg){
        System.out.println("sayHello1!!!");
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String append(String name,String msg){
        return name + msg;
    }

    public Object getInt(){
        return 1;
    }
}
