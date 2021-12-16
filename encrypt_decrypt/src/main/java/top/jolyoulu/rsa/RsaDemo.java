package top.jolyoulu.rsa;


import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @Author: JolyouLu
 * @Date: 2021/12/15 16:52
 * @Version 1.0
 */
public class RsaDemo {
    private static final String ALGORITHM = "RSA";
    private static final String CHARSET = "UTF-8";

    public static void main(String[] args) throws Exception {
        String input = "Hello World";
        creatKeyPair("pubKey","priKey");
        PrivateKey privateKey = getPrivateKey("priKey");
        PublicKey publicKey = getPublicKey("pubKey");
        // 使用私钥对原文加密============================================
        String privateKeyEncoder = encrypt(privateKey, input);
        // 使用公钥对原文解密============================================
        String publicKeyDecoder = decrypt(publicKey, privateKeyEncoder);
    }

    /**
     * 生成密钥对
     * @param pubPath 公钥生成路径
     * @param priPath 私钥生成路径
     * @return KeyPair
     */
    public static KeyPair creatKeyPair(String pubPath,String priPath) throws Exception{
        // 创建RSA算法的密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //生成私钥
        PrivateKey privateKey = keyPair.getPrivate();
        //生成公钥
        PublicKey publicKey = keyPair.getPublic();
        //获取私钥的字节数组
        byte[] privateKeyEncoded = privateKey.getEncoded();
        String privateKeyBase64 = new String(Base64.getEncoder().encode(privateKeyEncoded));
        System.out.println("私钥(Base64)："+ privateKeyBase64);
        //获取公钥的字节数组
        byte[] publicKeyEncoded = publicKey.getEncoded();
        String publicKeyBase64 = new String(Base64.getEncoder().encode(publicKeyEncoded));
        System.out.println("公钥(Base64)："+ publicKeyBase64);
        //把公钥保存到根目录
        FileUtils.writeStringToFile(new File(pubPath),publicKeyBase64, Charset.forName(CHARSET));
        FileUtils.writeStringToFile(new File(priPath),privateKeyBase64, Charset.forName(CHARSET));
        //把私钥保存到根目录
        return keyPair;
    }

    /**
     * 从文件中读取私钥
     * @param priPath 私钥绝对路径
     * @return PrivateKey
     */
    public static PrivateKey getPrivateKey(String priPath) throws Exception{
        String privateKeyString = FileUtils.readFileToString(new File(priPath), Charset.forName(CHARSET));
        //创建key工厂
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        //创建私钥key的规则
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 从文件中读取公钥
     * @param pubPath 公钥绝对路径
     * @return PublicKey
     */
    public static PublicKey getPublicKey(String pubPath) throws Exception{
        String publicKeyString = FileUtils.readFileToString(new File(pubPath), Charset.forName(CHARSET));
        //创建key工厂
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        //创建公钥key的规则
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 使用密钥对原文加密
     *  1.私钥加密，公钥解密
     *  2.公钥加密，私钥解密
     * @param key 密钥
     * @param input 原文
     * @throws Exception
     */
    public static String encrypt(Key key,String input) throws Exception{
        //创建Cipher加密对象
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        //加密进行初始化 参数1：加密模式 参数2：使用公钥加密还是私钥加密
        cipher.init(Cipher.ENCRYPT_MODE,key);
        //使用私钥进行加密
        byte[] bytes = cipher.doFinal(input.getBytes());
        String privateKeyInput = new String(Base64.getEncoder().encode(bytes));
        System.out.println("使用密钥加密后的密文(Base64)："+ privateKeyInput);
        return privateKeyInput;
    }

    /**
     * 使用密钥对原文解密密
     *  1.私钥加密，公钥解密
     *  2.公钥加密，私钥解密
     * @param key 密钥
     * @param encryptStr 密文
     * @throws Exception
     */
    public static String decrypt(Key key,String encryptStr) throws Exception{
        byte[] decode = Base64.getDecoder().decode(encryptStr);
        //创建Cipher加密对象
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        //加密进行初始化 参数1：加密模式 参数2：使用公钥加密还是私钥加密
        cipher.init(Cipher.DECRYPT_MODE,key);
        //使用私钥进行加密
        byte[] bytes = cipher.doFinal(decode);
        String input = new String(bytes);
        System.out.println("使用密钥解密后的原文："+ input);
        return input;
    }
}
