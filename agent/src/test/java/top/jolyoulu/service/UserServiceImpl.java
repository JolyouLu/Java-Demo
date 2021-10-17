package top.jolyoulu.service;

import lombok.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        System.out.println("UserServiceImpl被调用，服务的端口号：" + port);
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/school?serverTimezone=GMT%2B8",
                    "root", "123456");
            PreparedStatement statement = conn.prepareStatement("select * from `student` where id = ?");
            statement.setString(1, id);
            ResultSet res = statement.executeQuery();
            User user = null;
            if (res.next()) {
                String userId = res.getString(1);
                String userName = res.getString(2);
                user = new User(userId, userName, "UserServiceImpl被调用，服务的端口号：" + port);
            }
            statement.close();
            conn.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
