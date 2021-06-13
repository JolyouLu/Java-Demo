package top.jolyoulu;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/13 17:35
 * @Version 1.0
 */
public class TestShiroMD5 {
    public static void main(String[] args) {

        System.out.println("使用md5加密，未加盐===============================");
        Md5Hash md5Hash1 = new Md5Hash("123");
        //对加密后的结果转化16进制打印
        System.out.println(md5Hash1.toHex());

        System.out.println("使用md5加密+加盐(jolyoulu)=======================");
        Md5Hash md5Hash2 = new Md5Hash("123","jolyoulu");
        //对加密后的结果转化16进制打印
        System.out.println(md5Hash2.toHex());


        System.out.println("使用md5加密+加盐(jolyoulu)+hash散列=======================");
        Md5Hash md5Hash3 = new Md5Hash("123","jolyoulu",1024);
        //对加密后的结果转化16进制打印
        System.out.println(md5Hash3.toHex());
    }
}
