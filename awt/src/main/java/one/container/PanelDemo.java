package one.container;

import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 14:01
 * @Description
 */
public class PanelDemo {
    public static void main(String[] args) {
        //创建一个window对象
        Frame frame = new Frame("演示Panel");
        //创建panel对象
        Panel p = new Panel();
        //创建一个文本宽，并把他们放入panel容器中
        p.add(new TextField("这里是一个测试文本"));
        p.add(new Button("这是一个测试按钮"));
        //把panel放入win中
        frame.add(p);
        //设置window可见
        frame.setBounds(100,100,500,300);
        frame.setVisible(true);
    }
}
