package top.jolyoulu;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import top.jolyoulu.impl.UserServiceImpl;

import java.io.IOException;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/3 14:08
 * @Version 1.0
 */
public class SimpleServer {

    public void openService(int port){
        //服务配置
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>();
        //设置服务接口
        serviceConfig.setInterface(UserService.class);
        //设置开放的协议
        serviceConfig.setProtocol(new ProtocolConfig("dubbo",port));
        //设置注册中心(空的注册中心)
        serviceConfig.setRegistry(new RegistryConfig(RegistryConfig.NO_AVAILABLE));
        //设置当前服务的应用
        serviceConfig.setApplication(new ApplicationConfig("simple-app"));
        //设置服务实现对象
        UserServiceImpl ref = new UserServiceImpl();
        serviceConfig.setRef(ref);
        //将服务暴露出去
        serviceConfig.export();
        //获取的的口号
        int p = serviceConfig.getExportedUrls().get(0).getPort();
        ref.setPort(p);
        System.out.println("服务已开启"+p);
    }

    public static void main(String[] args) throws IOException {
        new SimpleServer().openService(20880);
        System.in.read();
    }

}
