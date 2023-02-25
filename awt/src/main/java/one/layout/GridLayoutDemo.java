package one.layout;

import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 14:54
 * @Description
 * GridLayout网格布局：将窗口拆分成指定列表格
 */
public class GridLayoutDemo {
    public static void main(String[] args) {
        Frame frame = new Frame("这里测试GridLayout");
        //创建一个panel对象，里面存放一个textfiled组件
        Panel p1 = new Panel();
        p1.add(new TextField(30));
        //把当前的panel添加到frame的北面
        frame.add(p1,BorderLayout.NORTH);
        //创建一个panel对象，并且设置布局为GridLayout
        Panel p2 = new Panel();
        p2.setLayout(new GridLayout(3,5,4,4));
        String[] but = {"0","1","2","3","4","5","6","7","8","9","+","-","*","/","."};
        for (String s : but) {
            p2.add(new Button(s));
        }
        //往panel中添加内容
        frame.add(p2,BorderLayout.SOUTH);
        //把panel添加到frame中
        frame.pack();
        frame.setVisible(true);
    }
}
