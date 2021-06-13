package top.jolyoulu.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/13 15:39
 * @Version 1.0
 * 自定义Realm 既然md5+盐+hash
 */
public class CustomerMd5Realm extends AuthorizingRealm {

    //模仿数据库中的表密码加密方式 md5加密+加盐(jolyoulu)+hash散列(1024)
    private static HashMap<String,String> userTable = new HashMap<>();
    static {
        userTable.put("test", "b56b894ba7666177af206a6a84f208f5");
    }


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //principals是用户的身份信息，就是用户名
        //根据用户名获取该用户拥有的角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //将数据库查询的角色信息赋值给simpleAuthorizationInfo
        simpleAuthorizationInfo.addRole("admin");
        simpleAuthorizationInfo.addRole("user");
        //将数据库查询的权限赋值给simpleAuthorizationInfo
        simpleAuthorizationInfo.addStringPermission("user:*:*");
        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //在token中获取用户名
        String principal = (String)token.getPrincipal();
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
                    ByteSource.Util.bytes("jolyoulu"), //返回加入的盐
                    this.getName());
            return simpleAuthenticationInfo;
        }
        return null;
    }


    //模仿mybatis查询接口
    private User getUserByUserName(String userName){
        if ("test".equals(userName)){
            return new User(userName,userTable.get(userName));
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
