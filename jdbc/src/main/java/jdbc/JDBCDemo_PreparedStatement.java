package jdbc;

import com.mysql.cj.jdbc.Driver;
import org.junit.jupiter.api.Test;
import pojo.SysUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/25 11:56
 * @Description
 */

public class JDBCDemo_PreparedStatement {

    /**
     * 执行DQL语句
     * @throws Exception
     */
    @Test
    public void testResultSet() throws Exception{
        //注册驱动
        DriverManager.registerDriver(new Driver());
        //useServerPrepStmts=true 开启预编译
        String url = "jdbc:mysql://localhost:3306/jl";
        String username = "root";
        String password = "123456";
        //获取连接
        Connection conn = DriverManager.getConnection(url, username, password);
        //定义sql
        String sql = "SELECT * FROM sys_user where id = ?";
        //获取执行sql的对象
        PreparedStatement pstmt = conn.prepareStatement(sql);
        //传入参数
        pstmt.setInt(1,1);
        //执行sql
        ResultSet rs = pstmt.executeQuery();
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
        pstmt.close();
        conn.close();
    }

}
