package top.jolyoulu.digest;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * @Author: JolyouLu
 * @Date: 2021/12/11 23:42
 * @Version 1.0
 * 消息摘要算法，用于防篡改
 */
public class DigestDemo {
    public static void main(String[] args) throws Exception{
        //原文
        String input = "Hello World";
        System.out.println("<=====MD5加密=====>");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] resMd5 = md5.digest(input.getBytes());
        //将密文转换16进制输出
        System.out.println("MD5："+toHexStr(resMd5));
        System.out.println("<=====SHA-1加密=====>");
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] resSha1 = sha1.digest(input.getBytes());
        //将密文转换16进制输出
        System.out.println("SHA-1："+toHexStr(resSha1));
        System.out.println("<=====SHA-256加密=====>");
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] resSha256 = sha256.digest(input.getBytes());
        //将密文转换16进制输出
        System.out.println("SHA-256："+toHexStr(resSha256));
        System.out.println("<=====SHA-512加密=====>");
        MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        byte[] resSha521 = sha512.digest(input.getBytes());
        //将密文转换16进制输出
        System.out.println("SHA-512："+toHexStr(resSha521));
    }

    private static String toHexStr(byte[] bytes){
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            String s = Integer.toHexString(aByte & 0xff);
            //判断如果密文长度是1，需要在高位补0
            if (s.length() == 1){
                s = "0" + s;
            }
            builder.append(s);
        }
        return builder.toString();
    }
}
