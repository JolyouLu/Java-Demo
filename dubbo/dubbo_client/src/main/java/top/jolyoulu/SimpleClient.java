package top.jolyoulu;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import top.jolyoulu.Bean.User;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/3 14:20
 * @Version 1.0
 */
public class SimpleClient {

    public UserService buildRemoteService(String remoteUrl){
        //配置一个服务消费者
        ReferenceConfig<UserService> referenceConfig = new ReferenceConfig<>();
        //设置服务接口
        referenceConfig.setInterface(UserService.class);
        //设置服务服务的url
        referenceConfig.setUrl(remoteUrl);
        //设置应用信息
        referenceConfig.setApplication(new ApplicationConfig("simple-client"));
        //获取一个服务并且返回
        return referenceConfig.get();
    }

    public static void main(String[] args) {
        SimpleClient client = new SimpleClient();
        //直接通过url直接去访问服务，得到UserService
        UserService userService = client.buildRemoteService("dubbo://127.0.0.1:20880/top.jolyoulu.UserService");
        User user = userService.getUser("123");
        System.out.println("获取到user信息："+user);
    }
}
