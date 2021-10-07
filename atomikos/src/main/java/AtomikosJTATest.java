import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import sun.security.provider.MD5;
import sun.security.rsa.RSASignature;

import javax.transaction.SystemException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/3 13:27
 * @Version 1.0
 */
public class AtomikosJTATest {

    private static AtomikosDataSourceBean creatAtomikosDataSourceBean(String dbName){
        Properties p = new Properties();
        p.setProperty("url","jdbc:mysql://localhost:3306/"+dbName+"?serverTimezone=GMT%2B8");
        p.setProperty("user","root");
        p.setProperty("password","123456");

        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName(dbName);
        ds.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
        ds.setXaProperties(p);
        return ds;
    }

    public static void main(String[] args) {
        AtomikosDataSourceBean ds1 = creatAtomikosDataSourceBean("demo");
        AtomikosDataSourceBean ds2 = creatAtomikosDataSourceBean("course_db");

        Connection conn1 = null;
        Connection conn2 = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        //构建一个atomikos事务模板
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        try {
            //开启事务
            userTransactionImp.begin();

            //更新一个库
            conn1 = ds1.getConnection();
            ps1 = conn1.prepareStatement("INSERT INTO demo.users (username, password) VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1,"test");
            ps1.setString(2,"123456");
            ps1.executeUpdate();

            //操作另外一个库
            conn2 = ds2.getConnection();
            ps2 = conn2.prepareStatement("INSERT INTO course_db.course_1 (cid, cname, user_id, cstatus) VALUES (?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps2.setLong(1, System.currentTimeMillis());
            ps2.setString(2,"java");
            ps2.setString(3,"123456");
            ps2.setString(4,"Normal");
            ps2.executeUpdate();

            //提交事务
            userTransactionImp.commit();
        }catch (Exception e){
            e.printStackTrace();
            try {
                //异常回滚
                userTransactionImp.rollback();
            } catch (SystemException systemException) {
                systemException.printStackTrace();
            }
        }
    }

}
