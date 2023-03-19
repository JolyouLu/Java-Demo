package top.jolyoulu.mybatis;


import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.session.*;
import top.jolyoulu.mybatis.entity.SysUser;
import top.jolyoulu.mybatis.mapper.SysUserMapper;
import top.jolyoulu.mybatis.plugin.page.JlPage;
import top.jolyoulu.mybatis.plugin.page.JlPageLocal;
import top.jolyoulu.mybatis.plugin.page.PageQueryPlugin;
import top.jolyoulu.mybatis.plugin.sqllog.SqlLogPlugin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 12:23
 * @Description
 */
public class MybatisTest {
    public static void main(String[] args) {
        //读取Mybatis配置文件
        InputStream inputStream = MybatisTest.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
        //构建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //添加自定义插件
        Configuration configuration = sqlSessionFactory.getConfiguration();
//        configuration.addInterceptor(new DataMaskPlugin());
        configuration.addInterceptor(new PageQueryPlugin());
        configuration.addInterceptor(new SqlLogPlugin());
        configuration.setLogImpl(NoLoggingImpl.class);
        //打开session
        SqlSession session = sqlSessionFactory.openSession();
        //获取Mapper对象
        SysUserMapper userMapper = session.getMapper(SysUserMapper.class);
        //调用Mapper接口对象方法操作数据库
        JlPage<SysUser> execute = JlPage.execute(
                SysUser.class, 1, 10,
                () -> {
                    List<SysUser> list = userMapper.listById("2b07e91a097349d3208d77a1");
                    return list;
                },true);
        System.out.println(execute);
    }
}
