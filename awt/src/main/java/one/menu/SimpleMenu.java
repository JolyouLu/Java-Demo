package one.menu;

import one.listener.EventDemo1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 12:01
 * @Description
 */
public class SimpleMenu {
    Frame frame = new Frame("这里测试菜单组件");
    //菜单条
    MenuBar menuBar = new MenuBar();
    //菜单组件
    Menu fileMenu = new Menu("文件");
    Menu editMenu = new Menu("编辑");
    Menu formatMenu = new Menu("格式");
    //菜单项组件
    MenuItem auto = new MenuItem("自动换行");
    MenuItem copy = new MenuItem("复制");
    MenuItem paste = new MenuItem("粘贴");
    //二级菜单
    MenuItem comment = new MenuItem("注释",new MenuShortcut(KeyEvent.VK_Q,true)); //关联快捷键
    MenuItem cancelComment = new MenuItem("取消注释");

    //文本框
    TextArea ta = new TextArea(6,40);

    public void init() {
        //组装视图
        comment.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ta.append("你点击了菜单项目："+e.getActionCommand());
            }
        });
        //子菜单
        formatMenu.add(comment);
        formatMenu.add(cancelComment);
        //组装菜单
        editMenu.add(auto);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(formatMenu);
        //菜单条
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        //将菜单条放入到frame中
        frame.setMenuBar(menuBar);
        frame.add(ta);
        //设置frame大小与可见
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleMenu().init();
    }
}
