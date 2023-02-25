package one.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 16:18
 * @Description
 */
public class DialogDemo1 {
    public static void main(String[] args) {
        Frame frame = new Frame("这里测试Dialog");

        //创建2个对话框Dialog对象，1个模式，一个非模式
        Dialog d1 = new Dialog(frame, "模式对话框", true);
        Dialog d2 = new Dialog(frame, "非模式对话框", false);
        //通过setBounds方法设置dialog的位置与大小
        d1.setBounds(20,30,300,200);
        d2.setBounds(20,30,300,200);
        //创建2个按钮并且
        Button b1 = new Button("打开模式对话框");
        Button b2 = new Button("打开非模式对话框");
        //给这2个按钮添加点击后的行为
        b1.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                d1.setVisible(true);
            }
        });
        b2.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                d2.setVisible(true);
            }
        });
        //把按钮添加到frme中
        frame.add(b1,BorderLayout.NORTH);
        frame.add(b2);

        frame.pack();
        frame.setVisible(true);
    }
}
