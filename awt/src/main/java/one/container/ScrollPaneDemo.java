package one.container;

import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 14:09
 * @Description
 */
public class ScrollPaneDemo {
    public static void main(String[] args) {
        Frame frame = new Frame("演示ScrollPane");
        //创建一个ScrollPane对象
        ScrollPane sp = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
        //往ScrollPane中添加内容
        sp.add(new TextField("这是测试文本"));
        sp.add(new Button("这是测试按钮"));
        //把ScrollPane添加到Frame中
        frame.add(sp);
        frame.setBounds(100,100,500,300);
        frame.setVisible(true);
    }
}
