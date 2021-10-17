package top.jolyoulu;

import java.sql.*;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/17 12:21
 * @Version 1.0
 */
public class JdbcAgentTest {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/school?serverTimezone=GMT%2B8",
                "root", "123456");
        PreparedStatement statement = conn.prepareStatement("select * from `student`");
        ResultSet res = statement.executeQuery();
        while (res.next()) {
            System.out.println(res.getString(1) + "     " + res.getString(2));
        }
        statement.close();
        conn.close();
    }
}
