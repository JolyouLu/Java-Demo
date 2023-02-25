package one.layout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 15:11
 * @Description 卡片布局：
 */
public class CardLayoutDemo {
    public static void main(String[] args) {
        Frame frame = new Frame("这里测试CardLayout");
        //创建一个Panel，用来存储多张卡片
        Panel p1 = new Panel();
        //创建CardLayout对象，把该对象设置给之前创建的容器
        CardLayout cardLayout = new CardLayout();
        p1.setLayout(cardLayout);
        //往panel中存储多个卡组件
        String[] names = {"第一张", "第二张", "第三张", "第四张", "第五张"};
        for (int i = 0; i < names.length; i++) {
            p1.add(names[i], new Button(names[i]));
        }
        //把panel放搭配窗口中显示
        frame.add(p1);
        //创建一个Panel，用存储多个按钮组件
        Panel p2 = new Panel();
        //创建5个按钮组件
        Button b1 = new Button("上一张");
        Button b2 = new Button("下一张");
        Button b3 = new Button("第一张");
        Button b4 = new Button("最后一张");
        Button b5 = new Button("第三张");
        //创建一个事件监听，监听按钮点击动作|
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand(); //获取按钮上的文字
                switch (actionCommand) {
                    case "上一张":
                        cardLayout.previous(p1);
                        break;
                    case "下一张":
                        cardLayout.next(p1);
                        break;
                    case "第一张":
                        cardLayout.first(p1);
                        break;
                    case "最后一张":
                        cardLayout.last(p1);
                        break;
                    case "第三张":
                        cardLayout.show(p1, "第三张");
                        break;
                }
            }
        };
        //把当前事件监听器和多个按钮绑定
        b1.addActionListener(listener);
        b2.addActionListener(listener);
        b3.addActionListener(listener);
        b4.addActionListener(listener);
        b5.addActionListener(listener);
        //把按钮添加到容器p2中
        p2.add(b1);
        p2.add(b2);
        p2.add(b3);
        p2.add(b4);
        p2.add(b5);
        //把panel放到frame的南边
        frame.add(p2,BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}
