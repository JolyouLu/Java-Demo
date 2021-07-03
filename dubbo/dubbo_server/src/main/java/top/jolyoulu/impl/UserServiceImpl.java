package top.jolyoulu.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.jolyoulu.Bean.User;
import top.jolyoulu.UserService;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/3 14:09
 * @Version 1.0
 */
@Data
public class UserServiceImpl implements UserService {

    private int port;

    @Override
    public User getUser(String id) {
        System.out.println("UserServiceImpl被调用，服务的端口号："+port);
        return new User(id,"test","UserServiceImpl被调用，服务的端口号："+port);
    }
}
