package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/25 11:56
 * @Description
 */
public class JDBCDemo {
    public static void main(String[] args) throws Exception {
        //注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/jl";
        String username = "root";
        String password = "123456";
        //获取连接
        Connection conn = DriverManager.getConnection(url, username, password);
        //定义sql
        String sql = "UPDATE sys_user SET nickname = '张三2' WHERE id = '1'";
        //获取执行sql的对象
        Statement stmt = conn.createStatement();
        //执行sql
        int count = stmt.executeUpdate(sql);
        System.out.println("受影响的行数："+count);
        //释放资源
        stmt.close();
        conn.close();
    }
}
