package jdbc;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/25 11:56
 * @Description
 */
public class JDBCDemo_Connenction {
    public static void main(String[] args) throws Exception {
        //注册驱动
        DriverManager.registerDriver(new Driver());
        String url = "jdbc:mysql://localhost:3306/jl";
        String username = "root";
        String password = "123456";
        //获取连接
        Connection conn = DriverManager.getConnection(url, username, password);
        //定义sql
        String sql1 = "UPDATE sys_user SET nickname = '张三6' WHERE id = '1'";
        String sql2 = "UPDATE sys_user SET nickname = '李四6' WHERE id = '2'";
        //获取执行sql的对象
        Statement stmt = conn.createStatement();
        try {
            //开启事务
            conn.setAutoCommit(false);
            //执行sql
            int count1 = stmt.executeUpdate(sql1);
            System.out.println("受影响的行数："+count1);
            //执行sql
            int count2 = stmt.executeUpdate(sql2);
            System.out.println("受影响的行数："+count2);
            //提交事务
            conn.commit();
        } catch (Exception e) {
            //回滚事务
            conn.rollback();
        }
        //释放资源
        stmt.close();
        conn.close();
    }
}
