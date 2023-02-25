package one.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 16:51
 * @Description
 */
public class ListenerDemo2 {

    public static void main(String[] args) {
        Frame frame = new Frame("这里测试监听器");
        frame.setBounds(200,200,500,300);
        //监听用户点击关闭窗口动作
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //退出
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

}
