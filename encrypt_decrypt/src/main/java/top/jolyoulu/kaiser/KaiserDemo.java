package top.jolyoulu.kaiser;

/**
 * @Author: JolyouLu
 * @Date: 2021/12/11 14:56
 * @Version 1.0
 * 恺撒加密算法
 */
public class KaiserDemo {
    public static void main(String[] args) {
        //定义原文
        String input = "Hello World";
        //定义偏移量
        Integer key = 5;
        System.out.println("<======加密======>");
        String kaiser = encryptKaiser(input,key);
        System.out.println("<======解密======>");
        decryptKaiser(kaiser,key);
    }

    //恺撒加密
    private static String encryptKaiser(String input,Integer key) {
        System.out.println("原文："+input);
        //获取字符串的字节数值
        char[] chars = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char aChar : chars) {
            int b = aChar;
            //将原文偏移3位
            b = b + key;
            char newB = (char) b;
            sb.append(newB);
        }
        System.out.println("密文："+sb.toString());
        return sb.toString();
    }

    //恺撒解密
    private static String decryptKaiser(String input,Integer key) {
        System.out.println("密文："+input);
        //获取字符串的字节数值
        char[] chars = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char aChar : chars) {
            int b = aChar;
            //将原文偏移3位
            b = b - key;
            char newB = (char) b;
            sb.append(newB);
        }
        System.out.println("原文："+sb.toString());
        return sb.toString();
    }
}
