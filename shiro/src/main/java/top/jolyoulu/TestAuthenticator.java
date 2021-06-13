package top.jolyoulu;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/13 13:09
 * @Version 1.0
 */
public class TestAuthenticator {
    public static void main(String[] args) {
        //创建安全管理器对象
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        //给安全管理器设置realms
        securityManager.setRealm(new IniRealm("classpath:shiro.ini"));
        //全局安全工具类SecurityUtils，提供认证、退出方法
        //给全局工具类设置安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        //认证关键对象subject主体
        Subject subject = SecurityUtils.getSubject();
        //创建令牌,并且设置身份信息与凭证信息
        UsernamePasswordToken token = new UsernamePasswordToken("test1","123");

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
    }
}
