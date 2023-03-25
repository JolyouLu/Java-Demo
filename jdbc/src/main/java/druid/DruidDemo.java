package druid;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/25 14:16
 * @Description
 */
public class DruidDemo {
    public static void main(String[] args) throws Exception {
        //加载配置文件
        Properties prop = new Properties();
        prop.load(DruidDemo.class.getClassLoader().getResourceAsStream("druid.properties"));
        //获取连接对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
        //获取一个连接
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
