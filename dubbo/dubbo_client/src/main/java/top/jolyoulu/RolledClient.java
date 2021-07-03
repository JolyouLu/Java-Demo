package top.jolyoulu;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import top.jolyoulu.Bean.User;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/3 14:20
 * @Version 1.0
 */
public class RolledClient {

    public UserService buildRemoteService(){
        //配置一个服务消费者
        ReferenceConfig<UserService> referenceConfig = new ReferenceConfig<>();
        //设置服务接口
        referenceConfig.setInterface(UserService.class);
        //设置注册中心(利用的是组网广播协议，只对局域网有效，必须与服务提供者的一致)
        referenceConfig.setRegistry(new RegistryConfig("multicast://224.5.6.7:1234"));
        //设置应用信息
        referenceConfig.setApplication(new ApplicationConfig("rolled-client"));
        //获取一个服务并且返回
        return referenceConfig.get();
    }

    public static void main(String[] args) throws InterruptedException {
        RolledClient client = new RolledClient();
        //直接通过url直接去访问服务，得到UserService
        UserService userService = client.buildRemoteService();
        //连续调用10次
        for (int i = 0; i < 10; i++) {
            User user = userService.getUser("123");
            System.out.println("获取到user信息："+user);
            Thread.sleep(1000);
        }
    }
}
