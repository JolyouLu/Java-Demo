package top.jolyoulu;



/**
 * @Author: JolyouLu
 * @Date: 2021/10/4 20:29
 * @Version 1.0
 */
public class AgentDemoTest {

    public static void main(String[] args) {
        System.out.println("main");
        Integer r = new AgentServer().sayHello("张三", "你好");
        System.out.println(r);
    }

}