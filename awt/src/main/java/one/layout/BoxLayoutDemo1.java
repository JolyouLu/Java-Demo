package one.layout;

import javax.swing.*;
import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 15:36
 * @Description
 *
 */
public class BoxLayoutDemo1 {
    public static void main(String[] args) {
        Frame frame = new Frame("这里测试BoxLayout");
        //基于frame容器，创建一个BoxLayout对象，并且该对象存放组件是垂直存放
        BoxLayout boxLayout = new BoxLayout(frame, BoxLayout.Y_AXIS);
        //吧BoxLayout对象设置到frame中
        frame.setLayout(boxLayout);
        //往fram中添加2个按钮
        frame.add(new Button("按钮1"));
        frame.add(new Button("按钮2"));
        frame.pack();
        frame.setVisible(true);
    }
}
