package jdbc;

import com.mysql.cj.jdbc.Driver;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/25 11:56
 * @Description
 */

public class JDBCDemo_Statement {

    /**
     * 执行MDL语句
     * @throws Exception
     */
    @Test
    public void testDML() throws Exception{
        //注册驱动
        DriverManager.registerDriver(new Driver());
        String url = "jdbc:mysql://localhost:3306/jl";
        String username = "root";
        String password = "123456";
        //获取连接
        Connection conn = DriverManager.getConnection(url, username, password);
        //定义sql
        String sql = "UPDATE sys_user SET nickname = '张三1' WHERE id = '1'";
        //获取执行sql的对象
        Statement stmt = conn.createStatement();
        //执行sql
        int count = stmt.executeUpdate(sql);
        if (count > 0){
            System.out.println("修改成功");
        }else {
            System.out.println("修改失败");
        }
        //释放资源
        stmt.close();
        conn.close();
    }

    /**
     * 执行DDL语句
     * @throws Exception
     */
    @Test
    public void testDDL() throws Exception{
        //注册驱动
        DriverManager.registerDriver(new Driver());
        String url = "jdbc:mysql://localhost:3306/jl";
        String username = "root";
        String password = "123456";
        //获取连接
        Connection conn = DriverManager.getConnection(url, username, password);
        //定义sql
        String sql = "create table table1(id int null)";
        //获取执行sql的对象
        Statement stmt = conn.createStatement();
        //执行sql(DDL执行成功也可能返回0)
        int count = stmt.executeUpdate(sql);
        //释放资源
        stmt.close();
        conn.close();
    }

}
