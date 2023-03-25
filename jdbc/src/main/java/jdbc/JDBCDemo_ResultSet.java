package jdbc;

import com.mysql.cj.jdbc.Driver;
import org.junit.jupiter.api.Test;
import pojo.SysUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/25 11:56
 * @Description
 */

public class JDBCDemo_ResultSet {

    /**
     * 执行DQL语句
     * @throws Exception
     */
    @Test
    public void testResultSet() throws Exception{
        //注册驱动
        DriverManager.registerDriver(new Driver());
        String url = "jdbc:mysql://localhost:3306/jl";
        String username = "root";
        String password = "123456";
        //获取连接
        Connection conn = DriverManager.getConnection(url, username, password);
        //定义sql
        String sql = "SELECT * FROM sys_user";
        //获取执行sql的对象
        Statement stmt = conn.createStatement();
        //执行sql
        ResultSet rs = stmt.executeQuery(sql);
        //处理结果，遍历rs中所有数据(方法1)
        /*while (rs.next()){ //光标向下移动一行获取数据
            int id = rs.getInt(1);//获取第1列 类型是int
            String account = rs.getString(2);//获取第2列 类型是String
            String nickname = rs.getString(3);//获取第3列 类型是String
            System.out.println(id);
            System.out.println(account);
            System.out.println(nickname);
            System.out.println("==========================");
        }*/
        //处理结果，遍历rs中所有数据(方法2)
        while (rs.next()){ //光标向下移动一行获取数据
            int id = rs.getInt("id");//获取id列 类型是int
            String account = rs.getString("account");//获取account列 类型是String
            String nickname = rs.getString("nickname");//获取nickname列 类型是String
            System.out.println(id);
            System.out.println(account);
            System.out.println(nickname);
            System.out.println("==========================");
        }
        //释放资源
        rs.close();
        stmt.close();
        conn.close();
    }

    /**
     * 执行DQL语句（将数据封装到对象中）
     * @throws Exception
     */
    @Test
    public void testResultSetToPojo() throws Exception{
        //注册驱动
        DriverManager.registerDriver(new Driver());
        String url = "jdbc:mysql://localhost:3306/jl";
        String username = "root";
        String password = "123456";
        //获取连接
        Connection conn = DriverManager.getConnection(url, username, password);
        //定义sql
        String sql = "SELECT * FROM sys_user";
        //获取执行sql的对象
        Statement stmt = conn.createStatement();
        //执行sql
        ResultSet rs = stmt.executeQuery(sql);
        //处理结果，遍历rs中所有数据(将数据封装到对象中)
        List<SysUser> list = new ArrayList<>();
        while (rs.next()){ //光标向下移动一行获取数据
            SysUser sysUser = new SysUser();
            int id = rs.getInt(1);//获取第1列 类型是int
            String account = rs.getString(2);//获取第2列 类型是String
            String nickname = rs.getString(3);//获取第3列 类型是String
            sysUser.setId(id);
            sysUser.setAccount(account);
            sysUser.setNickname(nickname);
            list.add(sysUser);
        }
        System.out.println(list);
        //释放资源
        rs.close();
        stmt.close();
        conn.close();
    }

}
