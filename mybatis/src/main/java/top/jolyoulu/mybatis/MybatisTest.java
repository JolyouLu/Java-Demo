package top.jolyoulu.mybatis;


import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.session.*;
import top.jolyoulu.mybatis.entity.SysUser;
import top.jolyoulu.mybatis.mapper.SysUserMapper;
import top.jolyoulu.mybatis.plugin.dataMask.DataMaskPlugin;
import top.jolyoulu.mybatis.plugin.page.JlPage;
import top.jolyoulu.mybatis.plugin.page.PageBO;
import top.jolyoulu.mybatis.plugin.page.PageQueryPlugin;
import top.jolyoulu.mybatis.plugin.sqllog.SqlLogPlugin;

import java.io.InputStream;
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
        configuration.addInterceptor(new PageQueryPlugin(true));
//        configuration.addInterceptor(new SqlLogPlugin());
        configuration.setLogImpl(NoLoggingImpl.class);
        //打开session
        SqlSession session = sqlSessionFactory.openSession();
        //获取Mapper对象
        SysUserMapper userMapper = session.getMapper(SysUserMapper.class);
        //调用Mapper接口对象方法操作数据库
        JlPage<SysUser> page = JlPage.getPage(SysUser.class, 1L, 10L);
        List<SysUser> list = userMapper.listById(page,"2b07e91a097349d3208d77a1");
        page.setList(list);
        System.out.println(page);
        System.out.println(list);
    }
}
