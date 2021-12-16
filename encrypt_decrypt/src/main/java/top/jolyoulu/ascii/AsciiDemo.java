package top.jolyoulu.ascii;

/**
 * @Author: JolyouLu
 * @Date: 2021/12/11 11:30
 * @Version 1.0
 * 了解Ascii码
 */
public class AsciiDemo {
    public static void main(String[] args) {
        System.out.println("=================================");
        //定义一个char变量，接受一个'A'
        char a = 'A';
        //将char转为int类型(Ascii转10进制)
        int b = a;
        //打印'A'，在10进制显示多少
        System.out.println(b);
        System.out.println("=================================");
        //定义字符串
        String a1 = "AaZ";
        //将字符串拆成字符
        char[] chars = a1.toCharArray();
        //循环打印每一个字符
        for (char aChar : chars) {
            int b1 = aChar;
            System.out.print(b1 + " ");
        }
        System.out.println();
    }
}
