package top.jolyoulu.rsa;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.sql.SQLOutput;
import java.util.Base64;

/**
 * @Author: JolyouLu
 * @Date: 2021/12/15 21:17
 * @Version 1.0
 */
public class SignatureDemo {
    private static final String ALGORITHM = "SHA256withRSA";
    private static final String CHARSET = "UTF-8";

    public static void main(String[] args) throws Exception {
        String input = "JolyouLu";
        //获取公钥与私钥
        PrivateKey privateKey = RsaDemo.getPrivateKey("priKey");
        PublicKey publicKey = RsaDemo.getPublicKey("pubKey");
        //生成数字签名
        String signatureData = getSignature(input, ALGORITHM, privateKey);
        System.out.println("签名："+signatureData);
        //校验签名
        boolean b = verifySignature(input,ALGORITHM,publicKey,signatureData);
        if (b){
            System.out.println("签名验证通过");
        }
    }


    /**
     * 生成数字签名
     * @param input 原文
     * @param algorithm 算法
     * @param privateKey 私钥
     * @return
     */
    public static String getSignature(String input,String algorithm,PrivateKey privateKey) throws Exception{
        //获取签名对象
        Signature signature = Signature.getInstance(algorithm);
        //初始化签名
        signature.initSign(privateKey);
        //传入原文
        signature.update(input.getBytes());
        //开始签名
        byte[] sign = signature.sign();
        //使用Base64编码
        return Base64.getEncoder().encodeToString(sign);
    }


    /**
     * 校验签名
     * @param input 原文
     * @param algorithm 算法
     * @param publicKey 公钥
     * @param signatureData 签名
     * @return
     */
    private static boolean verifySignature(String input, String algorithm, PublicKey publicKey, String signatureData) throws Exception {
        //获取签名对象
        Signature signature = Signature.getInstance(algorithm);
        //初始化签名
        signature.initVerify(publicKey);
        //传入原文
        signature.update(input.getBytes());
        //验证签名
        return signature.verify(Base64.getDecoder().decode(signatureData));
    }
}
