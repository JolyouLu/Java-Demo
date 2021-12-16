package top.jolyoulu.desaes;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @Author: JolyouLu
 * @Date: 2021/12/11 16:34
 * @Version 1.0
 * 对称加密
 */
public class AesDemo {
    public static void main(String[] args) throws Exception{
        //原文
        String input = "Hello World";
        //定义密钥（使用AES加密，密钥必须是16字节）
        String key = "1234567812345678";
        System.out.println("<======加密======>");
        String encryptAES = encryptAES(input, key);
        System.out.println("<======解密======>");
        String decryptAES = decryptAES(encryptAES, key);
    }

    /**
     * AES 加密算法
     * @param input 原文
     * @param key 密钥（必须8个字节）
     * @return
     * @throws Exception
     */
    private static String encryptAES(String input, String key) throws Exception {
        System.out.println("原文："+input);
        Cipher cipher = Cipher.getInstance("AES");
        //创建加密规则 参数1：密钥的字节 参数2：加密类型
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        //进行加密初始化 参数1：选择模式，加密模式/解密模式 参数2：加密规则
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        //传入原文字节数值，调用加密方法
        byte[] bytes = cipher.doFinal(input.getBytes());
        //打印密文,直接打印是乱码的，需要使用Base64打印
        System.out.println("密文："+new String(bytes));
        String str = new String(Base64.getEncoder().encode(bytes));
        System.out.println("Base64转码："+str);
        return str;
    }

    /**
     * AES 解密算法
     * @param input 密文
     * @param key 密钥（必须8个字节）
     * @return
     * @throws Exception
     */
    private static String decryptAES(String input, String key) throws Exception {
        System.out.println("密文："+input);
        byte[] deBase64 = Base64.getDecoder().decode(input.getBytes());
        Cipher cipher = Cipher.getInstance("AES");
        //创建解密规则 参数1：密钥的字节 参数2：加密类型
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        //进行解密初始化 参数1：选择模式，加密模式/解密模式 参数2：加密规则
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        //传入密文字节数值，调用解密方法
        byte[] bytes = cipher.doFinal(deBase64);
        //打印原文
        System.out.println("原文："+new String(bytes));
        return new String(bytes);
    }
}
