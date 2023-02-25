package one.container;

import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 13:57
 * @Description
 */
public class WindowDemo {
    public static void main(String[] args) {
        //创建一个窗口对象
        Frame frame = new Frame("这是一个测试窗口");
        //设置窗口显示位置，显示大小
        frame.setLocation(100,100);
        frame.setSize(500,300);
        //设置窗口显示
        frame.setVisible(true);
    }
}
