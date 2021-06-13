package top.jolyoulu;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import top.jolyoulu.realm.CustomerMd5Realm;
import top.jolyoulu.realm.CustomerRealm;

import java.util.Arrays;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/13 13:09
 * @Version 1.0
 */
public class TestCustomerMd5RealmAuthenticator {
    public static void main(String[] args) {
        //创建安全管理器对象
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        //获取realms
        CustomerMd5Realm realm = new CustomerMd5Realm();
        //获取HashedCredentialsMatcher
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //并且设置使用md5加密
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //设置散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        //给realm传入验证器
        realm.setCredentialsMatcher(hashedCredentialsMatcher);
        //给安全管理器设置realms
        securityManager.setRealm(realm);

        //全局安全工具类SecurityUtils，提供认证、退出方法
        //给全局工具类设置安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        //认证关键对象subject主体
        Subject subject = SecurityUtils.getSubject();
        //创建令牌,并且设置身份信息与凭证信息
        UsernamePasswordToken token = new UsernamePasswordToken("test","123");

        try {
            System.out.println("认证前，认证状态："+subject.isAuthenticated());
            subject.login(token);//用户认证
            System.out.println("认证后，认证状态："+subject.isAuthenticated());

        //认证失败时会抛异常
        }catch (UnknownAccountException e){
            System.out.println("认证失败：用户名不存在");
        }catch (IncorrectCredentialsException e){
            System.out.println("认证失败：密码错误");
        }catch (Exception e){
            e.printStackTrace();
        }

        //如果用户认证通过了，对用户进行授权
        if (subject.isAuthenticated()){
            //1、基于角色的访问控制
            System.out.println("当前主体是否有admin角色："+subject.hasRole("admin"));
            System.out.println("当前主体是否同时具有admin与user角色："+subject.hasAllRoles(Arrays.asList("admin","user")));
            //2、基于权限字符串的访问控制
            System.out.println("当前主体是否有user:*:*操作权限："+subject.isPermitted("user:*:*"));
            System.out.println("当前主体是否有user:update:*操作权限："+subject.isPermitted("user:update:*")); //使用了*通配符所以会包含所有权限
        }
    }
}
