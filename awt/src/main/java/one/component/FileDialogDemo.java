package one.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 16:32
 * @Description
 */
public class FileDialogDemo{
    public static void main(String[] args) {
        Frame frame = new Frame("这里测试Dialog");

        //创建对话框2个FileDialog对象
        FileDialog f1 = new FileDialog(frame, "选择要打开的文件", FileDialog.LOAD);
        FileDialog f2 = new FileDialog(frame, "选择要保存成的路径", FileDialog.SAVE);
        //创建2个按钮
        Button b1 = new Button("打开文件");
        Button b2 = new Button("保存文件");
        //给2个按钮设置点击后行为，获取打开或保存的路径文件名
        b1.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f1.setVisible(true); //阻塞等待选择
                String directory = f1.getDirectory();
                String file = f1.getFile();
                System.out.println("打开的文件路径："+directory);
                System.out.println("打开的文件名称："+file);
            }
        });
        b2.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f2.setVisible(true); //阻塞等待选择
                String directory = f2.getDirectory();
                String file = f2.getFile();
                System.out.println("保存的文件路径："+directory);
                System.out.println("保存的文件名称："+file);
            }
        });
        //把按钮添加到Frame
        frame.add(b1,BorderLayout.NORTH);
        frame.add(b2);

        frame.pack();
        frame.setVisible(true);
    }
}
