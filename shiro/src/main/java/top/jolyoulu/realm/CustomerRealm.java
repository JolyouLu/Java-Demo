package top.jolyoulu.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/13 15:39
 * @Version 1.0
 * 自定义Realm 将来认证/授权数据转为使用数据库实现
 */
public class CustomerRealm extends AuthorizingRealm {

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //在token中获取用户名
        String principal = (String)token.getPrincipal();
        System.out.println("doGetAuthenticationInfo 获取到用户名："+principal);
        //根据身份信息使用mybatis查询相关数据库
        //这里是假的模拟调用是mybatis的查询方法
        User user = this.getUserByUserName(principal);
        if (user != null){
            //参数1：数据库用户名
            //参数2：数据密码
            //参数3：当前Realm名称
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                    user.getUsername(),
                    user.getPassword(),
                    this.getName());
            return simpleAuthenticationInfo;
        }
        return null;
    }


    //模仿mybatis查询接口
    private User getUserByUserName(String userName){
        if ("test".equals(userName)){
            return new User("test", "123");
        }
        return null;
    }

    //数据库表对象
    static class User{
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
